package com.academy.fintech.pg.core.calculation;

import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.merchantProvider.client.enums.MerchantProviderEvent;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;

public final class ExponentialBackoffCalculator {
    private final static int MINUTES_IN_WEEK = 7 * 24 * 60;
    private static int BASE_BACKOFF_TIME_MINUTES;
    private static int MAX_BACKOFF_ATTEMPTS;

    @Value("${backoff.base-time-minutes}")
    public void setBaseBackoffTimeMinutes(int baseBackoffTimeMinutes) {
        BASE_BACKOFF_TIME_MINUTES = baseBackoffTimeMinutes;
    }

    @Value("${backoff.max-attempts}")
    public void setMaxBackoffAttempts(int maxBackoffAttempts) {
        MAX_BACKOFF_ATTEMPTS = maxBackoffAttempts;
    }

    /**
     * Calculate backoff time for a given attempt number
     * You can see more about the algorithm: <a href="https://en.wikipedia.org/wiki/Exponential_backoff">Exponential backoff</a>
     * @param offsetDateTime The time to calculate the backoff from
     * @param attemptNumber The current attempt number
     * @return The backoff time for the given attempt number (maximum 1 week from the given time)
     */
    public static OffsetDateTime calculateBackoffTime(OffsetDateTime offsetDateTime, int attemptNumber) {
        if (attemptNumber > MAX_BACKOFF_ATTEMPTS) {
            throw new BusinessException(MerchantProviderEvent.MERCHANT_PROCESSING_TIMEOUT_ERROR, "Max backoff attempts reached");
        }

        int backoffMinutes = (int) Math.pow(2, attemptNumber) * BASE_BACKOFF_TIME_MINUTES;
        backoffMinutes = Math.min(backoffMinutes, MINUTES_IN_WEEK);
        return offsetDateTime.plusMinutes(backoffMinutes);
    }

}
