package com.academy.fintech.dwh;

import com.academy.fintech.dwh.configuration.ContainerTest;
import com.academy.fintech.dwh.core.agreement.repository.AgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;

@ContainerTest
@SpringBootTest
class AgreementListenerTest {
    private static final String TOPIC = "v1.agreement.cdc";
    private static final String AGREEMENT_ID = "dc94481b-7dcd-43a2-afb8-c499cc0543a8";
    private static final String AGREEMENT_NUMBER = "e948e8bb-decc-45dc-b2a5-151a5edcfd54";

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        agreementRepository.deleteAll();
    }

    @Test
    void testSaveAgreement() {
        String payload = createPayload();

        kafkaTemplate.send(TOPIC, payload);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(agreementRepository.findByDataAgreementId(AGREEMENT_ID)).isPresent();
                });
    }

    @Test
    void testSaveAgreementTwice() {
        String payload = createPayload();

        kafkaTemplate.send(TOPIC, payload);
        kafkaTemplate.send(TOPIC, payload);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(agreementRepository.findAllByAgreementNumber(AGREEMENT_NUMBER)).hasSize(1);
                });
    }

    private String createPayload() {
        return String.format("""
                {
                   "id": "%s",
                   "agreementNumber": "%s",
                   "status": "CLOSED",
                   "businessDate": 1577836800000,
                   "amount": 1000
                }
                """, AGREEMENT_ID, AGREEMENT_NUMBER);
    }

}
