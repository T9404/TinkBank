package com.academy.fintech.origination.core.application;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.application.exception.ApplicationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancellingApplicationService {
    private final ApplicationService applicationService;

    @Transactional
    public void cancelApplication(String applicationId) {
        Application application = applicationService.findById(applicationId).orElseThrow(ApplicationNotFoundException::new);
        application.setStatus(ApplicationStatus.CLOSED.name());
        applicationService.saveApplication(application);
    }
}
