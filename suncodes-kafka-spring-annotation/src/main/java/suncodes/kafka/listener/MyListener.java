package suncodes.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyListener {

    @KafkaListener(groupId = "test-group", topics = "testTopic")
    public void listener(ConsumerRecord<String, String> data) {
        System.out.println("消费者线程：" + Thread.currentThread().getName()
                + "[ 消息 来自kafkatopic：" + data.topic() + ",分区：" + data.partition()
                + " ，委托时间：" + data.timestamp() + "]消息内容如下：");
        System.out.println(data.value());
    }
}
