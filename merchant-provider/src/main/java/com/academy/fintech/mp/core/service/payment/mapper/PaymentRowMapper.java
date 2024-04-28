package com.academy.fintech.mp.core.service.payment.mapper;

import com.academy.fintech.mp.core.service.payment.db.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentRowMapper implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId((java.util.UUID) rs.getObject("payment_id"));
        payment.setSenderAccountNumber(rs.getString("sender_account_number"));
        payment.setReceiverAccountNumber(rs.getString("receiver_account_number"));
        payment.setCurrency(rs.getString("currency"));
        payment.setRequestedDisbursementAmount(rs.getBigDecimal("requested_disbursement_amount"));
        payment.setCompletionTime(rs.getObject("completion_time", java.time.OffsetDateTime.class));
        payment.setStatus(rs.getString("status"));
        return payment;
    }
}
