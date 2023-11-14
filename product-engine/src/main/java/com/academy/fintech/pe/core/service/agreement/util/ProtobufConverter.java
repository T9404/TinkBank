package com.academy.fintech.pe.core.service.agreement.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class ProtobufConverter {

    public static LocalDateTime fromGoogleTimestampToLocalDateTime(final com.google.protobuf.Timestamp googleTimestamp) {
        return Instant.ofEpochSecond(googleTimestamp.getSeconds(), googleTimestamp.getNanos())
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
