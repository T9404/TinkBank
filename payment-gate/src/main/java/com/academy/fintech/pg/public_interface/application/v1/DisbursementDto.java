package com.academy.fintech.pg.public_interface.application.v1;

import java.time.LocalDateTime;

public record DisbursementDto(
        String agreementNumber,
        LocalDateTime paymentDate
) {
}
