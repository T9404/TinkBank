package com.academy.fintech.mp.core.service.payment.db;

import com.academy.fintech.mp.core.common.BusinessException;
import com.academy.fintech.mp.core.service.payment.enums.CurrencyType;
import com.academy.fintech.mp.core.service.payment.enums.PaymentEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentDao paymentDao;

    public Payment getPaymentById(UUID paymentId) {
        return paymentDao.get(paymentId);
    }

    public List<Payment> getPaymentByStatus(String status) {
        return paymentDao.getPaymentsByStatus(status);
    }

    public void updatePaymentStatus(UUID paymentId, String status) {
        paymentDao.updateStatus(paymentId, status);
    }

    public Payment save(Payment payment) {
        validateCurrency(payment.getCurrency());

        validateRequestedDisbursementAmount(payment.getRequestedDisbursementAmount());

        return paymentDao.save(payment);
    }

    private void validateCurrency(String currency) {
        try {
            CurrencyType.valueOf(currency);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(PaymentEvent.INVALID_CURRENCY, "Currency is not supported");
        }
    }

    private void validateRequestedDisbursementAmount(BigDecimal requestedDisbursementAmount) {
        if (requestedDisbursementAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(PaymentEvent.INVALID_DISBURSEMENT_AMOUNT, "Requested disbursement amount is invalid");
        }
    }
}
