package com.academy.fintech.pg.integration;

import com.academy.fintech.pg.integration.configuration.ContainerTest;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response.CreatePaymentResponse;
import com.academy.fintech.pg.public_interface.error.v1.ErrorResponseDto;
import com.academy.fintech.pg.core.service.payment.db.PaymentRepository;
import com.academy.fintech.pg.core.service.payment.product_engine.client.ClientPaymentService;
import com.academy.fintech.pg.rest.payment.v1.dto.CreatePaymentRequest;
import com.example.payment.ClientPaymentResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContainerTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreatePaymentIntegrationTest {
    private static final String SENDER_ACCOUNT_NUMBER = "c0407501-ccfa-4957-aa1e-b52912ec2c9b";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100000);
    private static final String CURRENCY = "USD";

    @LocalServerPort
    private Integer port;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${bank.identification.number}")
    private String bankIdentificationNumber;

    @MockBean
    private ClientPaymentService clientPaymentService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        paymentRepository.deleteAll();
    }

    @Test
    public void testCreatePayment() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(bankIdentificationNumber)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CURRENCY)
                .build();

        when(clientPaymentService.sendPaymentRequest(any())).thenReturn(ClientPaymentResponse.newBuilder()
                .setStatus("SUCCESS").build());

        var response = RestAssured.given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .statusCode(200)
                .extract()
                .as(CreatePaymentResponse.class);

        assertThat(response.status()).isEqualTo("SUCCESS");
    }


    @Test
    public void testCreateWithFakeBankId() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber("fake-bank-id")
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CURRENCY)
                .build();

        when(clientPaymentService.sendPaymentRequest(any())).thenReturn(ClientPaymentResponse.newBuilder()
                .setStatus("SUCCESS").build());

        var response = RestAssured.given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(response.message()).isEqualTo("Invalid receiver account number");
    }

}
