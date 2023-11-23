package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentService;
import com.academy.fintech.pe.core.service.agreement.util.ProtobufConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.DisbursementRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleCreationService {
    private final PaymentScheduleService paymentScheduleService;
    private final PaymentSchedulePaymentService paymentSchedulePaymentService;
    private final AgreementService agreementService;

    public void generateInitialPaymentGraphic(DisbursementRequest request) {
        LocalDateTime disbursementLocalDate = ProtobufConverter.fromGoogleTimestampToLocalDateTime(request.getPaymentDate());
        Timestamp disbursementTimestamp = Timestamp.valueOf(disbursementLocalDate);

        Agreement agreement = agreementService.getAgreementByAgreementNumber(request.getAgreementNumber());
        PaymentSchedule paymentSchedule = paymentScheduleService.savePaymentSchedule(agreement);
        paymentSchedulePaymentService.savePaymentSchedulePayment(agreement, disbursementTimestamp, paymentSchedule);
    }
}
