package com.academy.fintech.origination.core.application.scheduler;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.application.scoring.client.AcceptingScoringService;
import com.academy.fintech.origination.core.mail.MailService;
import com.academy.fintech.origination.core.service.agreement.CreateAgreementService;
import com.academy.fintech.origination.core.service.application.payment_gate.client.SendingDisbursementService;
import com.example.disbursement.SendDisbursementRequest;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.academy.fintech.origination.core.application.mapper.AcceptingScoringRequestMapper.mapToAcceptingScoringRequest;
import static com.academy.fintech.origination.core.converter.ProtobufConverter.toProtobufDecimalValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationScorerService {
    private final SendingDisbursementService sendingDisbursementService;
    private final CreateAgreementService createAgreementService;
    private final AcceptingScoringService acceptingScoringService;
    private final ApplicationService applicationService;
    private final MailService mailService;

    @Value("${application.email.subject}")
    private String emailSubject;

    @Value("${application.email.message-template}")
    private String emailMessageTemplate;

    @Scheduled(fixedRate = 1000)
    public void checkScoring() {
        applicationService.findApplicationsByStatus(ApplicationStatus.NEW.getStatus()).forEach(this::processApplication);
    }

    @Transactional
    public void processApplication(Application application) {
        application.setStatus(ApplicationStatus.SCORING.getStatus());
        applicationService.saveApplication(application);

        try {
            int score = acceptingScoringService.acceptScoring(mapToAcceptingScoringRequest(application));
            handleScore(application, score);
            applicationService.saveApplication(application);
        } catch (StatusRuntimeException exception) {
            application.setStatus(ApplicationStatus.NEW.getStatus());
        }

        applicationService.saveApplication(application);
        sendApplicationStatusEmail(application);
    }

    private void handleScore(Application application, int score) {
        if (score <= 0) {
            application.setStatus(ApplicationStatus.CLOSED.getStatus());
            return;
        }

        application.setStatus(ApplicationStatus.ACCEPTED.getStatus());

        createAgreement(application);

        sendDisbursement(application);
    }

    private void createAgreement(Application application) {
        log.info("Creating agreement for application: {}", application.getId());

        var agreementId = createAgreementService.createAgreement(application);
        application.setAgreementId(agreementId);

        applicationService.saveApplication(application);
        log.info("Agreement created: {}", agreementId);
    }

    private void sendDisbursement(Application application) {
        log.info("Sending disbursement for application: {}", application.getId());

        var sendDisbursementRequest = SendDisbursementRequest.newBuilder()
                .setApplicationId(application.getAgreementId())
                .setAmount(toProtobufDecimalValue(BigDecimal.valueOf(application.getRequestedDisbursementAmount())))
                .build();

        sendingDisbursementService.sendDisbursement(sendDisbursementRequest);
        log.info("Disbursement sent: {}", application.getAgreementId());
    }

    private void sendApplicationStatusEmail(Application application) {
        String recipientEmail = application.getUsers().getEmail();
        String firstName = application.getUsers().getFirstName();
        String applicationId = application.getId();
        String status = application.getStatus();
        String message = String.format(emailMessageTemplate, firstName, applicationId, status);
        // mailService.sendMail(recipientEmail, emailSubject, message);
    }
}
