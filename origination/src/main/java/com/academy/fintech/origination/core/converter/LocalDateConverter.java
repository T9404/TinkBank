package com.academy.fintech.origination.core.converter;

import java.time.LocalDate;
import java.time.ZoneOffset;

public final class LocalDateConverter {

    private LocalDateConverter() {
        throw new UnsupportedOperationException();
    }

    public static long convertToLong(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

}
