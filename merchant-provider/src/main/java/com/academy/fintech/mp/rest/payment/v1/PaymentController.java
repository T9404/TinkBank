package com.academy.fintech.mp.rest.payment.v1;

import com.academy.fintech.mp.core.service.payment.PaymentServiceV1;
import com.academy.fintech.mp.public_interface.payment.v1.dto.AddPaymentResponse;
import com.academy.fintech.mp.public_interface.payment.v1.dto.CreatePaymentResponse;
import com.academy.fintech.mp.public_interface.payment.v1.dto.GetPaymentResponse;
import com.academy.fintech.mp.rest.payment.v1.dto.CreatePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentServiceV1 paymentServiceV1;

    @GetMapping("/status/{id}")
    public GetPaymentResponse getPayments(@PathVariable("id") UUID paymentId) {
        return paymentServiceV1.getActualStatus(paymentId);
    }

    @PostMapping()
    public CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentServiceV1.save(request);
    }

    @PostMapping("/users")
    public Mono<AddPaymentResponse> addUserPayment(@RequestBody CreatePaymentRequest request) {
        return paymentServiceV1.addUserPayment(request);
    }
}
