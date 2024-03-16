package com.academy.fintech.pg.rest.payment.v1;

import com.academy.fintech.pg.core.merchantProvider.client.rest.dto.response.CreatePaymentResponse;
import com.academy.fintech.pg.core.service.payment.PaymentServiceV1;
import com.academy.fintech.pg.rest.payment.v1.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentServiceV1 paymentService;

    @PostMapping()
    public CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentService.sendClientPayment(request);
    }
}
