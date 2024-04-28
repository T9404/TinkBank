package com.academy.fintech.pe.integration.testcontainer;

import com.academy.fintech.pe.ProductEngineApplication;
import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.agreement.db.agreement.enums.AgreementStatus;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.enums.PaymentStatus;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import com.academy.fintech.pe.core.agreement.db.product.ProductRepository;
import com.academy.fintech.pe.core.service.balance.BalanceServiceV1;
import com.academy.fintech.pe.core.service.balance.db.BalanceRepository;
import com.academy.fintech.pe.core.service.balance.db.enums.BalanceType;
import com.academy.fintech.pe.core.service.overdue.OverdueService;
import com.academy.fintech.pe.integration.testcontainer.config.ServiceIntegrationConfig;
import com.academy.fintech.pe.integration.testcontainer.config.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = ProductEngineApplication.class)
@SpringJUnitConfig(classes = {ServiceIntegrationConfig.class, TestContainerConfig.class})
public class OverdueServiceTest {

    @Autowired
    private OverdueService overdueService;

    @Autowired
    private BalanceServiceV1 balanceServiceV1;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private PaymentSchedulePaymentRepository paymentSchedulePaymentRepository;

    @BeforeEach
    public void setup() {
        paymentSchedulePaymentRepository.deleteAll();
        balanceRepository.deleteAll();
        paymentScheduleRepository.deleteAll();
        agreementRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    public void testOverdueWithCreditBalance() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var paymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(paymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(100000));

        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        creditBalance.setBalance(new BigDecimal(10000));
        balanceServiceV1.save(creditBalance);

        overdueService.checkOverdue();

        var updatedCreditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        var updatedDebitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);

        assertThat(updatedCreditBalance.getBalance()).isEqualTo(new BigDecimal(0));
        assertThat(updatedDebitBalance.getBalance()).isEqualTo(new BigDecimal(100000));

        var payment = paymentSchedulePaymentRepository.findById(paymentSchedulePayment.getId()).get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID.getStatus());
    }

    @Test
    public void testOverdueWithDebitBalanceAndEmptyCredit() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var paymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(paymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(10000));


        overdueService.checkOverdue();


        var debitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);
        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);

        assertThat(debitBalance.getBalance()).isEqualTo(new BigDecimal(0));
        assertThat(creditBalance.getBalance()).isEqualTo(new BigDecimal(0));

        var payment = paymentSchedulePaymentRepository.findById(paymentSchedulePayment.getId()).get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID.getStatus());
    }

    @Test
    public void testOverdueWithNotEnoughCreditBalance() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var paymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(paymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(100000));

        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        creditBalance.setBalance(new BigDecimal(5000));
        balanceServiceV1.save(creditBalance);


        overdueService.checkOverdue();


        var updatedCreditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        var updatedDebitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);

        assertThat(updatedCreditBalance.getBalance()).isEqualTo(new BigDecimal(5000));
        assertThat(updatedDebitBalance.getBalance()).isEqualTo(new BigDecimal(90000));

        var payment = paymentSchedulePaymentRepository.findById(paymentSchedulePayment.getId()).get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID.getStatus());
    }

    @Test
    public void testWithoutOverdue() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var paymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().plusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(paymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(10000));


        overdueService.checkOverdue();


        var debitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);
        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);

        assertThat(debitBalance.getBalance()).isEqualTo(new BigDecimal(10000));
        assertThat(creditBalance.getBalance()).isEqualTo(new BigDecimal(0));

        var payment = paymentSchedulePaymentRepository.findById(paymentSchedulePayment.getId()).get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FUTURE.getStatus());
    }

    @Test
    public void testOverdueWithNotEnoughMoney() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var paymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(paymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(1000));


        overdueService.checkOverdue();


        var updatedCreditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        var updatedDebitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);

        assertThat(updatedCreditBalance.getBalance()).isEqualTo(new BigDecimal(-10000));
        assertThat(updatedDebitBalance.getBalance()).isEqualTo(new BigDecimal(1000));

        var payment = paymentSchedulePaymentRepository.findById(paymentSchedulePayment.getId()).get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.OVERDUE.getStatus());
    }

    @Test
    public void testWithDoubleOverdue() {
        var product = createProduct();
        var agreement = createAgreement(product);
        var paymentSchedule = createPaymentSchedule(agreement);

        var firstPaymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(10000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(9000))
                .periodNumber(1)
                .build();

        paymentSchedulePaymentRepository.save(firstPaymentSchedulePayment);

        var secondPaymentSchedulePayment = PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .status(PaymentStatus.FUTURE.getStatus())
                .paymentDate(OffsetDateTime.now().minusHours(1).toLocalDateTime())
                .periodPayment(new BigDecimal(15000))
                .interestPayment(new BigDecimal(1000))
                .principalPayment(new BigDecimal(14000))
                .periodNumber(2)
                .build();

        paymentSchedulePaymentRepository.save(secondPaymentSchedulePayment);

        balanceServiceV1.addPayment(agreement.getId(), new BigDecimal(100000));

        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        creditBalance.setBalance(new BigDecimal(11000));
        balanceServiceV1.save(creditBalance);


        overdueService.checkOverdue();


        var updatedCreditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        var updatedDebitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);

        assertThat(updatedCreditBalance.getBalance()).isEqualTo(new BigDecimal(1000));
        assertThat(updatedDebitBalance.getBalance()).isEqualTo(new BigDecimal(85000));

    }

    private Product createProduct() {
        Product product = new Product();
        product.setCode("CL 1.0");
        product.setMinTerm(12);
        product.setMaxTerm(60);
        product.setMinPrincipalAmount(new BigDecimal(10000));
        product.setMaxPrincipalAmount(new BigDecimal(500000));
        product.setMinInterest(new BigDecimal(8));
        product.setMaxInterest(new BigDecimal(12));
        product.setMinOriginationAmount(new BigDecimal(10000));
        product.setMaxOriginationAmount(new BigDecimal(500000));
        return productRepository.save(product);
    }

    private Agreement createAgreement(Product product) {
        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        agreement.setInterest(new BigDecimal(10));
        agreement.setTerm(12);
        agreement.setDisbursementDate(OffsetDateTime.now().toLocalDateTime());
        agreement.setOriginationAmount(new BigDecimal(5000));
        agreement.setPrincipalAmount(new BigDecimal(105000));
        agreement.setStatus(AgreementStatus.ACTIVE.getStatus());
        return agreementRepository.save(agreement);
    }

    private PaymentSchedule createPaymentSchedule(Agreement agreement) {
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1);
        return paymentScheduleRepository.save(paymentSchedule);
    }
}
