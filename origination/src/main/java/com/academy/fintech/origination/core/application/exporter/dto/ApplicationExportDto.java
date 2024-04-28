package com.academy.fintech.origination.core.application.exporter.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationExportDto(
        String dataApplicationId,
        String applicationId,
        String status,
        LocalDate createdAt
) {
}
