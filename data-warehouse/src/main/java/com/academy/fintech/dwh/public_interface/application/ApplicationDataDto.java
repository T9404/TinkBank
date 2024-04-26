package com.academy.fintech.dwh.public_interface.application;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationDataDto(
        String id,
        String content,
        LocalDate businessDate
) {}
