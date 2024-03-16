package com.academy.fintech.mp.core.service.payment.enums;

import com.academy.fintech.mp.core.common.EventInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentEvent implements EventInfo {
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, Level.INFO),
    INVALID_CURRENCY(HttpStatus.BAD_REQUEST, Level.INFO),
    INVALID_DISBURSEMENT_AMOUNT(HttpStatus.BAD_REQUEST, Level.INFO);

    private final HttpStatus status;
    private final Level level;
}
