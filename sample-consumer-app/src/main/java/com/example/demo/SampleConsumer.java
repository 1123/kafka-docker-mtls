package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SampleConsumer {

    @KafkaListener(topics = "messages-type-A")
    public void listenForMessagesOfTypeA(String message) {
        log.info("Received message of type A: {}", message);
    }

    @KafkaListener(topics = "messages-type-B")
    public void listenForMessagesOfTypeB(String message) {
        log.info("Received message of type B: {}", message);
    }

    /**
     * This method inspects messages from the original input topic and uses the Spring support for Kafka headers
     * to output the type.
     */

    @KafkaListener(topics = "messages")
    public void listenForAllMessages(String message, @Header("type") String type) {
        log.info("Received message from original input topic {}", message);
        log.info("Message type: {}", type);
    }


}
