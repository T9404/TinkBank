package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.exception.ApplicationAlreadyExists;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.academy.fintech.origination.core.service.application.mapper.ClientMapper.mapToClient;

@Service
@RequiredArgsConstructor
public class CreationApplicationService {
    private final ApplicationService applicationService;
    private final ClientService clientService;

    @Transactional
    public String createApplication(ApplicationRequest request) {
        Client client = saveClient(request);
        Application application = buildApplication(request, client);
        try {
            handleDuplicates(client, application);
            var createdApplication = applicationService.saveApplication(application);
            return createdApplication.getId();
        } catch (StatusRuntimeException exception) {
            Application existingApplication = applicationService.findApplication(client, application.getStatus(),
                    application.getRequestedDisbursementAmount()).orElseThrow();
            return exception.getTrailers().get(Metadata.Key.of(existingApplication.getId(), Metadata.ASCII_STRING_MARSHALLER));
        }
    }

    private Client saveClient(ApplicationRequest request) {
        Client potentialClient = mapToClient(request);
        return clientService.saveClient(potentialClient);
    }

    private Application buildApplication(ApplicationRequest request, Client client) {
        return Application.builder()
                .status(ApplicationStatus.NEW.getStatus())
                .client(client)
                .requestedDisbursementAmount(request.getDisbursementAmount())
                .build();
    }

    private void handleDuplicates(Client client, Application application) {
        if (applicationService.isApplicationExists(client, application.getStatus(),
                application.getRequestedDisbursementAmount())) {
            throw new ApplicationAlreadyExists();
        }
    }
}
