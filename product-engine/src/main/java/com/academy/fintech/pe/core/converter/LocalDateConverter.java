package com.academy.fintech.pe.core.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public final class LocalDateConverter {

    private LocalDateConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static LocalDate convertToLocalDate(long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }
}
