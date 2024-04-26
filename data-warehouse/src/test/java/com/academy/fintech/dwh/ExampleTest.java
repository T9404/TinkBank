package com.academy.fintech.dwh;

import com.academy.fintech.dwh.configuration.ContainerTest;
import com.academy.fintech.dwh.core.application.data.repository.ApplicationDataRepository;
import com.academy.fintech.dwh.public_interface.application.ApplicationDataDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.utils.KafkaTestUtils.consumerProps;

@ContainerTest
@SpringBootTest
public class ExampleTest {

    @Autowired
    private ApplicationDataRepository applicationDataRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        applicationDataRepository.deleteAll();
    }

    @Test
    public void testExample() {
        kafkaTemplate.send(
                "raw.data.application",
                """
                        {
                            "payload": {
                                "id": "value",
                                "content": "value",
                                "business_date": "03/03/2013"
                            }
                        }
                        """
        );

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(applicationDataRepository.findById("value")).isPresent();
                });
    }

    @Test
    public void testExample2() {
        kafkaTemplate.send(
                "raw.data.application",
                """
                        {
                            "payload": {
                                "id": "value",
                                "content": "value",
                                "business_date": "03/03/2013"
                            }
                        }
                        """
        );

        kafkaTemplate.send(
                "raw.data.application",
                """
                        {
                            "payload": {
                                "id": "value",
                                "content": "value",
                                "business_date": "03/03/2013"
                            }
                        }
                        """
        );

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(applicationDataRepository.findById("value")).isPresent();
                });

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps());
        consumer.subscribe(Collections.singleton("v1.data.application.error"));


        await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(consumer.poll(Duration.ofSeconds(1)).count()).isEqualTo(1);
                });
    }

    private Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTemplate.getProducerFactory().getConfigurationProperties().get("bootstrap.servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }
}
