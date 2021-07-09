package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SampleConsumer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @KafkaListener(topics = "messages-type-A")
    public void listenForMessagesOfTypeA(String message) {
        log.info("Received message of type A: {}", message);
    }

    @KafkaListener(topics = "messages-type-B")
    public void listenForMessagesOfTypeB(String message) {
        log.info("Received message of type B: {}", message);
    }

}
