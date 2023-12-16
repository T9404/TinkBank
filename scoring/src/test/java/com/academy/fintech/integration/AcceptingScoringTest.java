package com.academy.fintech.integration;

import com.academy.fintech.Application;
import com.example.payment.AcceptingScoringRequest;
import com.example.payment.AcceptingScoringResponse;
import com.example.payment.AcceptingScoringServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes= Application.class)
@SpringJUnitConfig(classes = { ServiceIntegrationConfig.class})
public class AcceptingScoringTest {

    @GrpcClient("stocks")
    private AcceptingScoringServiceGrpc.AcceptingScoringServiceBlockingStub acceptingScoringServiceBlockingStub;

    @Test
    public void testAcceptingScoring() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(50000)
                .setDisbursementAmount(100000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(2, response.getResult());
    }

    @Test
    public void testAcceptingScoringWithLargeRegularPayment() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(1000)
                .setDisbursementAmount(1000000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(1, response.getResult());
    }

    @Test
    public void testAcceptingScoringWithLargeLatePayment() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(50000)
                .setDisbursementAmount(1000000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("b58ed763-928c-4155-bee9-fdbaaadc15c2")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(0, response.getResult());
    }

    @Test
    public void testAcceptingScoringWithSmallLatePayment() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(50000)
                .setDisbursementAmount(1000000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("b58ed763-928c-4155-bee9-fdbaaadc15c3")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(1, response.getResult());
    }

    @Test
    public void testAcceptingScoringWithLargeLatePaymentAndBigRegularPayment() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(1000)
                .setDisbursementAmount(1000000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f7")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(-1, response.getResult());
    }

    @Test
    public void testAcceptingScoringWithSmallLatePaymentAndBigRegularPayment() {
        AcceptingScoringRequest request = AcceptingScoringRequest.newBuilder()
                .setInterest(12)
                .setOriginationAmount(5000)
                .setSalary(1000)
                .setDisbursementAmount(1000000)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc1510")
                .setLoanTerm(12)
                .build();

        AcceptingScoringResponse response = acceptingScoringServiceBlockingStub.acceptScoring(request);
        Assertions.assertEquals(0, response.getResult());
    }
}
