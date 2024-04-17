package com.academy.fintech.pg.core.merchantProvider.client.rest;

import com.academy.fintech.pg.core.common.BusinessException;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request.CreatePaymentRequest;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response.CreatePaymentResponse;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.request.GetPaymentRequest;
import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response.GetPaymentResponse;
import com.academy.fintech.pg.core.merchantProvider.client.enums.MerchantProviderEvent;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MerchantProviderClient {
    private final MerchantProviderProperties merchantProviderProperties;
    private final WebClient webClient;

    public Mono<CreatePaymentResponse> createPayment(CreatePaymentRequest request) {
        return webClient.post()
                .uri(buildUri("/api/v1/payments"))
                .bodyValue(request)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isSameCodeAs(HttpStatus.BAD_REQUEST), clientResponse ->
                        Mono.error(new BusinessException(MerchantProviderEvent.MERCHANT_INVALID_REQUEST,
                                "Invalid create payment request to merchant provider")))
                .bodyToMono(CreatePaymentResponse.class);

    }
    
    public Mono<GetPaymentResponse> getPaymentStatus(GetPaymentRequest request) {
        return webClient.get()
                .uri(buildUri("/api/v1/payments/status/" + request.paymentId()))
                .retrieve()
                .onStatus(httpStatus ->httpStatus.isSameCodeAs(HttpStatus.NOT_FOUND), clientResponse ->
                        Mono.error(new BusinessException(MerchantProviderEvent.MERCHANT_NOT_FOUND,
                                "Payment not found in merchant provider")))
                .bodyToMono(GetPaymentResponse.class);
    }

    private String buildUri(String path) {
        var scheme = merchantProviderProperties.getScheme();
        var host = merchantProviderProperties.getHost();
        var port = merchantProviderProperties.getPort();
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path)
                .build()
                .toUriString();
    }
}
