package com.academy.fintech.pg.public_interface.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DisbursementDto(String agreementNumber, LocalDateTime paymentDate) {
}
