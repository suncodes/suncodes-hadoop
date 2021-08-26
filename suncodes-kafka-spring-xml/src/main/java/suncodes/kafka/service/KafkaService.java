package suncodes.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 消息发送
     */
    public String producer() {
        kafkaTemplate.send("testTopic", "producer发送消息");
        return "success";
    }
}