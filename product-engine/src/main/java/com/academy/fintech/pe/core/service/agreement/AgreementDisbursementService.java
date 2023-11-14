package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.ScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.validation.AgreementDisbursementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.DisbursementRequest;

@Service
public class AgreementDisbursementService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementDisbursementService.class);
    private AgreementService agreementService;
    private ScheduleCreationService scheduleCreationService;

    @Autowired
    public void setAgreementService(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @Autowired
    public void setScheduleCreationService(ScheduleCreationService scheduleCreationService) {
        this.scheduleCreationService = scheduleCreationService;
    }

    public String acceptDisbursementProcess(DisbursementRequest request) {
        AgreementDisbursementValidator.checkDisbursementValidity(request);
        agreementService.activateAgreement(request);
        scheduleCreationService.generateInitialPaymentGraphic(request);
        logger.info("Disbursement process accepted for agreement: {}", request.getAgreementNumber());
        return "Agreement is activated successfully";
    }
}
