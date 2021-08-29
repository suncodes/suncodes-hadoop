package suncodes.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import suncodes.kafka.config.ConsumerPropsConfig;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ComponentScan(basePackageClasses = {ConsumerPropsConfig.class})
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerPropsConfig propsConfig;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propsConfig.getBootstrapServers());
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, propsConfig.getGroupId());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, propsConfig.getAutoCommit());
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, propsConfig.getAutoCommitIntervalMs());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, propsConfig.getKeySerializer());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, propsConfig.getValueSerializer());
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propsConfig.getOffsetReset());
        return propsMap;
    }
}
