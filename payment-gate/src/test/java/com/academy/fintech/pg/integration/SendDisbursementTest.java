package com.academy.fintech.pg.integration;

import com.academy.fintech.pg.integration.configuration.ContainerTest;
import com.academy.fintech.pg.integration.configuration.ServiceIntegrationConfig;
import com.academy.fintech.pg.core.service.payment.db.PaymentRepository;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import com.example.disbursement.DecimalValue;
import com.example.disbursement.SendDisbursementRequest;
import com.example.disbursement.SendDisbursementServiceGrpc;
import com.google.protobuf.ByteString;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ContainerTest
@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {ServiceIntegrationConfig.class})
public class SendDisbursementTest {
    private static final String SENDER_ACCOUNT_ID = "c0407501-ccfa-4957-aa1e-b52912ec2c9b";

    @GrpcClient("stocks")
    private SendDisbursementServiceGrpc.SendDisbursementServiceBlockingStub sendDisbursementServiceBlockingStub;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSendDisbursement() {
        var sendDisbursementRequest = SendDisbursementRequest.newBuilder()
                .setApplicationId(SENDER_ACCOUNT_ID)
                .setAmount(DecimalValue.newBuilder()
                        .setValue(ByteString.copyFromUtf8("100000"))
                        .setScale(2)
                        .setPrecision(4)
                        .build())
                .build();

        var response = sendDisbursementServiceBlockingStub.sendDisbursement(sendDisbursementRequest);

        var payment = paymentRepository.findBySenderAccountId(SENDER_ACCOUNT_ID);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING.name());

        assertThat(payment).isNotEmpty();
        assertThat(payment.get().getStatus()).isEqualTo(PaymentStatus.CREATED.name());
    }
}
