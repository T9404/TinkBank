package com.academy.fintech.dwh.kafka.listener;

import com.academy.fintech.dwh.core.application.data.service.ApplicationDataService;
import com.academy.fintech.dwh.public_interface.application.ApplicationDataDto;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {
    private final ApplicationDataService applicationDataService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper jsonMapper;

    @Value("${exporter.dwh.data-application.error-topic}")
    private String errorTopicName;

    @KafkaListener(
            topics = "${exporter.dwh.data-application.source-topic}",
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
            applicationDataService.save(dataApplicationDto);
        } catch (Exception exception) {
            kafkaTemplate.send(errorTopicName, consumerRecord.key(), consumerRecord.value());
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    private ApplicationDataDto getEvent(ConsumerRecord<String, String> consumerRecord) {
        JsonNode payload = jsonMapper.readTree(consumerRecord.value()).get("payload");

        LocalDate businessDate = parseDate(payload.get("business_date").asText());

        return ApplicationDataDto.builder()
                .id(payload.get("id").asText())
                .content(payload.get("content").asText())
                .businessDate(businessDate)
                .build();
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[dd/MM/yyyy]" + "[yyyy/MM/dd]"
                        + "[dd-MM-yyyy]" + "[yyyy-MM-dd]"
                        + "[dd.MM.yyyy]" + "[yyyy.MM.dd]"));

        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
        return LocalDate.parse(date, dateTimeFormatter);
    }

}
