package com.academy.fintech.mp.core.service.payment.payment_gate.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment-gate")
public class PaymentGateProperties {
    private String scheme;
    private String host;
    private int port;
}
