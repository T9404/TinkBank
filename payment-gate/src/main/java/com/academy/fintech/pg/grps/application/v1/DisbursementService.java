package com.academy.fintech.pg.grps.application.v1;

import com.academy.fintech.pg.public_interface.application.v1.DisbursementDto;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.DisbursementProcessGrpc;
import proto.DisbursementRequest;

import static com.academy.fintech.pg.core.conveter.ProtobufConverter.convertLocalDateTimeToGoogleTimestamp;

@Service
public class DisbursementService {

    @GrpcClient("grpc-server")
    private DisbursementProcessGrpc.DisbursementProcessBlockingStub disbursementStub;

    public String disburse(DisbursementDto request) {
        var timestamp = convertLocalDateTimeToGoogleTimestamp(request.paymentDate());

        var disbursementRequest = getDisbursementRequest(request, timestamp);

        var response = disbursementStub.acceptDisbursementProcess(disbursementRequest);
        return response.getMessage();
    }

    private DisbursementRequest getDisbursementRequest(DisbursementDto request, Timestamp timestamp) {
        return DisbursementRequest.newBuilder()
                .setAgreementNumber(request.agreementNumber())
                .setPaymentDate(timestamp)
                .build();
    }
}
