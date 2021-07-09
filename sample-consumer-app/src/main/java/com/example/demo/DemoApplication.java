package com.example.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public NewTopic messagesTypeA() {
		return TopicBuilder.name("messages-type-A")
				.partitions(6)
				.replicas(1)
				.compact()
				.build();
	}

	@Bean
	public NewTopic messagesTypeB() {
		return TopicBuilder.name("messages-type-B")
				.partitions(6)
				.replicas(1)
				.compact()
				.build();
	}

}

