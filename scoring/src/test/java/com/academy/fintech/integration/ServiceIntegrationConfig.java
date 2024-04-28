package com.academy.fintech.integration;

import com.academy.fintech.ScoringApplication;
import com.academy.fintech.core.service.approval.EstimateService;
import com.academy.fintech.core.service.approval.product.engine.client.LatePaymentService;
import com.academy.fintech.core.service.approval.product.engine.client.PeriodPaymentService;
import com.example.payment.LatePaymentRequest;
import com.example.payment.PeriodPaymentRequest;
import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.academy.fintech.core.service.approval.util.ProtobufConverter.toGoogleTimestampUTC;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class,
        GrpcServerFactoryAutoConfiguration.class,
        GrpcClientAutoConfiguration.class,
        ScoringApplication.class})
public class ServiceIntegrationConfig {

    @Value("${late.payment.threshold.days}")
    private int daysThreshold;

    @Bean
    public EstimateService estimateService() {
        return new EstimateService(periodPaymentService(), latePaymentService());
    }

    @Bean
    public PeriodPaymentService periodPaymentService() {
        PeriodPaymentService periodPaymentServiceMock = mock(PeriodPaymentService.class);

        // Test with large regular payment
        setupPeriodPaymentMock(periodPaymentServiceMock, createPeriodPaymentUser("e58ed763-928c-4155-bee9-fdbaaadc15f3"));

        // Test with large regular payment and large late payment
        setupPeriodPaymentMock(periodPaymentServiceMock, createPeriodPaymentUser("e58ed763-928c-4155-bee9-fdbaaadc15f7"));

        // Test with small regular payment and large late payment
        setupPeriodPaymentMock(periodPaymentServiceMock, createPeriodPaymentUser("e58ed763-928c-4155-bee9-fdbaaadc1510"));

        return periodPaymentServiceMock;
    }

    @Bean
    public LatePaymentService latePaymentService() {
        LatePaymentService latePaymentServiceMock = mock(LatePaymentService.class);

        // Test with large late payment
        setupLatePaymentMock(latePaymentServiceMock, createLatePaymentUser("b58ed763-928c-4155-bee9-fdbaaadc15c2"));

        // Test with large late payment and large regular payment
        setupLatePaymentMock(latePaymentServiceMock, createLatePaymentUser("e58ed763-928c-4155-bee9-fdbaaadc15f7"));

        // Test with small late payment
        setupLatePaymentMock(latePaymentServiceMock, createLatePaymentUser("b58ed763-928c-4155-bee9-fdbaaadc15c3"),
                daysThreshold - 1);

        // Test with small late payment and large regular payment
        setupLatePaymentMock(latePaymentServiceMock, createLatePaymentUser("e58ed763-928c-4155-bee9-fdbaaadc1510"),
                daysThreshold - 1);

        return latePaymentServiceMock;
    }

    private void setupPeriodPaymentMock(PeriodPaymentService periodPaymentServiceMock, PeriodPaymentRequest request) {
        when(periodPaymentServiceMock.getPeriodPayment(eq(request)))
                .thenReturn(89293);
    }

    private void setupLatePaymentMock(LatePaymentService latePaymentServiceMock, LatePaymentRequest request) {
        List<Timestamp> fakeTimestamps = new ArrayList<>();
        fakeTimestamps.add(toGoogleTimestampUTC(LocalDateTime.of(2000, 1, 1, 0, 0)));
        when(latePaymentServiceMock.getLatePayment(eq(request)))
                .thenReturn(fakeTimestamps);
    }

    private void setupLatePaymentMock(LatePaymentService latePaymentServiceMock, LatePaymentRequest request, int daysBefore) {
        List<Timestamp> fakeTimestamps = new ArrayList<>();
        fakeTimestamps.add(toGoogleTimestampUTC(LocalDateTime.now().minusDays(daysBefore)));
        when(latePaymentServiceMock.getLatePayment(eq(request)))
                .thenReturn(fakeTimestamps);
    }

    private LatePaymentRequest createLatePaymentUser(String userId) {
        return LatePaymentRequest.newBuilder().setUserId(userId).build();
    }

    private PeriodPaymentRequest createPeriodPaymentUser(String userId) {
        return PeriodPaymentRequest.newBuilder()
                .setUserId(userId)
                .setLoanTerm(12)
                .setDisbursementAmount(1000000)
                .setOriginationAmount(5000)
                .setInterest(12)
                .setProductCode("CL")
                .setProductVersion("1.0")
                .build();
    }
}
