package com.academy.fintech.pg.core.service.scheduler;

import com.academy.fintech.pg.core.merchantProvider.client.MerchantProviderService;
import com.academy.fintech.pg.core.origination.client.AcceptPaymentService;
import com.academy.fintech.pg.core.service.payment.db.PaymentEntity;
import com.academy.fintech.pg.core.service.payment.db.PaymentService;
import com.academy.fintech.pg.core.service.payment.db.dto.PaymentFilter;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import com.example.payment.AcceptPaymentRequest;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request.CreatePaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.academy.fintech.pg.core.calculation.ExponentialBackoffCalculator.calculateBackoffTime;
import static com.academy.fintech.pg.core.conveter.ProtobufConverter.convertOffsetDateTimeToGoogleTimestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCheckingStatusService {
    private final MerchantProviderService merchantProviderService;
    private final AcceptPaymentService acceptPaymentService;
    private final PaymentService paymentService;

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void checkPaymentStatus() {
        var filter = PaymentFilter.builder()
                .status(PaymentStatus.PENDING.name())
                .end(OffsetDateTime.now())
                .build();

        var pendingPayments = getPendingPayments(filter);

        log.info("Sending payments {}: {}", pendingPayments.size(), pendingPayments);
        pendingPayments.forEach(this::updatePaymentStatus);
    }

    @Scheduled(fixedRate = 5000)
    public void createPayments() {
        var filter = PaymentFilter.builder()
                .status(PaymentStatus.CREATED.name())
                .end(OffsetDateTime.now())
                .build();

        var createdPayments = paymentService.fetchPaymentsWithFilter(filter);

        log.info("Sending payments {}: {}", createdPayments.size(), createdPayments);
        createdPayments.forEach(this::createPayment);
    }

    private List<PaymentEntity> getPendingPayments(PaymentFilter filter) {
        return paymentService.fetchPaymentsWithFilter(filter)
                .stream()
                .filter(payment -> Optional.ofNullable(payment.getMerchantId()).isPresent())
                .toList();
    }

    private void updatePaymentStatus(PaymentEntity payment) {
        log.info("Checking payment status for paymentId: {}", payment.getPaymentId());
        merchantProviderService.getPaymentStatus(payment.getMerchantId())
                .doOnNext(payment::setStatus)
                .doOnNext(status -> handlePaymentStatus(payment, status))
                .doOnNext(status -> paymentService.save(payment))
                .doOnNext(status -> log.info("Payment status for paymentId: {} is {}", payment.getPaymentId(), status))
                .subscribe();
    }

    private void handlePaymentStatus(PaymentEntity payment, String status) {
        payment.setStatus(status);

        if (PaymentStatus.PENDING.name().equals(status)) {
            handlePendingPayment(payment);
            return;
        }

        paymentService.save(payment);
        var acceptPaymentRequest = createAcceptPaymentRequest(payment);
        acceptPaymentService.acceptPayment(acceptPaymentRequest);
    }

    private void handlePendingPayment(PaymentEntity payment) {
        var attemptedBackoffCount = payment.getAttemptedBackoffCount() + 1;
        var backoffTime = calculateBackoffTime(payment.getBackoffTime(), attemptedBackoffCount);

        payment.setBackoffTime(backoffTime);
        paymentService.save(payment);
    }

    private AcceptPaymentRequest createAcceptPaymentRequest(PaymentEntity payment) {
        var convertedBackoffTime = convertOffsetDateTimeToGoogleTimestamp(payment.getBackoffTime());

        return AcceptPaymentRequest.newBuilder()
                .setApplicationId(payment.getSenderAccountId())
                .setPaymentDate(convertedBackoffTime)
                .setStatus(payment.getStatus())
                .build();
    }

    private void createPayment(PaymentEntity payment) {
        log.info("Creating payment for paymentId: {}", payment.getPaymentId());
        var createPaymentRequest = buildPaymentRequest(payment);

        merchantProviderService.createPayment(createPaymentRequest)
                .doOnNext(payment::setMerchantId)
                .doOnNext(p -> payment.setStatus(PaymentStatus.PENDING.name()))
                .doOnNext(p -> paymentService.save(payment))
                .doOnNext(p -> log.info("Payment SUCCESSFULLY created for paymentId: {}", payment.getPaymentId()))
                .subscribe();
    }

    private CreatePaymentRequest buildPaymentRequest(PaymentEntity payment) {
        return CreatePaymentRequest.builder()
                .currency("RUB")
                .requestedDisbursementAmount(payment.getPaymentAmount())
                .receiverAccountNumber(payment.getReceiverAccountId())
                .senderAccountNumber(payment.getSenderAccountId())
                .build();
    }

}
