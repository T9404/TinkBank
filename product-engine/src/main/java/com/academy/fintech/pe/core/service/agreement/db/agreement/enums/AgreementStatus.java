package com.academy.fintech.pe.core.service.agreement.db.agreement.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgreementStatus {
    NEW("NEW"),
    ACTIVE("ACTIVE"),
    CLOSE("CLOSED");

    private final String status;
}
