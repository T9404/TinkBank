package com.academy.fintech.core.service.approval.util;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;

import static com.academy.fintech.core.service.approval.util.ProtobufConverter.fromGoogleTimestampToLocalDateTime;

public final class LatePaymentChecker {

    /**
     * @return {@code true} if payment is late more than daysThreshold days
     */
    public static boolean isLargeLatePayment(Timestamp timestamp, int daysThreshold) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime paymentDate = fromGoogleTimestampToLocalDateTime(timestamp);
        return paymentDate.isBefore(now.minusDays(daysThreshold));
    }

    /**
     * @return {@code true} if payment is late less than daysThreshold days
     */
    public static boolean isSmallLatePayment(Timestamp timestamp, int daysThreshold) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime paymentDate = fromGoogleTimestampToLocalDateTime(timestamp);
        return paymentDate.isAfter(now.minusDays(daysThreshold).minusDays(7));
    }
}
