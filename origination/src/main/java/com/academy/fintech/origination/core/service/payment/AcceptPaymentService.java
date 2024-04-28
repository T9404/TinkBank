package com.academy.fintech.origination.core.service.payment;

import com.academy.fintech.origination.core.service.payment.product_engine.client.UpdatingApplicationStatusService;
import com.example.payment.AcceptPaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import proto.DisbursementRequest;

@Service
@AllArgsConstructor
public class AcceptPaymentService {
    private final UpdatingApplicationStatusService updatingApplicationStatusService;

    public void acceptPayment(AcceptPaymentRequest request) {
        var updateApplicationStatusRequest = DisbursementRequest.newBuilder()
                .setAgreementNumber(request.getApplicationId())
                .setPaymentDate(request.getPaymentDate())
                .build();

        updatingApplicationStatusService.updateApplicationStatus(updateApplicationStatusRequest);
    }
}
