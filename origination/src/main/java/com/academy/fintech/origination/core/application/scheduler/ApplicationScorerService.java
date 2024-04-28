package com.academy.fintech.origination.core.application.scheduler;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.application.mapper.AcceptingScoringRequestMapper;
import com.academy.fintech.origination.core.application.scoring.client.AcceptingScoringService;
import com.academy.fintech.origination.core.mail.MailService;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationScorerService {
    private final AcceptingScoringService acceptingScoringService;
    private final ApplicationService applicationService;
    private final MailService mailService;

    @Value("${application.email.subject}")
    private String emailSubject;

    @Value("${application.email.message-template}")
    private String emailMessageTemplate;

    @Scheduled(fixedRate = 10000)
    public void checkScoring() {
        applicationService.findApplicationsByStatus(ApplicationStatus.NEW.getStatus())
                .forEach(this::processApplication);
    }

    @Transactional
    public void processApplication(Application application) {
        application.setStatus(ApplicationStatus.SCORING.getStatus());
        applicationService.saveApplication(application);

        try {
            int score = acceptingScoringService.acceptScoring(AcceptingScoringRequestMapper.mapToAcceptingScoringRequest(application));
            application.setStatus(score > 0 ? ApplicationStatus.ACCEPTED.getStatus() : ApplicationStatus.CLOSED.getStatus());
        } catch (StatusRuntimeException exception) {
            application.setStatus(ApplicationStatus.NEW.getStatus());
        }

        applicationService.saveApplication(application);
        sendApplicationStatusEmail(application);
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
