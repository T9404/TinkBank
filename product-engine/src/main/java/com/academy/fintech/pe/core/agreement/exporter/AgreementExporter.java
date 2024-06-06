package com.academy.fintech.pe.core.agreement.exporter;

import com.academy.fintech.pe.core.agreement.exporter.dto.AgreementExportDto;
import com.academy.fintech.pe.core.agreement.exporter.exception.KafkaWaitingException;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.academy.fintech.pe.core.converter.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
public class AgreementExporter {
    private final KafkaTemplate<String, AgreementExportDto> kafkaTemplate;
    private final ObjectMapper jsonMapper;

    @Value("${exporter.agreement.out-topic}")
    private String agreementTopicName;

    @KafkaListener(
            topics = "${exporter.agreement.source-topic}",
            groupId = "${exporter.agreement.group-id}",
            batch = "true"
    )
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        var futures = records.stream()
                .map(this::getEvent)
                .map(agreement -> kafkaTemplate.send(agreementTopicName, agreement.agreementNumber(), agreement))
                .toArray(CompletableFuture[]::new);

        waitSending(futures);
        ack.acknowledge();
    }

    @SneakyThrows(JsonProcessingException.class)
    private AgreementExportDto getEvent(ConsumerRecord<String, String> record) {
        JsonNode payload = jsonMapper.readTree(record.value()).get("payload").get("after");

        LocalDate businessDate = convertToLocalDate(payload.get("disbursement_date").asLong());

        return AgreementExportDto.builder()
                .id(UUID.randomUUID().toString())
                .businessDate(businessDate)
                .agreementNumber(payload.get("id").asText())
                .amount(new BigDecimal(payload.get("origination_amount").asText()))
                .status(payload.get("status").asText())
                .build();
    }

    private void waitSending(CompletableFuture[] futures) {
        try {
            CompletableFuture.allOf(futures).join();
        } catch (Exception e) {
            throw new KafkaWaitingException("Error while waiting for sending messages to Kafka", e);
        }
    }
}
