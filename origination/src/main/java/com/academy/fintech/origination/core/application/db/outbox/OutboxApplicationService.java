package com.academy.fintech.origination.core.application.db.outbox;

import com.academy.fintech.origination.core.application.db.application.Application;
import com.academy.fintech.origination.core.application.db.outbox.enums.OutboxApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxApplicationService {
    private final OutboxApplicationRepository outboxApplicationRepository;

    public void save(Application application) {
        var outboxApplication = OutboxApplication.builder()
                .application(application)
                .status(OutboxApplicationStatus.NEW)
                .updatedAt(OffsetDateTime.now())
                .build();

        outboxApplicationRepository.save(outboxApplication);
    }

    public List<OutboxApplication> findAllByStatus(OutboxApplicationStatus status) {
        return outboxApplicationRepository.findAllByStatus(status.name());
    }

    public List<OutboxApplication> setAllPending(OutboxApplicationStatus status) {
        return outboxApplicationRepository.setAllPending(status.name());
    }

    public void updateStatus(OutboxApplication outboxApplication, OutboxApplicationStatus status) {
        outboxApplication.setStatus(status);
        outboxApplicationRepository.saveAndFlush(outboxApplication);
    }
}
