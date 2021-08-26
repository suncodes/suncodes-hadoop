package suncodes.kafka.service;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

public class KafkaServiceTest {

    @Test
    public void producer() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-bean.xml");
        KafkaService kafkaService = context.getBean("kafkaService", KafkaService.class);
        System.out.println(kafkaService.producer());

        KafkaMessageListenerContainer messageListenerContainer =
                context.getBean("messageListenerContainer", KafkaMessageListenerContainer.class);
        messageListenerContainer.start();
    }
}