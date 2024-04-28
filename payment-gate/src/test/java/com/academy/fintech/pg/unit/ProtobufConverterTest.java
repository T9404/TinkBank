package com.academy.fintech.pg.unit;

import com.academy.fintech.pg.core.conveter.ProtobufConverter;
import com.example.payment.DecimalValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtobufConverterTest {

    @Test
    public void testConvertLocalDateTimeToGoogleTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        Timestamp timestamp = ProtobufConverter.convertLocalDateTimeToGoogleTimestamp(localDateTime);

        assertEquals(localDateTime.toEpochSecond(ZoneOffset.UTC), timestamp.getSeconds());
        assertEquals(localDateTime.getNano(), timestamp.getNanos());
    }

    @Test
    public void testConvertOffsetDateTimeToGoogleTimestamp() {
        OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);
        Timestamp timestamp = ProtobufConverter.convertOffsetDateTimeToGoogleTimestamp(offsetDateTime);

        assertEquals(offsetDateTime.toEpochSecond(), timestamp.getSeconds());
        assertEquals(offsetDateTime.getNano(), timestamp.getNanos());
    }

    @Test
    public void testToBigDecimal() {
        BigInteger unscaledValue = BigInteger.valueOf(123456789);

        int scale = 2;
        int precision = unscaledValue.toString().length();
        com.example.disbursement.DecimalValue decimalValue = com.example.disbursement.DecimalValue.newBuilder()
                .setValue(ByteString.copyFrom(unscaledValue.toByteArray()))
                .setScale(scale)
                .setPrecision(precision)
                .build();

        BigDecimal result = ProtobufConverter.toBigDecimal(decimalValue);

        BigDecimal expected = new BigDecimal(unscaledValue, scale, new MathContext(precision));
        assertEquals(expected, result);
    }

    @Test
    public void testToDecimalValue() {
        BigDecimal value = new BigDecimal("123456789.01");

        com.example.payment.DecimalValue result = ProtobufConverter.toProtobufDecimalValue(value);

        BigInteger unscaled = value.unscaledValue();
        int scale = value.scale();
        int precision = unscaled.toString().length();

        com.example.payment.DecimalValue expected = DecimalValue.newBuilder()
                .setValue(ByteString.copyFrom(unscaled.toByteArray()))
                .setScale(scale)
                .setPrecision(precision)
                .build();

        assertEquals(expected, result);
    }
}
