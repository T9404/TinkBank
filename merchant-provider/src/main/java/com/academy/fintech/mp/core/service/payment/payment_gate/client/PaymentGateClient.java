package com.academy.fintech.mp.core.service.payment.payment_gate.client;

import com.academy.fintech.mp.core.common.BusinessException;
import com.academy.fintech.mp.core.service.payment.payment_gate.client.enums.ClientEvent;
import com.academy.fintech.mp.public_interface.payment.v1.dto.AddPaymentResponse;
import com.academy.fintech.mp.rest.payment.v1.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PaymentGateClient {
    private final PaymentGateProperties paymentGateProperties;
    private final WebClient webClient;

    public Mono<AddPaymentResponse> sendPaymentInfo(CreatePaymentRequest request) {
        return webClient.post()
                .uri(buildUri("/api/v1/payments"))
                .bodyValue(request)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(), clientResponse ->
                        Mono.error(new BusinessException(ClientEvent.CLIENT_ERROR_EVENT, "Error during payment sending to payment gate")))
                .bodyToMono(AddPaymentResponse.class);
    }

    private String buildUri(String path) {
        var scheme = paymentGateProperties.getScheme();
        var host = paymentGateProperties.getHost();
        var port = paymentGateProperties.getPort();
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path)
                .build()
                .toUriString();
    }
}
