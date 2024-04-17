package com.academy.fintech.mp.core.service.scheduler;

import com.academy.fintech.mp.core.service.payment.db.Payment;
import com.academy.fintech.mp.core.service.payment.db.PaymentService;
import com.academy.fintech.mp.core.service.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PaymentAcceptingService {
    private final PaymentService paymentService;

    @Transactional
    @Scheduled(fixedRate = 1000)
    public void acceptPayments() {
        var pendingPayments = paymentService.getPaymentByStatus(PaymentStatus.PENDING.name());
        pendingPayments.forEach(this::updatePaymentStatusIfCompleted);
    }

    private void updatePaymentStatusIfCompleted(Payment payment) {
        if (isPaymentCompleted(payment)) {
            payment.setStatus(PaymentStatus.COMPLETED.name());
            paymentService.updatePaymentStatus(payment.getPaymentId(), PaymentStatus.COMPLETED.name());
        }
    }

    private boolean isPaymentCompleted(Payment payment) {
        return OffsetDateTime.now().isAfter(payment.getCompletionTime())
                && payment.getStatus().equals(PaymentStatus.PENDING.name());
    }
}
