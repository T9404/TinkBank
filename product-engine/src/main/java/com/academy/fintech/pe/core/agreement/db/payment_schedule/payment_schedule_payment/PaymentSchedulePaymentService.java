package com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.dto.PaymentScheduleDto;
import com.academy.fintech.pe.core.calculator.AgreementCalculator;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentSchedulePaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentSchedulePaymentService.class);
    private final PaymentSchedulePaymentRepository paymentSchedulePaymentRepository;

    public List<PaymentSchedulePayment> findAllByPaymentSchedule(PaymentSchedule paymentSchedule) {
        return paymentSchedulePaymentRepository.findAllByPaymentSchedule(paymentSchedule);
    }

    public List<PaymentSchedulePayment> findAllOverduePayments() {
        return paymentSchedulePaymentRepository
                .findAllByStatusAndPaymentDateIsBefore(PaymentStatus.FUTURE.getStatus(), OffsetDateTime.now().toLocalDateTime());
    }

    public void savePaymentSchedulePayment(Agreement agreement, Timestamp disbursementDate, PaymentSchedule paymentSchedule) {
        List<PaymentSchedulePayment> payments = calculatePayments(agreement, disbursementDate, paymentSchedule);
        logger.info("Initial payment graphic generated: {}", payments);
        paymentSchedulePaymentRepository.saveAll(payments);
    }

    public void save(PaymentSchedulePayment payment) {
        paymentSchedulePaymentRepository.save(payment);
    }

    private List<PaymentSchedulePayment> calculatePayments(Agreement agreement, Timestamp disbursementDate, PaymentSchedule paymentSchedule) {
        int numberOfPeriods = agreement.getTerm();
        PaymentScheduleDto paymentScheduleDto = initializePaymentScheduleDto(agreement);
        List<PaymentSchedulePayment> payments = new ArrayList<>();

        for (int periodNumber = 0; periodNumber < numberOfPeriods; periodNumber++) {
            BigDecimal interestPayment = AgreementCalculator.calculateIPMT(paymentScheduleDto.principalPayment(),
                    paymentScheduleDto.annualInterestRate(), numberOfPeriods, periodNumber + 1);
            BigDecimal principalPayment = AgreementCalculator.calculatePPMT(paymentScheduleDto.principalPayment(),
                    paymentScheduleDto.annualInterestRate(), numberOfPeriods, periodNumber + 1);
            Timestamp nextPaymentDate = AgreementCalculator.calculateNextPaymentDate(disbursementDate, periodNumber);

            PaymentSchedulePayment payment = buildPayment(paymentSchedule, nextPaymentDate.toLocalDateTime(),
                    paymentScheduleDto.monthlyPayment(), interestPayment, principalPayment, periodNumber + 1);
            payments.add(payment);
        }

        return payments;
    }

    private PaymentScheduleDto initializePaymentScheduleDto(Agreement agreement) {
        int numberOfPeriods = agreement.getTerm();
        BigDecimal principal = agreement.getPrincipalAmount();
        BigDecimal annualInterestRate = agreement.getInterest();
        BigDecimal balance = agreement.getPrincipalAmount();
        BigDecimal monthlyPayment = AgreementCalculator.calculatePMT(principal, annualInterestRate, numberOfPeriods);
        return new PaymentScheduleDto(monthlyPayment, annualInterestRate, balance);
    }

    private PaymentSchedulePayment buildPayment(PaymentSchedule paymentSchedule, LocalDateTime paymentDate, BigDecimal periodPayment,
                                                BigDecimal interestPayment, BigDecimal principalPayment, int periodNumber) {
        return PaymentSchedulePayment.builder()
                .paymentSchedule(paymentSchedule)
                .paymentDate(paymentDate)
                .periodPayment(periodPayment)
                .interestPayment(interestPayment)
                .principalPayment(principalPayment)
                .periodNumber(periodNumber)
                .status(PaymentStatus.FUTURE.getStatus())
                .build();
    }
}
