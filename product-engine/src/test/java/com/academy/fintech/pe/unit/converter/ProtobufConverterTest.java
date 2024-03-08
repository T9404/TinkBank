package com.academy.fintech.pe.unit.converter;

import com.academy.fintech.pe.core.conveter.ProtobufConverter;
import com.example.payment.DecimalValue;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtobufConverterTest {

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

        BigDecimal result = ProtobufConverter.toBigDecimal(decimalValue);

        BigDecimal expected = new BigDecimal(unscaledValue, scale, new MathContext(precision));
        assertEquals(expected, result);
    }

    @Test
    public void testToDecimalValue() {
        BigDecimal value = new BigDecimal("123456789.01");

        DecimalValue result = ProtobufConverter.toDecimalValue(value);

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
