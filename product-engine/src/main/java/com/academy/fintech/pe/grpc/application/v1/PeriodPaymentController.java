package com.academy.fintech.pe.grpc.application.v1;

import com.academy.fintech.pe.core.service.agreement.util.AgreementCalculator;
import com.example.payment.PeriodPaymentRequest;
import com.example.payment.PeriodPaymentResponse;
import com.example.payment.PeriodPaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
public class PeriodPaymentController extends PeriodPaymentServiceGrpc.PeriodPaymentServiceImplBase {

    @Override
    public void getPeriodPayment(PeriodPaymentRequest request, StreamObserver<PeriodPaymentResponse> responseObserver) {
        int periodPayment = calculatePeriodPayment(request);
        PeriodPaymentResponse response = PeriodPaymentResponse.newBuilder()
            .setPeriodPayment(periodPayment)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private static int calculatePeriodPayment(PeriodPaymentRequest request) {
        BigDecimal periodPayment = AgreementCalculator.calculatePMT(BigDecimal.valueOf(request.getDisbursementAmount() +
                        request.getOriginationAmount()), BigDecimal.valueOf(request.getInterest()), request.getLoanTerm());
        return periodPayment.intValue();
    }
}
