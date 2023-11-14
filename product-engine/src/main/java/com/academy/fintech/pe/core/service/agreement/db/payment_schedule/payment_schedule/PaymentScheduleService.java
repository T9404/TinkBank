package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentScheduleService {
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    public void setPaymentScheduleRepository(PaymentScheduleRepository paymentScheduleRepository) {
        this.paymentScheduleRepository = paymentScheduleRepository;
    }

    public PaymentSchedule savePaymentSchedule(Agreement agreement) {
        int version = determineNumberOfPaymentVersions(agreement.getId());
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setAgreementNumber(agreement.getId());
        paymentSchedule.setVersion(version);
        paymentScheduleRepository.save(paymentSchedule);
        return paymentSchedule;
    }

    public int determineNumberOfPaymentVersions(UUID agreementNumber) {
        int latestVersion = paymentScheduleRepository.getLatestVersionByAgreementNumber(agreementNumber).orElse(0);
        return latestVersion + 1;
    }
}
