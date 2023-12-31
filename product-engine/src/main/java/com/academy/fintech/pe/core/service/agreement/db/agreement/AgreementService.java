package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proto.DisbursementRequest;

import java.util.List;

import static com.academy.fintech.pe.core.service.agreement.util.ProtobufConverter.fromGoogleTimestampToLocalDateTime;

@Service
@RequiredArgsConstructor
public class AgreementService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
    private final AgreementRepository agreementRepository;

    public List<Agreement> findAllByUserId(String clientId) {
        return agreementRepository.findAllByClientId(clientId);
    }

    public void saveAgreement(Agreement agreement) {
        agreementRepository.save(agreement);
    }

    public Agreement getAgreementByAgreementNumber(String agreementNumber) {
        return agreementRepository.findById(agreementNumber).orElseThrow(AgreementNotFoundException::new);
    }

    @Transactional
    public void activateAgreement(DisbursementRequest request) {
        Agreement agreement = agreementRepository.findById(request.getAgreementNumber())
                .orElseThrow(AgreementNotFoundException::new);
        agreement.setStatus(AgreementStatus.ACTIVE.getStatus());
        agreement.setDisbursementDate(fromGoogleTimestampToLocalDateTime(request.getPaymentDate()));
        logger.info("Agreement activated: {}", agreement);
        agreementRepository.save(agreement);
    }
}
