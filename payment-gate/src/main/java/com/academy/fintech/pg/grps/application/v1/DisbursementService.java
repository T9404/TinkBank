package com.academy.fintech.pg.grps.application.v1;

import com.academy.fintech.pg.public_interface.application.dto.DisbursementDto;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.DisbursementProcessGrpc;
import proto.DisbursementRequest;

import static com.academy.fintech.pg.grps.application.v1.util.ProtobufConverter.convertLocalDateTimeToGoogleTimestamp;

@Service
public class DisbursementService {

    @GrpcClient("grpc-server")
    private DisbursementProcessGrpc.DisbursementProcessBlockingStub disbursementStub;

    public String disburse(DisbursementDto request) {
        Timestamp timestamp = convertLocalDateTimeToGoogleTimestamp(request.paymentDate());
        DisbursementRequest disbursementRequest = DisbursementRequest.newBuilder()
                .setAgreementNumber(request.agreementNumber())
                .setPaymentDate(timestamp)
                .build();
        return disbursementStub.acceptDisbursementProcess(disbursementRequest).getMessage();
    }
}
