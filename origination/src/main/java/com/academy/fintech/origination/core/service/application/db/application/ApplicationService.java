package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
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

    public boolean isApplicationExists(Client client, String status, int requestedDisbursementAmount) {
        return applicationRepository.existsByClientAndStatusAndRequestedDisbursementAmount(client, status, requestedDisbursementAmount);
    }

    public Optional<Application> findApplication(Client client, String status, int requestedDisbursementAmount) {
        return applicationRepository.findByClientAndStatusAndRequestedDisbursementAmount(client, status, requestedDisbursementAmount);
    }

    public Iterable<Application> findApplicationsByStatus(String status) {
        return applicationRepository.findAllByStatus(status);
    }
}
