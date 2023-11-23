package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentSchedule savePaymentSchedule(Agreement agreement) {
        int version = determineNumberOfPaymentVersions(agreement);
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(version);
        paymentScheduleRepository.save(paymentSchedule);
        return paymentSchedule;
    }

    private int determineNumberOfPaymentVersions(Agreement agreement) {
        Optional<PaymentSchedule> latestPaymentScheduleOptional =
                paymentScheduleRepository.findFirstByAgreementOrderByVersionDesc(agreement);
        int latestVersion = latestPaymentScheduleOptional.map(PaymentSchedule::getVersion).orElse(0);
        return latestVersion + 1;
    }
}
