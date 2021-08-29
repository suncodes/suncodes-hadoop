package suncodes.kafka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import suncodes.kafka.consumer.KafkaConsumerConfig;
import suncodes.kafka.producer.KafkaProducerConfig;

@Configuration
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
@ComponentScan(basePackages = {"suncodes.kafka.service", "suncodes.kafka.listener"})
public class KafkaConfig {
}
