package com.academy.fintech.pe.unit.service;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static com.academy.fintech.pe.core.converter.TimestampConverter.fromGoogleTimestampToLocalDateTime;

public class TimestampConverterTest {

    @Test
    public void testFromGoogleTimestampUTC() {
        com.google.protobuf.Timestamp googleTimestamp = com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(1635724800)
                .setNanos(0)
                .build();
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2021, 11, 1, 0, 0, 0);
        LocalDateTime actualLocalDateTime = fromGoogleTimestampToLocalDateTime(googleTimestamp);
        assert expectedLocalDateTime.equals(actualLocalDateTime);
    }
}
