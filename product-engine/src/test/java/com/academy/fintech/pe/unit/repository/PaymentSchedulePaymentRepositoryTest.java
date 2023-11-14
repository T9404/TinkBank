package com.academy.fintech.pe.unit.repository;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(UUID.randomUUID());
        paymentSchedule.setAgreementNumber(UUID.randomUUID());
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assert (paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
    }

    @Test
    public void testDeletePaymentSchedulePayment() {
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(UUID.randomUUID());
        paymentSchedule.setAgreementNumber(UUID.randomUUID());
        paymentSchedule.setVersion(1);
        paymentScheduleRepository.save(paymentSchedule);

        assert (paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent());
        paymentScheduleRepository.delete(paymentSchedule);

        assertAll(() -> assertFalse(paymentScheduleRepository.findById(paymentSchedule.getId()).isPresent()));
    }

    @Test
    public void testFindByPaymentScheduleIdNotFound() {
        List<PaymentSchedulePayment> paymentSchedulePaymentList = paymentSchedulePaymentRepository
                .findByPaymentScheduleId(UUID.randomUUID());
        assert (paymentSchedulePaymentList.isEmpty());
    }
}
