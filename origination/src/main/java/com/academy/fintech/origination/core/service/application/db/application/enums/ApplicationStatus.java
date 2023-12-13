package com.academy.fintech.origination.core.service.application.db.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    NEW("NEW"),
    SCORING("SCORING"),
    ACCEPTED("ACCEPTED"),
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED");

    private final String status;
}
