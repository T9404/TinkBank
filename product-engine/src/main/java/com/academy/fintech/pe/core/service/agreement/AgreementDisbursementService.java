package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.ScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.validation.AgreementDisbursementValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.DisbursementRequest;

@Service
@RequiredArgsConstructor
public class AgreementDisbursementService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementDisbursementService.class);
    private final AgreementService agreementService;
    private final ScheduleCreationService scheduleCreationService;

    public String acceptDisbursementProcess(DisbursementRequest request) {
        AgreementDisbursementValidator.checkDisbursementValidity(request);
        agreementService.activateAgreement(request);
        scheduleCreationService.generateInitialPaymentGraphic(request);
        logger.info("Disbursement process accepted for agreement: {}", request.getAgreementNumber());
        return "Agreement is activated successfully";
    }
}
