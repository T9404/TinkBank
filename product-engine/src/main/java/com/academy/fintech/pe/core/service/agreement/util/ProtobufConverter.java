package com.academy.fintech.pe.core.service.agreement.util;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class ProtobufConverter {

    public static LocalDateTime fromGoogleTimestampToLocalDateTime(final com.google.protobuf.Timestamp googleTimestamp) {
        return Instant.ofEpochSecond(googleTimestamp.getSeconds(), googleTimestamp.getNanos())
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    public static Timestamp toGoogleTimestampUTC(final LocalDateTime localDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(localDateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(localDateTime.getNano())
                .build();
    }
}
