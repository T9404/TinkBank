package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.scoring.client.AcceptingScoringService;
import com.example.payment.AcceptingScoringRequest;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.academy.fintech.origination.core.service.application.mapper.AcceptingScoringRequestMapper.mapToAcceptingScoringRequest;

@Service
@RequiredArgsConstructor
public class ApplicationScorerService {
    private final AcceptingScoringService acceptingScoringService;
    private final ApplicationService applicationService;
    private final MailService mailService;

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
            int score = acceptingScoringService.acceptScoring(mapToAcceptingScoringRequest(application));
            application.setStatus(score > 0 ? ApplicationStatus.ACCEPTED.getStatus() : ApplicationStatus.CLOSED.getStatus());
        } catch (StatusRuntimeException exception) {
            application.setStatus(ApplicationStatus.NEW.getStatus());
        }

        applicationService.saveApplication(application);
        sendApplicationStatusEmail(application);
    }

    private void sendApplicationStatusEmail(Application application) {
        String subject = "Status application in TinkBank";
        String recipientEmail = application.getClient().getEmail();
        String firstName = application.getClient().getFirstName();
        String applicationId = application.getId();
        String status = application.getStatus();
        String message = String.format("Hello, %s. Your application %s has %s status", firstName, applicationId, status);
        mailService.sendMail(recipientEmail, subject, message);
    }
}
