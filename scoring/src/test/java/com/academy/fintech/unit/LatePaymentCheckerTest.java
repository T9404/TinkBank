package com.academy.fintech.unit;

import com.academy.fintech.core.service.approval.util.LatePaymentChecker;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.academy.fintech.core.service.approval.util.ProtobufConverter.toGoogleTimestampUTC;

public class LatePaymentCheckerTest {

    @Test
    public void testCheckLatePayment() {
        Timestamp timestamp = toGoogleTimestampUTC(LocalDateTime.now().minusDays(10));
        int daysThreshold = 7;
        boolean expected = true;
        boolean actual = LatePaymentChecker.isLargeLatePayment(timestamp, daysThreshold);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCheckSmallLatePayment() {
        Timestamp timestamp = toGoogleTimestampUTC(LocalDateTime.now().minusDays(5));
        int daysThreshold = 7;
        boolean expected = true;
        boolean actual = LatePaymentChecker.isSmallLatePayment(timestamp, daysThreshold);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCheckNotLatePayment() {
        Timestamp timestamp = toGoogleTimestampUTC(LocalDateTime.now().minusDays(5));
        int daysThreshold = 3;
        boolean expected = true;
        boolean actual = LatePaymentChecker.isLargeLatePayment(timestamp, daysThreshold);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCheckNotSmallLatePayment() {
        Timestamp timestamp = toGoogleTimestampUTC(LocalDateTime.now().minusDays(10));
        int daysThreshold = 3;
        boolean expected = false;
        boolean actual = LatePaymentChecker.isSmallLatePayment(timestamp, daysThreshold);
        Assertions.assertEquals(expected, actual);
    }
}
