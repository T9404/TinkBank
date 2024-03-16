package com.academy.fintech.mp.core.service.payment.db;

import java.util.List;
import java.util.UUID;

public interface PaymentDao {
    Payment save(Payment payment);

    Payment get(UUID paymentId);

    List<Payment> getPaymentsByStatus(String status);

    void updateStatus(UUID paymentId, String status);

    void deleteAll();
}
