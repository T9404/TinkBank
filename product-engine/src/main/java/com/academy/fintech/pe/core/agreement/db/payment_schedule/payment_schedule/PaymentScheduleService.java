package com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentSchedule> findAllByAgreement(Agreement agreement) {
        return paymentScheduleRepository.findAllByAgreement(agreement);
    }

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
