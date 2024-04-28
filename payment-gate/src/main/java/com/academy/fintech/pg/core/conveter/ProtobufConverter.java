package com.academy.fintech.pg.core.conveter;

import com.example.disbursement.DecimalValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ProtobufConverter {

    /**
     * Converts a Google Timestamp to a LocalDateTime
     * More detailed info:
     * <a href="https://stackoverflow.com/questions/50733646/how-to-convert-localdatetime-to-com-google-protobuf-timestamp">Stackoverflow</a>
     */
    public static Timestamp convertLocalDateTimeToGoogleTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(localDateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(localDateTime.getNano())
                .build();
    }

    /**
     * Converts a Google Timestamp to a LocalDateTime
     * More detailed info:
     * <a href="https://stackoverflow.com/questions/63617857/convert-zoned-timestamp-to-com-google-protobuf-timestamp">Stackoverflow</a>
     */
    public static Timestamp convertOffsetDateTimeToGoogleTimestamp(final OffsetDateTime offsetDateTime) {
        long seconds = offsetDateTime.toEpochSecond();
        int nanos = offsetDateTime.getNano();
        return Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
    }

    /**
     * Converts a BigDecimal to a DecimalValue
     * More detailed info:
     * <a href="https://stackoverflow.com/questions/1051732/what-is-the-best-approach-for-serializing-bigdecimal-biginteger-to-protocolbuffe">Stackoverflow</a>
     */
    public static BigDecimal toBigDecimal(DecimalValue value) {
        var mc = new MathContext(value.getPrecision());
        return new BigDecimal(
                new BigInteger(value.getValue().toByteArray()),
                value.getScale(),
                mc);
    }

    /**
     * Converts a BigDecimal to a DecimalValue
     * More detailed info:
     * <a href="https://stackoverflow.com/questions/1051732/what-is-the-best-approach-for-serializing-bigdecimal-biginteger-to-protocolbuffe">Stackoverflow</a>
     */
    public static com.example.payment.DecimalValue toProtobufDecimalValue(BigDecimal value) {
        return com.example.payment.DecimalValue.newBuilder()
                .setScale(value.scale())
                .setPrecision(value.precision())
                .setValue(ByteString.copyFrom(value.unscaledValue().toByteArray()))
                .build();
    }
}
