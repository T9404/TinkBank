package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.enums.PaymentStatus;
import com.academy.fintech.pe.core.service.agreement.util.AgreementCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentSchedulePaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentSchedulePaymentService.class);
    private PaymentSchedulePaymentRepository paymentSchedulePaymentRepository;

    @Autowired
    public void setPaymentSchedulePaymentRepository(PaymentSchedulePaymentRepository paymentSchedulePaymentRepository) {
        this.paymentSchedulePaymentRepository = paymentSchedulePaymentRepository;
    }

    public void savePaymentSchedulePayment(Agreement agreement, Timestamp disbursementDate, PaymentSchedule paymentSchedule) {
        List<PaymentSchedulePayment> payments = calculatePayments(agreement, disbursementDate, paymentSchedule);
        logger.info("Initial payment graphic generated: {}", payments);
        paymentSchedulePaymentRepository.saveAll(payments);
    }

    private List<PaymentSchedulePayment> calculatePayments(Agreement agreement, Timestamp disbursementDate, PaymentSchedule paymentSchedule) {
        int numberOfPeriods = agreement.getTerm();
        BigDecimal principal = agreement.getPrincipalAmount();
        BigDecimal annualInterestRate = agreement.getInterest();
        BigDecimal totalInterestPayment = BigDecimal.ZERO;
        BigDecimal balance = agreement.getPrincipalAmount();
        BigDecimal monthlyPayment = AgreementCalculator.calculatePMT(principal, annualInterestRate, numberOfPeriods);
        List<PaymentSchedulePayment> payments = new ArrayList<>();

        for (int i = 0; i < numberOfPeriods; i++) {
            BigDecimal interestPayment = AgreementCalculator.calculateIPMT(principal, annualInterestRate, numberOfPeriods, i + 1);
            BigDecimal principalPayment = AgreementCalculator.calculatePPMT(principal, annualInterestRate, numberOfPeriods, i + 1);

            totalInterestPayment = totalInterestPayment.add(interestPayment);
            balance = balance.subtract(principalPayment);
            Timestamp nextPaymentDate = AgreementCalculator.calculateNextPaymentDate(disbursementDate, i);

            PaymentSchedulePayment payment = buildPayment(paymentSchedule.getId(), nextPaymentDate.toLocalDateTime(),
                    monthlyPayment, interestPayment, principalPayment, i + 1);
            payments.add(payment);
        }

        return payments;
    }

    private PaymentSchedulePayment buildPayment(UUID paymentScheduleId, LocalDateTime paymentDate, BigDecimal periodPayment,
                                                BigDecimal interestPayment, BigDecimal principalPayment, int periodNumber) {
        return PaymentSchedulePayment.builder()
                .paymentScheduleId(paymentScheduleId)
                .paymentDate(paymentDate)
                .periodPayment(periodPayment)
                .interestPayment(interestPayment)
                .principalPayment(principalPayment)
                .periodNumber(periodNumber)
                .status(PaymentStatus.FUTURE.getStatus())
                .build();
    }
}
