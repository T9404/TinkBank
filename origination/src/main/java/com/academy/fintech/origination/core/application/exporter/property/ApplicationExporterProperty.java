package com.academy.fintech.origination.core.application.exporter.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "outbox.application")
public class ApplicationExporterProperty {
    private int pendingTimeoutInSeconds;
    private int retryMaxAttempts;
    private int kafkaTransformTimeout;
    private long kafkaSendTimeout;
}
