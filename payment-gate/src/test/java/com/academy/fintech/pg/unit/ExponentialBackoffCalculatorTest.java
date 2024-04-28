package com.academy.fintech.pg.unit;

import com.academy.fintech.pg.core.calculation.ExponentialBackoffCalculator;
import com.academy.fintech.pg.core.common.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExponentialBackoffCalculatorTest {

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(ExponentialBackoffCalculator.class, "BASE_BACKOFF_TIME_MINUTES", 10);
        ReflectionTestUtils.setField(ExponentialBackoffCalculator.class, "MAX_BACKOFF_ATTEMPTS", 5);
    }

    @Test
    public void testCalculateBackoffTime() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        OffsetDateTime backoffTime = ExponentialBackoffCalculator.calculateBackoffTime(now, 2);
        assertEquals(now.plusMinutes(40), backoffTime);

        backoffTime = ExponentialBackoffCalculator.calculateBackoffTime(now, 5);
        assertEquals(now.plusMinutes(320), backoffTime);
    }

    @Test
    public void testCalculateBackoffTimeMaxAttempts() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        assertThrows(BusinessException.class, () -> ExponentialBackoffCalculator.calculateBackoffTime(now, 6));
    }
}
