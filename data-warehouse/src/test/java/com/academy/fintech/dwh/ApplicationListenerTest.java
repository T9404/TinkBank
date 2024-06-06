package com.academy.fintech.dwh;

import com.academy.fintech.dwh.configuration.ContainerTest;
import com.academy.fintech.dwh.core.application.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ContainerTest
@SpringBootTest
class ApplicationListenerTest {
    private static final String DATA_APPLICATION_ID = "dc94481b-7dcd-43a2-afb8-c499cc0543a8";
    private static final String APPLICATION_ID = "e948e8bb-decc-45dc-b2a5-151a5edcfd54";
    private static final String TOPIC = "v1.application.cdc";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        applicationRepository.deleteAll();
    }

    @Test
    void testSaveApplication() {
        String payload = createPayload();

        kafkaTemplate.send(TOPIC, payload);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(applicationRepository.findByDataApplicationId(DATA_APPLICATION_ID)).isPresent();
                });
    }

    @Test
    void testSaveApplicationTwice() {
        String payload = createPayload();

        kafkaTemplate.send(TOPIC, payload);
        kafkaTemplate.send(TOPIC, payload);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(applicationRepository.findAllByApplicationId(APPLICATION_ID)).hasSize(1);
                });
    }

    private String createPayload() {
        return String.format("""
                {
                   "dataApplicationId": "%s",
                   "applicationId": "%s",
                   "status": "CLOSED",
                   "createdAt": 1577836800000
                }
                """, DATA_APPLICATION_ID, APPLICATION_ID);
    }
}
