package com.academy.fintech.pe.integration.repository;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import com.academy.fintech.pe.core.agreement.db.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static com.academy.fintech.pe.integration.factory.AgreementFactory.createAgreement;
import static com.academy.fintech.pe.integration.factory.ProductFactory.createProduct;
import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentSchedulePaymentRepositoryTest {

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
    void testSavePaymentSchedulePayment() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(String.valueOf(UUID.randomUUID()));
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assertTrue(paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
    }

    @Test
    void testDeletePaymentSchedulePayment() {
        Product product = createProduct();
        Agreement agreement = createAgreement(product);

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(String.valueOf(UUID.randomUUID()));
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assertTrue(paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
        paymentScheduleRepository.delete(paymentSchedule);

        assertAll(() -> assertFalse(paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent()));
    }

    @Test
    void testFindByPaymentScheduleIdNotFound() {
        List<PaymentSchedulePayment> paymentSchedulePaymentList = paymentSchedulePaymentRepository
                .findByPaymentScheduleId(String.valueOf(UUID.randomUUID()));
        assertTrue(paymentSchedulePaymentList.isEmpty());
    }
}
