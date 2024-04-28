package com.academy.fintech.mp.unit;

import com.academy.fintech.mp.core.service.generator.RandomTimeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomTimeGeneratorTest {

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(RandomTimeGenerator.class, "MAX_WAITING_MINUTES", 60 * 24 * 7);
    }

    @Test
    public void testGetRandomTime() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime randomTime = RandomTimeGenerator.getRandomTime();

        assertTrue(randomTime.isAfter(now));
        assertTrue(randomTime.isBefore(now.plusDays(7)));
    }
}
