package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.users.UserService;
import com.academy.fintech.origination.core.service.application.db.users.Users;
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
    private final UserService userService;

    @Transactional
    public String createApplication(ApplicationRequest request) {
        Users users = saveClient(request);
        Application application = buildApplication(request, users);
        try {
            handleDuplicates(users, application);
            var createdApplication = applicationService.saveApplication(application);
            return createdApplication.getId();
        } catch (StatusRuntimeException exception) {
            Application existingApplication = applicationService.findApplication(users, application.getStatus(),
                    application.getRequestedDisbursementAmount()).orElseThrow();
            return exception.getTrailers().get(Metadata.Key.of(existingApplication.getId(), Metadata.ASCII_STRING_MARSHALLER));
        }
    }

    private Users saveClient(ApplicationRequest request) {
        Users potentialUser = mapToClient(request);
        return userService.saveClient(potentialUser);
    }

    private Application buildApplication(ApplicationRequest request, Users user) {
        return Application.builder()
                .status(ApplicationStatus.NEW.getStatus())
                .users(user)
                .requestedDisbursementAmount(request.getDisbursementAmount())
                .build();
    }

    private void handleDuplicates(Users user, Application application) {
        if (applicationService.isApplicationExists(
                user, application.getStatus(), application.getRequestedDisbursementAmount())) {
            throw new ApplicationAlreadyExists();
        }
    }
}
