package com.academy.fintech.origination.core.application.mapper;

import com.academy.fintech.origination.core.application.db.outbox.OutboxApplication;
import com.academy.fintech.origination.core.application.exporter.dto.ApplicationExportDto;

import java.time.LocalDate;
import java.util.UUID;

import static com.academy.fintech.origination.core.converter.LocalDateConverter.convertToLong;

public final class OutboxApplicationMapper {

    private OutboxApplicationMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ApplicationExportDto mapToApplicationExportDto(OutboxApplication outboxApplication) {
        var application = outboxApplication.getApplication();

        return ApplicationExportDto.builder()
                .dataApplicationId(UUID.randomUUID().toString())
                .applicationId(application.getId())
                .status(application.getStatus())
                .createdAt(convertToLong(LocalDate.now()))
                .build();
    }
}
