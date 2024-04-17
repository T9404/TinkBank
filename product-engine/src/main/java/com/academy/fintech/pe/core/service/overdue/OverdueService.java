package com.academy.fintech.pe.core.service.overdue;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.enums.PaymentStatus;
import com.academy.fintech.pe.core.service.balance.BalanceServiceV1;
import com.academy.fintech.pe.core.service.balance.db.Balance;
import com.academy.fintech.pe.core.service.balance.db.enums.BalanceType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OverdueService {
    private final PaymentSchedulePaymentService paymentSchedulePaymentService;
    private final BalanceServiceV1 balanceServiceV1;

    @Transactional
    @Scheduled(fixedRateString = "${scheduler.checkOverdueRate}", initialDelay = 1000)
    public void checkOverdue() {
        var overdueAgreements = paymentSchedulePaymentService.findAllOverduePayments();
        overdueAgreements.forEach(this::updatePaymentStatusIfOverdue);
    }

    private void updatePaymentStatusIfOverdue(PaymentSchedulePayment payment) {
        var agreement = payment.getPaymentSchedule().getAgreement();

        var creditBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.CREDIT);
        if (updateBalanceAndSetPaymentStatus(creditBalance, payment)) {
            return;
        }

        var debitBalance = balanceServiceV1.getBalance(agreement.getId(), BalanceType.DEBIT);
        if (updateBalanceAndSetPaymentStatus(debitBalance, payment)) {
            return;
        }

        creditBalance.setBalance(creditBalance.getBalance().subtract(payment.getPeriodPayment()));
        balanceServiceV1.save(creditBalance);
        payment.setStatus(PaymentStatus.OVERDUE.getStatus());
        paymentSchedulePaymentService.save(payment);
    }

    private boolean updateBalanceAndSetPaymentStatus(Balance balance, PaymentSchedulePayment payment) {
        var paymentValue = payment.getPeriodPayment();

        if (balance.getBalance().compareTo(paymentValue) >= 0) {
            balance.setBalance(balance.getBalance().subtract(paymentValue));
            balanceServiceV1.save(balance);
            payment.setStatus(PaymentStatus.PAID.getStatus());
            paymentSchedulePaymentService.save(payment);
            return true;
        }

        return false;
    }
}
