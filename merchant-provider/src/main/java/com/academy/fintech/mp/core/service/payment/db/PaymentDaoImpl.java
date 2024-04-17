package com.academy.fintech.mp.core.service.payment.db;

import com.academy.fintech.mp.core.service.payment.mapper.PaymentRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class PaymentDaoImpl implements PaymentDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Payment save(Payment payment) {
        var sql = """
                INSERT INTO payment (payment_id, receiver_account_number, sender_account_number,  currency, requested_disbursement_amount, completion_time, status)
                VALUES (:paymentId, :receiverAccountNumber, :senderAccountNumber, :currency, :requestedDisbursementAmount, :completionTime, :status)
                """;

        payment.setPaymentId(UUID.randomUUID());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("paymentId", payment.getPaymentId());
        params.addValue("receiverAccountNumber", payment.getReceiverAccountNumber());
        params.addValue("senderAccountNumber", payment.getSenderAccountNumber());
        params.addValue("currency", payment.getCurrency());
        params.addValue("requestedDisbursementAmount", payment.getRequestedDisbursementAmount());
        params.addValue("completionTime", Timestamp.from(payment.getCompletionTime().toInstant()));
        params.addValue("status", payment.getStatus());

        jdbcTemplate.update(sql, params);

        return payment;
    }

    @Override
    public Payment get(UUID paymentId) {
        var sql = """
                SELECT * FROM payment WHERE payment_id = :paymentId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("paymentId", paymentId);

        return jdbcTemplate.queryForObject(sql, params, new PaymentRowMapper());
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        var sql = """
                SELECT * FROM payment WHERE status = :status
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);

        return jdbcTemplate.query(sql, params, new PaymentRowMapper());
    }

    @Override
    public void updateStatus(UUID paymentId, String status) {
        var sql = """
                UPDATE payment SET status = :status WHERE payment_id = :paymentId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", status);
        params.addValue("paymentId", paymentId);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll() {
        var sql = """
                DELETE FROM payment
                """;
        jdbcTemplate.update(sql, Collections.emptyMap());
    }
}
