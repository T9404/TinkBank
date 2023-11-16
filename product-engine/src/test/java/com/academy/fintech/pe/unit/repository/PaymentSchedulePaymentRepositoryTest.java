package com.academy.fintech.pe.unit.repository;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentSchedulePaymentRepositoryTest {

    @Autowired
    PaymentSchedulePaymentRepository paymentSchedulePaymentRepository;

    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;

    @BeforeEach
    void setup() {
        paymentSchedulePaymentRepository.deleteAll();
        paymentScheduleRepository.deleteAll();
    }

    @Test
    public void testSavePaymentSchedulePayment() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(String.valueOf(UUID.randomUUID()));
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assert (paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
    }

    @Test
    public void testDeletePaymentSchedulePayment() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(String.valueOf(UUID.randomUUID()));
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assert (paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
        paymentScheduleRepository.delete(paymentSchedule);

        assertAll(() -> assertFalse(paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent()));
    }

    @Test
    public void testFindByPaymentScheduleIdNotFound() {
        List<PaymentSchedulePayment> paymentSchedulePaymentList = paymentSchedulePaymentRepository
                .findByPaymentScheduleId(String.valueOf(UUID.randomUUID()));
        assert (paymentSchedulePaymentList.isEmpty());
    }

    private Agreement createAgreement(Product product) {
        Agreement agreement = new Agreement();
        agreement.setProduct(product);
        agreement.setClientId("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        agreement.setInterest(new BigDecimal(8));
        agreement.setTerm(12);
        agreement.setOriginationAmount(new BigDecimal(10000));
        agreement.setPrincipalAmount(new BigDecimal(500000));
        agreement.setStatus(AgreementStatus.NEW.getStatus());
        return agreement;
    }

    private Product createProduct() {
        Product product = new Product();
        product.setCode("CL 1.0.1.1.1");
        product.setMinTerm(12);
        product.setMaxTerm(60);
        product.setMinPrincipalAmount(new BigDecimal(10000));
        product.setMaxPrincipalAmount(new BigDecimal(500000));
        product.setMinInterest(new BigDecimal(8));
        product.setMaxInterest(new BigDecimal(12));
        product.setMinOriginationAmount(new BigDecimal(10000));
        product.setMaxOriginationAmount(new BigDecimal(500000));
        return product;
    }
}
