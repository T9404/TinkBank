package com.academy.fintech.mp.core.service.payment;

import com.academy.fintech.mp.core.service.generator.RandomTimeGenerator;
import com.academy.fintech.mp.core.service.payment.db.Payment;
import com.academy.fintech.mp.core.service.payment.enums.PaymentStatus;
import com.academy.fintech.mp.core.service.payment.payment_gate.client.PaymentGateClient;
import com.academy.fintech.mp.public_interface.payment.v1.dto.AddPaymentResponse;
import com.academy.fintech.mp.public_interface.payment.v1.dto.CreatePaymentResponse;
import com.academy.fintech.mp.rest.payment.v1.dto.CreatePaymentRequest;
import com.academy.fintech.mp.core.service.payment.db.PaymentService;
import com.academy.fintech.mp.public_interface.payment.v1.dto.GetPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceV1 {
    private final PaymentGateClient paymentGateClient;
    private final PaymentService paymentService;

    public GetPaymentResponse getActualStatus(UUID paymentId) {
        var payment = paymentService.getPaymentById(paymentId);
        log.info("Payment with id {} has been found", payment.getPaymentId());

        return new GetPaymentResponse(payment.getStatus());
    }

    public CreatePaymentResponse save(CreatePaymentRequest request) {
        var payment = createPaymentFromRequest(request, PaymentStatus.PENDING);

        var savedPayment = paymentService.save(payment);
        log.info("Payment with id {} has been saved", savedPayment.getPaymentId());

        return new CreatePaymentResponse(savedPayment.getPaymentId(), savedPayment.getStatus());
    }

    public Mono<AddPaymentResponse> addUserPayment(CreatePaymentRequest request) {
        var payment = createPaymentFromRequest(request, PaymentStatus.COMPLETED);
        paymentService.save(payment);

        log.info("Payment with id {} has been saved", payment.getPaymentId());
        return paymentGateClient.sendPaymentInfo(request);
    }

    private Payment createPaymentFromRequest(CreatePaymentRequest request, PaymentStatus status) {
        return Payment.builder()
                .receiverAccountNumber(request.receiverAccountNumber())
                .senderAccountNumber(request.senderAccountNumber())
                .currency(request.currency())
                .requestedDisbursementAmount(request.requestedDisbursementAmount())
                .status(status.name())
                .completionTime(RandomTimeGenerator.getRandomTime())
                .build();
    }
}
