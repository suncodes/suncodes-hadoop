package suncodes.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import suncodes.kafka.config.ProducerPropsConfig;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ComponentScan(basePackageClasses = ProducerPropsConfig.class)
public class KafkaProducerConfig {

    @Autowired
    private ProducerPropsConfig propsConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propsConfig.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, propsConfig.getClientId());
        props.put(ProducerConfig.RETRIES_CONFIG, propsConfig.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, propsConfig.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, propsConfig.getLingerMs());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, propsConfig.getBufferMemory());
        props.put(ProducerConfig.ACKS_CONFIG, propsConfig.getAcks());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, propsConfig.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, propsConfig.getValueSerializer());
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
