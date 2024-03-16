package com.academy.fintech.pg.core.merchantProvider.client.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "merchant-provider")
public class MerchantProviderProperties {
    private String scheme;
    private String host;
    private int port;
}
