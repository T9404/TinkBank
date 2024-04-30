package com.academy.fintech.dwh.kafka.agreement;

import com.academy.fintech.dwh.core.agreement.service.AgreementService;
import com.academy.fintech.dwh.public_interface.agreement.AgreementExportDto;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AgreementListener {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AgreementService agreementService;
    private final ObjectMapper jsonMapper;

    @Value("${exporter.dwh.agreement.error-topic}")
    private String errorTopicName;

    @KafkaListener(
            topics = "${exporter.dwh.agreement.source-topic}",
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
            var dataAgreementDto = getEvent(consumerRecord);
            agreementService.save(dataAgreementDto);
        } catch (Exception exception) {
            kafkaTemplate.send(errorTopicName, consumerRecord.key(), consumerRecord.value());
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    private AgreementExportDto getEvent(ConsumerRecord<String, String> consumerRecord) {
        JsonNode payload = jsonMapper.readTree(consumerRecord.value());

        return AgreementExportDto.builder()
                .dataAgreementId(payload.get("id").asText())
                .agreementNumber(payload.get("agreementNumber").asText())
                .status(payload.get("status").asText())
                .businessDate(payload.get("businessDate").asLong())
                .amount(BigDecimal.valueOf(payload.get("amount").asDouble()))
                .build();
    }

}
