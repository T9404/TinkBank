package com.academy.fintech.pg.core.merchantProvider.client;

import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.merchantProvider.client.rest.MerchantProviderClient;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request.CreatePaymentRequest;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request.GetPaymentRequest;
import com.academy.fintech.pg.core.merchantProvider.client.enums.MerchantProviderEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MerchantProviderService {
    private final MerchantProviderClient merchantProviderClient;

    public Mono<String> getPaymentStatus(UUID merchantId) {
        var request = new GetPaymentRequest(merchantId);

        return merchantProviderClient.getPaymentStatus(request)
                .map(response -> response.status())
                .onErrorMap(BusinessException.class, e -> new BusinessException(MerchantProviderEvent.MERCHANT_MAPPING_ERROR,
                        "Failed to get payment status for payment id " + merchantId + " in merchant provider"));

    }

    public Mono<UUID> createPayment(CreatePaymentRequest request) {
        return merchantProviderClient.createPayment(request)
                .map(response -> response.paymentId())
                .onErrorMap(BusinessException.class, e -> new BusinessException(MerchantProviderEvent.MERCHANT_MAPPING_ERROR,
                        "Failed to create payment in merchant provider for request " + request));
    }
}
