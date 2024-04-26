package com.academy.fintech.dwh.core.application.data.repository.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationDataEntity(
        String id,
        String content,
        LocalDate businessDate
) {}
