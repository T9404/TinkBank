package com.academy.fintech.pg.core.service.payment;

import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response.CreatePaymentResponse;
import com.academy.fintech.pg.core.service.payment.db.PaymentEntity;
import com.academy.fintech.pg.core.service.payment.db.PaymentService;
import com.academy.fintech.pg.core.service.payment.enums.PaymentEvent;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import com.academy.fintech.pg.core.service.payment.product_engine.client.ClientPaymentService;
import com.academy.fintech.pg.rest.payment.v1.dto.CreatePaymentRequest;
import com.example.disbursement.SendDisbursementRequest;
import com.example.payment.ClientPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import static com.academy.fintech.pg.core.conveter.ProtobufConverter.toBigDecimal;
import static com.academy.fintech.pg.core.conveter.ProtobufConverter.toProtobufDecimalValue;

@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {
    private final ClientPaymentService clientPaymentService;
    private final PaymentService paymentService;

    @Value("${bank.identification.number}")
    private String bankIdentificationNumber;

    public void sendDisbursement(SendDisbursementRequest request) {
        var payment = buildPaymentEntity(request);
        paymentService.save(payment);
    }

    public CreatePaymentResponse sendClientPayment(CreatePaymentRequest request) {
        validateReceiverAccountNumber(request.receiverAccountNumber());

        var clientPaymentRequest = buildClientPaymentRequest(request);
        var response = clientPaymentService.sendPaymentRequest(clientPaymentRequest);

        return CreatePaymentResponse.builder()
                .status(response.getStatus())
                .build();
    }

    private PaymentEntity buildPaymentEntity(SendDisbursementRequest request) {
        return PaymentEntity.builder()
                .paymentAmount(toBigDecimal(request.getAmount()))
                .status(PaymentStatus.CREATED.name())
                .backoffTime(OffsetDateTime.now())
                .senderAccountId(request.getApplicationId())
                .receiverAccountId(bankIdentificationNumber)
                .attemptedBackoffCount(0)
                .build();
    }

    private void validateReceiverAccountNumber(String receiverAccountNumber) {
        if (!receiverAccountNumber.equals(bankIdentificationNumber)) {
            throw new BusinessException(PaymentEvent.INVALID_RECEIVER_ACCOUNT, "Invalid receiver account number");
        }
    }

    private ClientPaymentRequest buildClientPaymentRequest(CreatePaymentRequest request) {
        return ClientPaymentRequest.newBuilder()
                .setApplicationId(request.senderAccountNumber())
                .setAmount(toProtobufDecimalValue(request.requestedDisbursementAmount()))
                .build();
    }
}
