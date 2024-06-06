package com.academy.fintech.dwh.public_interface.application;

import lombok.Builder;

@Builder
public record ApplicationExportDto(
        String dataApplicationId,
        String applicationId,
        String status,
        long createdAt
) {
}
