package com.academy.fintech.origination.core.application.exporter.dto;

import lombok.Builder;

@Builder
public record ApplicationExportDto(
        String dataApplicationId,
        String applicationId,
        String status,
        long createdAt
) {
}
