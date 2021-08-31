package suncodes.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import suncodes.kafka.KafkaConstant;

@Component
public class MyKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void kafkaSend() {
        kafkaTemplate.send(KafkaConstant.TOPIC, "SpringBoot send...");
    }
}
