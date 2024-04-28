package com.academy.fintech.pe.core.agreement;

import com.academy.fintech.pe.core.converter.ProtobufConverter;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.ScheduleService;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LatePaymentService {
    private static final Logger logger = LoggerFactory.getLogger(LatePaymentService.class);
    private final ScheduleService scheduleService;

    public List<Timestamp> getLatePayment(String agreementId) {
        logger.info("Getting late payments for agreementId: {}", agreementId);

        List<PaymentSchedulePayment> payments = scheduleService.findAllLatePayment(agreementId);

        List<Timestamp> latePayments = payments.stream()
                .map(payment -> ProtobufConverter.toGoogleTimestampUTC(payment.getPaymentDate()))
                .collect(Collectors.toList());

        logger.debug("Late payments for agreementId {}: {}", agreementId, latePayments);
        return latePayments;
    }
}
