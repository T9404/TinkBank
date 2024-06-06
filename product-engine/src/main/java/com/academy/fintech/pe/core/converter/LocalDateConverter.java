package com.academy.fintech.pe.core.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public final class LocalDateConverter {

    private LocalDateConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static long convertToLong(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static LocalDate convertMicrosecondsToLocalDate(long microseconds) {
        return Instant.ofEpochMilli(microseconds / 1000).atZone(ZoneOffset.UTC).toLocalDate();
    }
}
