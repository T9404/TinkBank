package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentService;
import com.academy.fintech.pe.core.service.agreement.util.ProtobufConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.DisbursementRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ScheduleCreationService {
    private PaymentScheduleService paymentScheduleService;
    private PaymentSchedulePaymentService paymentSchedulePaymentService;
    private AgreementService agreementService;

    @Autowired
    public void setPaymentScheduleService(PaymentScheduleService paymentScheduleService) {
        this.paymentScheduleService = paymentScheduleService;
    }

    @Autowired
    public void setPaymentSchedulePaymentService(PaymentSchedulePaymentService paymentSchedulePaymentService) {
        this.paymentSchedulePaymentService = paymentSchedulePaymentService;
    }

    @Autowired
    public void setAgreementService(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    public void generateInitialPaymentGraphic(DisbursementRequest request) {
        LocalDateTime disbursementLocalDate = ProtobufConverter.fromGoogleTimestampToLocalDateTime(request.getPaymentDate());
        Timestamp disbursementTimestamp = Timestamp.valueOf(disbursementLocalDate);
        UUID agreementNumber = UUID.fromString(request.getAgreementNumber());

        Agreement agreement = agreementService.getAgreementByAgreementNumber(agreementNumber);
        PaymentSchedule paymentSchedule = paymentScheduleService.savePaymentSchedule(agreement);
        paymentSchedulePaymentService.savePaymentSchedulePayment(agreement, disbursementTimestamp, paymentSchedule);
    }
}
