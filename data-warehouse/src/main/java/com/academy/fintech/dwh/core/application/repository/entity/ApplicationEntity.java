package com.academy.fintech.dwh.core.application.repository.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationEntity(
        String dataApplicationId,
        String applicationId,
        String status,
        LocalDate businessDate
) {
}
