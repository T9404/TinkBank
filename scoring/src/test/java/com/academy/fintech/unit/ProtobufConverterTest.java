package com.academy.fintech.unit;

import com.academy.fintech.core.service.approval.util.ProtobufConverter;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ProtobufConverterTest {

    @Test
    public void testToGoogleTimestampUTC() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = ProtobufConverter.toGoogleTimestampUTC(localDateTime);

        Timestamp expectedTimestamp = Timestamp.newBuilder()
                .setSeconds(localDateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(localDateTime.getNano())
                .build();

        Assertions.assertEquals(timestamp, expectedTimestamp);
    }

    @Test
    public void testFromGoogleTimestampToLocalDateTime() {
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(1631123456)
                .setNanos(123456789)
                .build();

        LocalDateTime localDateTime = ProtobufConverter.fromGoogleTimestampToLocalDateTime(timestamp);
        LocalDateTime expectedLocalDateTime = LocalDateTime.ofEpochSecond(1631123456, 123456789, ZoneOffset.UTC);

        Assertions.assertEquals(localDateTime, expectedLocalDateTime);
    }
}
