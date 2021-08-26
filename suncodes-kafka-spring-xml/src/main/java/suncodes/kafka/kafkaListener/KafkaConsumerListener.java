package suncodes.kafka.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * 监听类的实现
 */
public class KafkaConsumerListener implements MessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {
        System.out.println("========消费端收到消息========");
        System.out.println(stringStringConsumerRecord.value());
        //根据不同主题，消费
        if ("主题1".equals(stringStringConsumerRecord.topic())) {
            //逻辑1
        } else if ("主题2".equals(stringStringConsumerRecord.topic())) {
            //逻辑2
        }
    }
}
