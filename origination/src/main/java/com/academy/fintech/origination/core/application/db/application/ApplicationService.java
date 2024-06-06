package com.academy.fintech.origination.core.application.db.application;

import com.academy.fintech.origination.core.application.db.outbox.OutboxApplicationService;
import com.academy.fintech.origination.core.users.db.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final OutboxApplicationService outboxApplicationService;
    private final ApplicationRepository applicationRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Application saveApplication(Application application) {
        var savedApplication = applicationRepository.save(application);
        outboxApplicationService.save(savedApplication);
        return savedApplication;
    }

    public Optional<Application> findById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public boolean isApplicationExists(Users users, String status, int requestedDisbursementAmount) {
        return applicationRepository.existsByUsersAndStatusAndRequestedDisbursementAmount(users, status, requestedDisbursementAmount);
    }

    public Optional<Application> findApplication(Users users, String status, int requestedDisbursementAmount) {
        return applicationRepository.findByUsersAndStatusAndRequestedDisbursementAmount(users, status, requestedDisbursementAmount);
    }

    public Iterable<Application> findApplicationsByStatus(String status) {
        return applicationRepository.findAllByStatus(status);
    }
}
