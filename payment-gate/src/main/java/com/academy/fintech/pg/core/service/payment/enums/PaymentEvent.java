package com.academy.fintech.pg.core.service.payment.enums;

import com.academy.fintech.pg.core.common.EventInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.slf4j.event.Level;

@Getter
@AllArgsConstructor
public enum PaymentEvent implements EventInfo {
    DISBURSEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, Level.INFO),
    DISBURSEMENT_INVALID_STATUS(HttpStatus.BAD_REQUEST, Level.INFO),
    DISBURSEMENT_INVALID_AMOUNT(HttpStatus.BAD_REQUEST, Level.INFO),
    INVALID_RECEIVER_ACCOUNT(HttpStatus.BAD_REQUEST, Level.INFO);

    private final HttpStatus status;
    private final Level level;
}
