package com.academy.fintech.pe.unit.service;

import com.academy.fintech.pe.core.converter.ProtobufConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ProtobufConverterTest {

    @Test
    public void testFromGoogleTimestampUTC() {
        com.google.protobuf.Timestamp googleTimestamp = com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(1635724800)
                .setNanos(0)
                .build();
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2021, 11, 1, 0, 0, 0);
        LocalDateTime actualLocalDateTime = ProtobufConverter.fromGoogleTimestampToLocalDateTime(googleTimestamp);
        assert expectedLocalDateTime.equals(actualLocalDateTime);
    }
}
