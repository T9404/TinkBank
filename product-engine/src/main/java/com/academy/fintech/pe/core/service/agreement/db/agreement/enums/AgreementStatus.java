package com.academy.fintech.pe.core.service.agreement.db.agreement.enums;

import lombok.Getter;

@Getter
public enum AgreementStatus {
    NEW("NEW"),
    ACTIVE("ACTIVE"),
    CLOSE("CLOSED");

    private final String status;

    AgreementStatus(String status) {
        this.status = status;
    }

}
