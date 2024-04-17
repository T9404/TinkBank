package com.academy.fintech.origination.unit;

import com.academy.fintech.origination.core.converter.ProtobufConverter;
import com.example.disbursement.DecimalValue;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtobufConverterTest {

    @Test
    public void testToDecimalValue() {
        BigDecimal value = new BigDecimal("123456789.01");

        DecimalValue result = ProtobufConverter.toProtobufDecimalValue(value);

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
