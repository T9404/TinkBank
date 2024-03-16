package com.academy.fintech.pg.public_interface.error.v1;

import java.time.OffsetDateTime;

public record ErrorResponseDto(
        OffsetDateTime time,
        String message,
        int code
) {
}
