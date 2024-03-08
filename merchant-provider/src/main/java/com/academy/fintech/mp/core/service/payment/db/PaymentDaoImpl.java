package com.academy.fintech.mp.core.service.payment.db;

import com.academy.fintech.mp.core.service.payment.mapper.PaymentRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class PaymentDaoImpl implements PaymentDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Payment save(Payment payment) {
        var sql = """
                INSERT INTO payment (payment_id, receiver_account_number, sender_account_number,  currency, requested_disbursement_amount, completion_time, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        payment.setPaymentId(UUID.randomUUID());
        jdbcTemplate.update(sql, payment.getPaymentId(), payment.getReceiverAccountNumber(),
                payment.getSenderAccountNumber(), payment.getCurrency(), payment.getRequestedDisbursementAmount(),
                Timestamp.from(payment.getCompletionTime().toInstant()), payment.getStatus());

        return payment;
    }

    @Override
    public Payment get(UUID paymentId) {
        var sql = """
                SELECT * FROM payment WHERE payment_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), paymentId);
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        var sql = """
                SELECT * FROM payment WHERE status = ?
                """;
        return jdbcTemplate.query(sql, new PaymentRowMapper(), status);
    }

    @Override
    public void updateStatus(UUID paymentId, String status) {
        var sql = """
                UPDATE payment SET status = ? WHERE payment_id = ?
                """;
        jdbcTemplate.update(sql, status, paymentId);
    }

    @Override
    public void deleteAll() {
        var sql = """
                DELETE FROM payment
                """;
        jdbcTemplate.update(sql);
    }
}
