package com.academy.fintech.origination.core.service.application.db.outbox.application.listener;

import com.academy.fintech.origination.core.service.application.db.outbox.application.OutboxApplication;
import com.academy.fintech.origination.core.service.application.db.outbox.application.OutboxApplicationService;
import com.academy.fintech.origination.core.service.application.db.outbox.application.kafka.OutboxApplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxApplicationListener {
    private final OutboxApplicationProducer outboxApplicationProducer;
    private final OutboxApplicationService outboxApplicationService;

    @Scheduled(fixedRateString = "${outbox.application.listener.fixed-rate}")
    public void listenOutboxApplication() {
        var outboxApplications = outboxApplicationService.findAll();

        outboxApplications.forEach(this::updateOutboxApplication);
    }

    private void updateOutboxApplication(OutboxApplication outboxApplication) {
        outboxApplicationProducer.send(outboxApplication);
        outboxApplicationService.delete(outboxApplication);
    }
}
