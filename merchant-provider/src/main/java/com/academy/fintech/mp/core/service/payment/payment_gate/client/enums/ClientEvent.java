package com.academy.fintech.mp.core.service.payment.payment_gate.client.enums;

import com.academy.fintech.mp.core.common.EventInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClientEvent implements EventInfo {
    CLIENT_ERROR_EVENT(HttpStatus.BAD_REQUEST, Level.INFO);

    private final HttpStatus status;
    private final Level level;
}
