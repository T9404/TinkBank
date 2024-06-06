package com.academy.fintech.pe.integration.testcontainer;

import com.academy.fintech.pe.ProductEngineApplication;
import com.academy.fintech.pe.core.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.balance.db.BalanceRepository;
import com.academy.fintech.pe.core.service.balance.db.enums.BalanceType;
import com.academy.fintech.pe.integration.testcontainer.config.ServiceIntegrationConfig;
import com.academy.fintech.pe.integration.testcontainer.config.TestContainerConfig;
import com.example.agreement.AgreementRequest;
import com.example.agreement.AgreementServiceGrpc;
import com.example.payment.ClientPaymentRequest;
import com.example.payment.ClientPaymentServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static com.academy.fintech.pe.core.conveter.ProtobufConverter.toDecimalValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = ProductEngineApplication.class)
@SpringJUnitConfig(classes = {ServiceIntegrationConfig.class, TestContainerConfig.class})
public class AddPaymentTest {
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100000);

    @GrpcClient("stocks")
    private ClientPaymentServiceGrpc.ClientPaymentServiceBlockingStub clientPaymentServiceBlockingStub;

    @GrpcClient("stocks")
    private AgreementServiceGrpc.AgreementServiceBlockingStub agreementServiceBlockingStub;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @BeforeEach
    public void setUp() {
        agreementRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    public void testAddPayment() {
        var agreement = agreementServiceBlockingStub.createAgreement(AgreementRequest.newBuilder()
                .setUserId("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setProductCode("CL")
                .setProductVersion("1.0")
                .setInterest(8)
                .setLoanTerm(12)
                .setDisbursementAmount(490000)
                .setOriginationAmount(10000)
                .build());

        var request = ClientPaymentRequest.newBuilder()
                .setApplicationId(agreement.getAgreementNumber())
                .setAmount(toDecimalValue(AMOUNT))
                .build();

        var response = clientPaymentServiceBlockingStub.addClientPayment(request);
        assertThat(response.getStatus()).isEqualTo("SUCCESS");


        // Check if the credit balance is equal to the amount
        var creditBalance = balanceRepository.findByAgreementIdAndType(agreement.getAgreementNumber(), BalanceType.CREDIT.name());

        assertThat(creditBalance).isPresent();
        assertThat(creditBalance.get().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(0));


        // Check if the debit balance is equal to the amount
        var debitBalance = balanceRepository.findByAgreementIdAndType(agreement.getAgreementNumber(), BalanceType.DEBIT.name());

        assertThat(debitBalance).isPresent();
        assertThat(debitBalance.get().getBalance()).isEqualByComparingTo(AMOUNT);
    }

    @Test
    public void testAddPaymentWithNoAgreement() {
        var request = ClientPaymentRequest.newBuilder()
                .setApplicationId("m58ed763-928c-4155-bee9-fdbaaadc15f3")
                .setAmount(toDecimalValue(AMOUNT))
                .build();

        assertThatThrownBy(() -> clientPaymentServiceBlockingStub.addClientPayment(request))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessageContaining("NOT_FOUND: Agreement with this agreement number not found");
    }
}
