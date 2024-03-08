package com.academy.fintech.pg.integration;

import com.academy.fintech.pg.integration.configuration.ContainerTest;
import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.service.payment.db.PaymentEntity;
import com.academy.fintech.pg.core.service.payment.db.PaymentRepository;
import com.academy.fintech.pg.core.service.payment.db.PaymentService;
import com.academy.fintech.pg.core.service.payment.db.dto.PaymentFilter;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContainerTest
@ActiveProfiles("default")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServiceTest {
    private static final String SENDER_ACCOUNT_ID = "c0407501-ccfa-4957-aa1e-b52912ec2c9b";
    private static final String RECEIVER_ACCOUNT_ID = "c0407501-ccfa-4957-aa1e-b52912ec2c9b";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100000);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testSave() {
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.CREATED.name())
                .paymentAmount(AMOUNT)
                .receiverAccountId(RECEIVER_ACCOUNT_ID)
                .senderAccountId(SENDER_ACCOUNT_ID)
                .merchantId(UUID.randomUUID())
                .build();

        var savedPayment = paymentService.save(payment);

        var filter = PaymentFilter.builder()
                .status(PaymentStatus.CREATED.name())
                .end(payment.getBackoffTime())
                .paymentAmount(payment.getPaymentAmount())
                .build();

        var payments = paymentService.fetchPaymentsWithFilter(filter);

        assertThat(payments).isNotEmpty();
        assertThat(payments.get(0).getPaymentId()).isEqualTo(savedPayment.getPaymentId());
    }

    @Test
    public void testFetchPaymentsWithTimeAfter() {
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.CREATED.name())
                .paymentAmount(AMOUNT)
                .receiverAccountId(RECEIVER_ACCOUNT_ID)
                .senderAccountId(SENDER_ACCOUNT_ID)
                .merchantId(UUID.randomUUID())
                .build();

        paymentService.save(payment);

        var filter = PaymentFilter.builder()
                .end(payment.getBackoffTime())
                .build();

        var payments = paymentService.fetchPaymentsWithFilter(filter);

        assertThat(payments).isNotEmpty();
    }

    @Test
    public void testFetchPaymentsWithTimeBefore() {
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.CREATED.name())
                .paymentAmount(AMOUNT)
                .receiverAccountId(RECEIVER_ACCOUNT_ID)
                .senderAccountId(SENDER_ACCOUNT_ID)
                .merchantId(UUID.randomUUID())
                .build();

        paymentService.save(payment);

        var filter = PaymentFilter.builder()
                .start(OffsetDateTime.now())
                .build();

        var payments = paymentService.fetchPaymentsWithFilter(filter);

        assertThat(payments).isEmpty();
    }

    @Test
    public void testFetchWithFilters() {
        var payments = IntStream.range(0, 100)
                .mapToObj(i -> PaymentEntity.builder()
                        .status(i % 2 == 0 ? PaymentStatus.CREATED.name() : PaymentStatus.PENDING.name())
                        .paymentAmount(new BigDecimal(10000 + i * 100))
                        .receiverAccountId(RECEIVER_ACCOUNT_ID + i)
                        .senderAccountId(SENDER_ACCOUNT_ID + i)
                        .merchantId(UUID.randomUUID())
                        .build())
                .collect(Collectors.toList());

        payments.forEach(paymentService::save);


        // Test fetching payments with specific status
        var filterStatus = PaymentFilter.builder()
                .status(PaymentStatus.CREATED.name())
                .build();

        var createdPayments = paymentService.fetchPaymentsWithFilter(filterStatus);

        assertThat(createdPayments).isNotEmpty();
        assertThat(createdPayments.stream().allMatch(p -> p.getStatus().equals(PaymentStatus.CREATED.name())));


        // Test fetching payments with specific amount
        var amount = new BigDecimal(10000 + 49 * 100);
        var filterAmountRange = PaymentFilter.builder()
                .paymentAmount(amount)
                .build();

        var paymentsInAmountRange = paymentService.fetchPaymentsWithFilter(filterAmountRange);

        assertThat(paymentsInAmountRange).isNotEmpty();
        assertThat(paymentsInAmountRange.stream().allMatch(p -> p.getPaymentAmount().equals(amount)));
    }

    @Test
    public void testCreatePaymentWithInvalidAmount() {
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.CREATED.name())
                .paymentAmount(new BigDecimal(-10000))
                .receiverAccountId(RECEIVER_ACCOUNT_ID)
                .senderAccountId(SENDER_ACCOUNT_ID)
                .merchantId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> paymentService.save(payment))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid disbursement amount");
    }

    @Test
    public void testCreatePaymentWithInvalidStatus() {
        var payment = PaymentEntity.builder()
                .status("INVALID_STATUS")
                .paymentAmount(AMOUNT)
                .receiverAccountId(RECEIVER_ACCOUNT_ID)
                .senderAccountId(SENDER_ACCOUNT_ID)
                .merchantId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> paymentService.save(payment))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid disbursement status");
    }

}
