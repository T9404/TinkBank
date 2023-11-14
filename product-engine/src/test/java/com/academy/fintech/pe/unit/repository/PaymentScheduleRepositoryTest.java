package com.academy.fintech.pe.unit.repository;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentScheduleRepositoryTest {

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
    public void testSavePayment() {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.randomUUID());
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreementNumber(UUID.randomUUID());
        payment.setVersion(1);
        paymentScheduleRepository.save(payment);

        assert (paymentScheduleRepository.findById(payment.getId()).isPresent());
    }

    @Test
    public void testSavePaymentWithInvalidAgreementNumber() {
        PaymentSchedule payment = new PaymentSchedule();
        payment.setAgreementNumber(UUID.randomUUID());
        payment.setVersion(1);
        paymentScheduleRepository.save(payment);

        assertThrows(DataIntegrityViolationException.class, () -> paymentScheduleRepository.count());
    }

    @Test
    public void testSavePaymentWithInvalidData() {
        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(UUID.randomUUID());
        paymentScheduleRepository.save(payment);

        assertThrows(DataIntegrityViolationException.class, () -> paymentScheduleRepository.count());
    }

    @Test
    public void findByInvalidAgreementNumber() {
        assert (paymentScheduleRepository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    public void findById() {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.randomUUID());
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreementNumber(UUID.randomUUID());
        payment.setVersion(1);
        paymentScheduleRepository.save(payment);

        assert (paymentScheduleRepository.findById(payment.getId()).isPresent());
    }

    @Test
    public void testDelete() {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.randomUUID());
        agreementRepository.save(agreement);

        PaymentSchedule payment = new PaymentSchedule();
        payment.setId(agreement.getId());
        payment.setAgreementNumber(UUID.randomUUID());
        payment.setVersion(1);

        paymentScheduleRepository.save(payment);
        paymentScheduleRepository.delete(payment);

        assert (paymentScheduleRepository.findById(payment.getId()).isEmpty());
    }
}
