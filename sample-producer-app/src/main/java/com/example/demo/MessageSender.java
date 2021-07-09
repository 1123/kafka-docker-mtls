package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class MessageSender {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Random r = new Random();

    public MessageSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void produce() {
        var record = new ProducerRecord<>(
                "messages",
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Collections.singletonList(
                        new RecordHeader(
                                "type",
                                (r.nextFloat() < 0.5) ?
                                        "type-A".getBytes(StandardCharsets.UTF_8) :
                                        "type-B".getBytes(StandardCharsets.UTF_8)
                        )
                )
        );
        log.info("Sending record {}", record.toString());
        kafkaTemplate.send(record);
    }

}
