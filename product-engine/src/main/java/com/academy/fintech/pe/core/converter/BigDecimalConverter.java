package com.academy.fintech.pe.core.converter;

import com.example.payment.DecimalValue;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class BigDecimalConverter {

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
    public static DecimalValue toDecimalValue(BigDecimal value) {
        var unscaled = value.unscaledValue();
        var scale = value.scale();
        return DecimalValue.newBuilder()
                .setValue(ByteString.copyFrom(unscaled.toByteArray()))
                .setScale(scale)
                .setPrecision(unscaled.toString().length())
                .build();
    }
}
