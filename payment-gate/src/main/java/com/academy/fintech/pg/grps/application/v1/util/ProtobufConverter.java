package com.academy.fintech.pg.grps.application.v1.util;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ProtobufConverter {

    public static Timestamp convertLocalDateTimeToGoogleTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(localDateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(localDateTime.getNano())
                .build();
    }
}
