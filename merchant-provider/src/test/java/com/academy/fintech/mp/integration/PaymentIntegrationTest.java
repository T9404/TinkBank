package com.academy.fintech.mp.integration;

import com.academy.fintech.mp.core.service.generator.RandomTimeGenerator;
import com.academy.fintech.mp.core.service.payment.db.PaymentDao;
import com.academy.fintech.mp.core.service.payment.enums.CurrencyType;
import com.academy.fintech.mp.core.service.payment.payment_gate.client.PaymentGateClient;
import com.academy.fintech.mp.public_interface.payment.v1.dto.AddPaymentResponse;
import com.academy.fintech.mp.rest.payment.v1.dto.CreatePaymentRequest;
import org.junit.jupiter.api.Test;
import com.academy.fintech.mp.integration.configuration.ContainerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ContainerTest
@TestPropertySource(properties = {"max-waiting-seconds=100000"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentIntegrationTest {
    private static final String RECEIVER_ACCOUNT_NUMBER = "c0407501-ccfa-4957-aa1e-b52912ec2c9b";
    private static final String SENDER_ACCOUNT_NUMBER = "c0407501-ccfa-4957-aa1e-b52912ec2c9c";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100000);

    @LocalServerPort
    private Integer port;

    @Autowired
    private PaymentDao paymentDao;

    @MockBean
    private PaymentGateClient paymentGateClient;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        paymentDao.deleteAll();
    }

    @Test
    public void testCreatePayment() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(RECEIVER_ACCOUNT_NUMBER)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CurrencyType.GBP.name())
                .build();

        given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetPayment() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(RECEIVER_ACCOUNT_NUMBER)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CurrencyType.RUB.name())
                .build();

        try (MockedStatic<RandomTimeGenerator> mocked = mockStatic(RandomTimeGenerator.class)) {
            mocked.when(RandomTimeGenerator::getRandomTime).thenReturn(OffsetDateTime.now().plusYears(1));

            var paymentId = given()
                    .contentType("application/json")
                    .body(createPaymentRequest)
                    .when()
                    .post("/api/v1/payments")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("PENDING"))
                    .extract()
                    .path("payment_id");

            given()
                    .contentType("application/json")
                    .when()
                    .get("/api/v1/payments/status/" + paymentId)
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("PENDING"));
        }
    }

    @Test
    public void testGetPaymentNotFound() {
        var randomPaymentId = UUID.fromString("d0407501-ccfa-4957-aa1e-b52912ec2c1d");

        given()
                .contentType("application/json")
                .when()
                .get("/api/v1/payments/status/" + randomPaymentId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreatePaymentBadRequest() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(null)
                .senderAccountNumber(null)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CurrencyType.USD.name())
                .build();

        given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .statusCode(500);
    }

    @Test
    public void testGetPaymentBadRequest() {
        given()
                .contentType("application/json")
                .when()
                .get("/api/v1/payments/status/")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreatePaymentWithInvalidCurrency() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(RECEIVER_ACCOUNT_NUMBER)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency("FAKE")
                .build();

        var response = given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .extract()
                .response();

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Currency is not supported"));
    }

    @Test
    public void testCreateWithInvalidAmount() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(RECEIVER_ACCOUNT_NUMBER)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(BigDecimal.valueOf(-1000))
                .currency(CurrencyType.EUR.name())
                .build();

        var response = given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments")
                .then()
                .extract()
                .response();

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Requested disbursement amount is invalid"));
    }

    @Test
    public void testAddUserPayment() {
        var createPaymentRequest = CreatePaymentRequest.builder()
                .receiverAccountNumber(RECEIVER_ACCOUNT_NUMBER)
                .senderAccountNumber(SENDER_ACCOUNT_NUMBER)
                .requestedDisbursementAmount(AMOUNT)
                .currency(CurrencyType.GBP.name())
                .build();

        var mockResponse = new AddPaymentResponse("COMPLETED");
        when(paymentGateClient.sendPaymentInfo(createPaymentRequest)).thenReturn(Mono.just(mockResponse));

        var response = given()
                .contentType("application/json")
                .body(createPaymentRequest)
                .when()
                .post("/api/v1/payments/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertTrue(response.getBody().asString().contains("COMPLETED"));
    }

}
