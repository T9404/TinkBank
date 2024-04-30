package com.academy.fintech.dwh.kafka.application;

import com.academy.fintech.dwh.core.application.service.ApplicationService;
import com.academy.fintech.dwh.public_interface.application.ApplicationExportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ApplicationListener {
    private final ApplicationService applicationService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper jsonMapper;

    @Value("${exporter.dwh.application.error-topic}")
    private String errorTopicName;

    @KafkaListener(
            topics = "${exporter.dwh.application.source-topic}",
            groupId = "${consumer.kafka.group-id}",
            batch = "true"
    )
    @Transactional("transactionManager")
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        List<CompletableFuture<Void>> futures = records.stream()
                .map(consumerRecord -> CompletableFuture.runAsync(() -> processRecord(consumerRecord)))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRun(ack::acknowledge);
    }

    private void processRecord(ConsumerRecord<String, String> consumerRecord) {
        try {
            var dataApplicationDto = getEvent(consumerRecord);
            applicationService.save(dataApplicationDto);
        } catch (Exception exception) {
            kafkaTemplate.send(errorTopicName, consumerRecord.key(), consumerRecord.value());
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    private ApplicationExportDto getEvent(ConsumerRecord<String, String> consumerRecord) {
        JsonNode payload = jsonMapper.readTree(consumerRecord.value());

        return ApplicationExportDto.builder()
                .dataApplicationId(payload.get("dataApplicationId").asText())
                .applicationId(payload.get("applicationId").asText())
                .status(payload.get("status").asText())
                .createdAt(payload.get("createdAt").asLong())
                .build();
    }

}
