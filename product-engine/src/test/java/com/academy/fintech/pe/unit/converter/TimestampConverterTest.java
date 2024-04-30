package com.academy.fintech.pe.unit.converter;

import com.example.payment.DecimalValue;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static com.academy.fintech.pe.core.converter.BigDecimalConverter.toBigDecimal;
import static com.academy.fintech.pe.core.converter.BigDecimalConverter.toDecimalValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimestampConverterTest {

    @Test
    public void testToBigDecimal() {
        BigInteger unscaledValue = BigInteger.valueOf(123456789);

        int scale = 2;
        int precision = unscaledValue.toString().length();
        DecimalValue decimalValue = DecimalValue.newBuilder()
                .setValue(ByteString.copyFrom(unscaledValue.toByteArray()))
                .setScale(scale)
                .setPrecision(precision)
                .build();

        BigDecimal result = toBigDecimal(decimalValue);

        BigDecimal expected = new BigDecimal(unscaledValue, scale, new MathContext(precision));
        assertEquals(expected, result);
    }

    @Test
    public void testToDecimalValue() {
        BigDecimal value = new BigDecimal("123456789.01");

        DecimalValue result = toDecimalValue(value);

        BigInteger unscaled = value.unscaledValue();
        int scale = value.scale();
        int precision = unscaled.toString().length();

        DecimalValue expected = DecimalValue.newBuilder()
                .setValue(ByteString.copyFrom(unscaled.toByteArray()))
                .setScale(scale)
                .setPrecision(precision)
                .build();

        assertEquals(expected, result);
    }

}
