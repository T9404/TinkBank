package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.service.payment.db.dto.PaymentFilter;

import java.util.List;

public interface FilterPaymentRepository {
    List<PaymentEntity> findAll(PaymentFilter filter);
}
