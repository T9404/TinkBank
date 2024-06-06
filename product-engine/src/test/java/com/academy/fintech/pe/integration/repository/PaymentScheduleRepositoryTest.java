package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.academy.fintech.pe.integration.factory.AgreementFactory.createAgreement;
import static com.academy.fintech.pe.integration.factory.ProductFactory.createProduct;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentScheduleRepositoryTest {

    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    PaymentSchedulePaymentRepository paymentSchedulePaymentRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @BeforeEach
    void setup() {
        paymentScheduleRepository.deleteAll();
        paymentSchedulePaymentRepository.deleteAll();
    }

    @Test
    void testSavePayment() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreement(agreement);
        payment.setVersion(1);
        paymentScheduleRepository.save(payment);

        assertTrue(paymentScheduleRepository.findById(payment.getId()).isPresent());
    }

    @Test
    void testSavePaymentWithInvalidAgreementNumber() {
        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(String.valueOf(UUID.randomUUID()));
        paymentScheduleRepository.save(payment);

        assertThrows(DataIntegrityViolationException.class, () -> paymentScheduleRepository.count());
    }

    @Test
    void testSavePaymentWithInvalidData() {
        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(String.valueOf(UUID.randomUUID()));
        paymentScheduleRepository.save(payment);

        assertThrows(DataIntegrityViolationException.class, () -> paymentScheduleRepository.count());
    }

    @Test
    void findByInvalidAgreementNumber() {
        assertTrue(paymentScheduleRepository.findById(String.valueOf(UUID.randomUUID())).isEmpty());
    }

    @Test
    void findById() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreement(agreement);
        payment.setVersion(1);
        paymentScheduleRepository.save(payment);

        assertTrue(paymentScheduleRepository.findById(payment.getId()).isPresent());
    }

    @Test
    void testDelete() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreement(agreement);
        payment.setVersion(1);

        paymentScheduleRepository.save(payment);
        paymentScheduleRepository.delete(payment);

        assertTrue(paymentScheduleRepository.findById(payment.getId()).isEmpty());
    }
}
