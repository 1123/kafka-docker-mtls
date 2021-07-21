package com.example.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class SampleStreamsApp {

	public static void main(String[] args) {
		SpringApplication.run(SampleStreamsApp.class, args);
	}

	@Bean
	public NewTopic messages() {
		return TopicBuilder.name("messages")
				.partitions(6)
				.replicas(1)
				.compact()
				.build();
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

	@Bean
	public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
		KStream<String, String> stream = kStreamBuilder.stream(
				"messages",
				Consumed.with(new Serdes.StringSerde(), new Serdes.StringSerde())
		);
		stream.to(
				(key, value, recordContext) ->
						"messages-" + new String(recordContext.headers().lastHeader("type").value()),
				Produced.with(new Serdes.StringSerde(), new Serdes.StringSerde())
		);
		return stream;
	}

}

