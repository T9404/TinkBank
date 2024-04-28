package com.academy.fintech.origination.core.application.exporter;

import com.academy.fintech.origination.core.application.db.outbox.OutboxApplication;
import com.academy.fintech.origination.core.application.db.outbox.OutboxApplicationService;
import com.academy.fintech.origination.core.application.db.outbox.enums.OutboxApplicationStatus;
import com.academy.fintech.origination.core.application.exporter.exception.KafkaWaitingException;
import com.academy.fintech.origination.core.application.exporter.property.ApplicationExporterProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.academy.fintech.origination.core.application.mapper.OutboxApplicationMapper.mapToApplicationExportDto;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@RequiredArgsConstructor
public class ApplicationExporter {
    private final ApplicationExporterProperty applicationExporterProperty;
    private final OutboxApplicationService outboxApplicationService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${exporter.application.topic}")
    private String applicationTopicName;

    @Scheduled(fixedRateString = "${outbox.application.exporter.fixed-rate}")
    public void export() {
        var outboxApplications = outboxApplicationService.setAllPending(OutboxApplicationStatus.NEW);

        var futures = outboxApplications.stream()
                .map(this::trySendApplication)
                .toArray(CompletableFuture[]::new);

        waitSendingEnd(CompletableFuture.allOf(futures), outboxApplications.size());
    }

    @Scheduled(fixedRateString = "${outbox.application.stalled.fixed-rate}")
    public void processStalled() {
        var outboxApplications = outboxApplicationService.findAllByStatus(OutboxApplicationStatus.PENDING);
        outboxApplications.forEach(this::processStalled);
    }

    private CompletableFuture<Void> trySendApplication(OutboxApplication outboxApplication) {
        try {
            var applicationDto = mapToApplicationExportDto(outboxApplication);
            return kafkaTemplate.send(applicationTopicName, applicationDto.applicationId(), applicationDto)
                    .thenAccept(result -> outboxApplicationService.updateStatus(outboxApplication, OutboxApplicationStatus.SENT))
                    .exceptionally(exception -> {
                        outboxApplicationService.updateStatus(outboxApplication, OutboxApplicationStatus.ERROR);
                        return null;
                    });
        } catch (Exception e) {
            outboxApplicationService.updateStatus(outboxApplication, OutboxApplicationStatus.ERROR);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void waitSendingEnd(CompletableFuture<Void> commonCompletableFuture, int countSendingElements) {
        var kafkaSendTimeout = applicationExporterProperty.getKafkaSendTimeout();
        var kafkaTransformTimeout = applicationExporterProperty.getKafkaTransformTimeout();
        final long awaitTime = countSendingElements * kafkaSendTimeout + kafkaTransformTimeout;

        try {
            commonCompletableFuture.get(awaitTime, MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException exception) {
            throw new KafkaWaitingException("Failed to wait for Kafka sending", exception);
        }
    }

    private void processStalled(OutboxApplication outboxApplication) {
        var applicationTime = outboxApplication.getUpdatedAt();

        var pendingTimeout = applicationExporterProperty.getPendingTimeoutInSeconds();
        if (OffsetDateTime.now().isBefore(applicationTime.plusSeconds(pendingTimeout))) {
            return;
        }

        var maxAttempts = applicationExporterProperty.getRetryMaxAttempts();
        if (outboxApplication.getRetryCount() >= maxAttempts) {
            outboxApplicationService.updateStatus(outboxApplication, OutboxApplicationStatus.ERROR);
            return;
        }

        outboxApplication.setRetryCount(outboxApplication.getRetryCount() + 1);
        outboxApplicationService.updateStatus(outboxApplication, OutboxApplicationStatus.NEW);
    }
}
