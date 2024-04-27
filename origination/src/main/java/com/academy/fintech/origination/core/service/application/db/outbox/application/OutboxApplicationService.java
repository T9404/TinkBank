package com.academy.fintech.origination.core.service.application.db.outbox.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.outbox.application.enums.OutboxApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxApplicationService {
    private final OutboxApplicationRepository outboxApplicationRepository;

    public void save(Application application) {
        var outboxApplication = OutboxApplication.builder()
                .applicationId(application)
                .updatedAt(OffsetDateTime.now())
                .build();

        outboxApplicationRepository.save(outboxApplication);
    }

    public void delete(OutboxApplication outboxApplication) {
        outboxApplicationRepository.delete(outboxApplication);
    }

    public List<OutboxApplication> findAll() {
        return outboxApplicationRepository.findAll();
    }
}
