package com.academy.fintech.pe.integration.testcontainer;

import com.academy.fintech.pe.Application;
import com.example.agreement.AgreementRequest;
import com.example.agreement.AgreementResponse;
import com.example.agreement.AgreementServiceGrpc;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;
import proto.DisbursementProcessGrpc;
import proto.DisbursementRequest;
import proto.DisbursementResponse;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes= Application.class)
@SpringJUnitConfig(classes = { ServiceIntegrationConfig.class, TestContainerConfig.class })
@Testcontainers
public class AgreementTest {

    @GrpcClient("stocks")
    private AgreementServiceGrpc.AgreementServiceBlockingStub agreementServiceBlockingStub;

    @GrpcClient("stocks")
    private DisbursementProcessGrpc.DisbursementProcessBlockingStub disbursementProcessBlockingStub;

    @Test
    public void testCreationAgreement() {
        AgreementResponse response = agreementServiceBlockingStub.createAgreement(AgreementRequest.newBuilder()
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setInterest(8)
                .setLoanTerm(12)
                .setDisbursementAmount(490000)
                .setOriginationAmount(10000)
                .build());

        assertAll(
                () -> Assertions.assertTrue(response.getAgreementNumber().startsWith("CL1.0-")),
                () -> UUID.fromString(response.getAgreementNumber().substring(6))
        );
    }

    @Test
    public void testDisbursementAgreement() {
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(1640995200)
                .setNanos(0)
                .build();

        AgreementResponse creationAgreementResponse = agreementServiceBlockingStub.createAgreement(AgreementRequest.newBuilder()
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setInterest(8)
                .setLoanTerm(12)
                .setDisbursementAmount(490000)
                .setOriginationAmount(10000)
                .build());

        String agreementNumber = creationAgreementResponse.getAgreementNumber();

        DisbursementRequest request = DisbursementRequest.newBuilder()
                .setAgreementNumber(agreementNumber)
                .setPaymentDate(timestamp)
                .build();

        DisbursementResponse acceptingAgreementResponse = disbursementProcessBlockingStub.acceptDisbursementProcess(request);
        assert acceptingAgreementResponse.getMessage().equals("Agreement is activated successfully");
    }
}
