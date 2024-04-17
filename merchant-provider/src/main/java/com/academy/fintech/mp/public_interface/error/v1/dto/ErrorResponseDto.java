package com.academy.fintech.mp.public_interface.error.v1.dto;

import java.time.OffsetDateTime;

public record ErrorResponseDto(
        OffsetDateTime time,
        String message,
        int code
) {
}

