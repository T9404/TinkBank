package com.academy.fintech.origination.core.converter;

import com.example.disbursement.DecimalValue;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;

public final class ProtobufConverter {

    /**
     * Converts a BigDecimal to a DecimalValue
     * More detailed info:
     * <a href="https://stackoverflow.com/questions/1051732/what-is-the-best-approach-for-serializing-bigdecimal-biginteger-to-protocolbuffe">Stackoverflow</a>
     */
    public static DecimalValue toProtobufDecimalValue(BigDecimal value) {
        return DecimalValue.newBuilder()
                .setScale(value.scale())
                .setPrecision(value.precision())
                .setValue(ByteString.copyFrom(value.unscaledValue().toByteArray()))
                .build();
    }
}
