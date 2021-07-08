package com.example.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
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
				.partitions(10)
				.replicas(3)
				.compact()
				.build();
	}

	@Bean
	public NewTopic messagesTypeB() {
		return TopicBuilder.name("messages-type-B")
				.partitions(10)
				.replicas(3)
				.compact()
				.build();
	}

	@Bean
	public KStream<?, ?> kStream(StreamsBuilder kStreamBuilder) {
		KStream<Integer, byte[]> stream = kStreamBuilder.stream(
				"messages",
				Consumed.with(new Serdes.IntegerSerde(), new Serdes.ByteArraySerde())
		);
		stream.mapValues(m -> m)
				.to("messages-type-A", Produced.with(new Serdes.IntegerSerde(), new Serdes.ByteArraySerde()));
		stream.mapValues(m -> m)
				.to("messages-type-B", Produced.with(new Serdes.IntegerSerde(), new Serdes.ByteArraySerde()));
		return stream;
	}

}

