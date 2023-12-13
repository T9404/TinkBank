package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
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
}
