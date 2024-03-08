package com.academy.fintech.mp.core.service.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Random;

@Component
public final class RandomTimeGenerator {
    private static long MAX_WAITING_MINUTES;

    @Value("${max-waiting-seconds}")
    public void setMaxWaitingMinutes(int maxWaitingMinutes) {
        MAX_WAITING_MINUTES = maxWaitingMinutes;
    }

    /**
     * It needs to simulate the time when the payment will be processed.
     *
     * @return a random time between 1 second and 7 days from now
     */
    public static OffsetDateTime getRandomTime() {
        var random = new Random();

        var randomMinutes = random.nextLong(MAX_WAITING_MINUTES) + 1;

        return OffsetDateTime.now().plusSeconds(randomMinutes);
    }
}
