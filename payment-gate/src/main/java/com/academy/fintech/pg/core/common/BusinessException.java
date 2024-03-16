package com.academy.fintech.pg.core.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final EventInfo eventInfo;

    public BusinessException(EventInfo eventInfo, String message) {
        super(message);
        this.eventInfo = eventInfo;
    }
}
