package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.service.payment.db.dto.PaymentFilter;
import com.academy.fintech.pg.core.service.payment.enums.PaymentEvent;
import com.academy.fintech.pg.core.service.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<PaymentEntity> fetchPaymentsWithFilter(PaymentFilter paymentFilter) {
        return paymentRepository.findAll(paymentFilter);
    }

    public PaymentEntity save(PaymentEntity paymentEntity) {
        checkDisbursementAmount(paymentEntity.getPaymentAmount());

        checkStatus(paymentEntity.getStatus());

        return paymentRepository.save(paymentEntity);
    }

    private void checkDisbursementAmount(BigDecimal disbursementAmount) {
        if (disbursementAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(PaymentEvent.DISBURSEMENT_INVALID_AMOUNT, "Invalid disbursement amount");
        }
    }

    private void checkStatus(String status) {
        try {
            PaymentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(PaymentEvent.DISBURSEMENT_INVALID_STATUS, "Invalid disbursement status");
        }
    }
}

