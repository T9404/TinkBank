package com.academy.fintech.pg.core.merchantProvider.client.enums;

import com.academy.fintech.pg.core.common.EventInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MerchantProviderEvent implements EventInfo {
    MERCHANT_NOT_FOUND(HttpStatus.NOT_FOUND, Level.INFO),
    MERCHANT_INVALID_REQUEST(HttpStatus.BAD_REQUEST, Level.INFO),
    MERCHANT_MAPPING_ERROR(HttpStatus.BAD_REQUEST, Level.WARN),
    MERCHANT_PROCESSING_TIMEOUT_ERROR(HttpStatus.BAD_REQUEST, Level.WARN);

    private final HttpStatus status;
    private final Level level;
}
