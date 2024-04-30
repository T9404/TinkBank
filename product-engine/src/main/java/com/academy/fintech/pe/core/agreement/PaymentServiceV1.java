package com.academy.fintech.pe.core.agreement;

import com.academy.fintech.pe.core.agreement.db.payment_schedule.ScheduleService;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.converter.ProtobufConverter;
import com.academy.fintech.pe.core.service.balance.BalanceServiceV1;
import com.example.payment.ClientPaymentRequest;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.academy.fintech.pe.core.conveter.ProtobufConverter.toBigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceV1 {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceV1.class);
    private final ScheduleService scheduleService;
    private final BalanceServiceV1 balanceService;

    public List<Timestamp> getLatePayment(String agreementId) {
        logger.info("Getting late payments for agreementId: {}", agreementId);

        List<PaymentSchedulePayment> payments = scheduleService.findAllLatePayment(agreementId);

        List<Timestamp> latePayments = payments.stream()
                .map(payment -> ProtobufConverter.toGoogleTimestampUTC(payment.getPaymentDate()))
                .toList();

        logger.debug("Late payments for agreementId {}: {}", agreementId, latePayments);
        return latePayments;
    }

    public void addPayment(ClientPaymentRequest request) {
        var amount = toBigDecimal(request.getAmount());
        balanceService.addPayment(request.getApplicationId(), amount);
    }
}
