package com.academy.fintech.pe.core.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePaymentService;
import com.academy.fintech.pe.core.converter.ProtobufConverter;
import com.academy.fintech.pe.core.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.PaymentSchedulePayment;
import com.academy.fintech.pe.core.agreement.db.payment_schedule.payment_schedule_payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proto.DisbursementRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
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

    @Transactional
    public List<PaymentSchedulePayment> findAllLatePayment(String userId) {
        List<Agreement> agreements = agreementService.findAllByUserId(userId);

        List<PaymentSchedule> paymentSchedules = agreements.stream()
                .flatMap(agreement -> paymentScheduleService.findAllByAgreement(agreement).stream())
                .toList();
        List<PaymentSchedulePayment> paymentSchedulePayments = paymentSchedules.stream()
                .flatMap(paymentSchedule -> paymentSchedulePaymentService.findAllByPaymentSchedule(paymentSchedule).stream())
                .toList();

        return paymentSchedulePayments.stream()
                .filter(paymentSchedulePayment -> paymentSchedulePayment.getStatus().equals(PaymentStatus.OVERDUE.name()))
                .toList();
    }
}
