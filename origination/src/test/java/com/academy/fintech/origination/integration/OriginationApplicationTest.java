package com.academy.fintech.origination.integration;

import com.academy.fintech.application.*;
import com.academy.fintech.origination.OriginationApplication;
import com.academy.fintech.origination.core.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.application.db.outbox.OutboxApplicationRepository;
import com.academy.fintech.origination.integration.config.ServiceIntegrationConfig;
import com.academy.fintech.origination.integration.config.TestContainerConfig;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes= OriginationApplication.class)
@SpringJUnitConfig(classes = { ServiceIntegrationConfig.class, TestContainerConfig.class })
class OriginationApplicationTest {

    @GrpcClient("stocks")
    private ApplicationServiceGrpc.ApplicationServiceBlockingStub applicationServiceBlockingStub;

    @GrpcClient("stocks")
    private CancelApplicationServiceGrpc.CancelApplicationServiceBlockingStub cancelApplicationServiceBlockingStub;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OutboxApplicationRepository outboxApplicationRepository;

    @BeforeEach
    void setUp() {
        outboxApplicationRepository.deleteAll();
        applicationRepository.deleteAll();
    }

    @Test
    void testCreationApplication() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("John3213")
                .setLastName("Doe2321")
                .setEmail("john312@gmail.com")
                .setSalary(100000)
                .setDisbursementAmount(490000)
                .build();

        ApplicationResponse response = applicationServiceBlockingStub.create(request);
        assertAll(() -> UUID.fromString(response.getApplicationId()));
    }

    @Test
    void testCreationApplicationWithInvalidSalary() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("John3213")
                .setLastName("Doe2321")
                .setEmail("john312@gmail.com")
                .build();
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            applicationServiceBlockingStub.create(request);
        });
        assertAll(
                () -> assertEquals("INTERNAL: could not execute statement", exception.getMessage().substring(0, 37))
        );
    }

    @Test
    void testDuplicateApplication() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("John3213")
                .setLastName("Doe2321")
                .setEmail("john312@gmail.com")
                .setSalary(100000)
                .setDisbursementAmount(490000)
                .build();

        assertAll(() -> applicationServiceBlockingStub.create(request));

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            applicationServiceBlockingStub.create(request);
        });
        assertAll(
                () -> assertEquals("ALREADY_EXISTS: Application already exists", exception.getMessage())
        );
    }

    @Test
    void testCancelApplication() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("John3213")
                .setLastName("Doe2321")
                .setEmail("john312@gmail.com")
                .setSalary(100000)
                .setDisbursementAmount(490000)
                .build();

        String id =  applicationServiceBlockingStub.create(request).getApplicationId();

        CancelApplicationRequest response = CancelApplicationRequest.newBuilder()
                .setApplicationId(id)
                .build();
        CancelApplicationResponse cancelApplicationResponse = cancelApplicationServiceBlockingStub.cancel(response);
        assertAll(
                () -> assertEquals("Application cancelled", cancelApplicationResponse.getMessage())
        );
    }

    @Test
    void testCancelNonExistingApplication() {
        CancelApplicationRequest response = CancelApplicationRequest.newBuilder()
                .setApplicationId("123")
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            cancelApplicationServiceBlockingStub.cancel(response);
        });
        assertAll(
                () -> assertEquals("NOT_FOUND: Application not found", exception.getMessage())
        );
    }
}
