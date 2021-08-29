package suncodes.kafka.service;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

public class KafkaServiceTest {

    @Test
    public void producer() throws InterruptedException {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-bean.xml");
        KafkaService kafkaService = context.getBean("kafkaService", KafkaService.class);
        System.out.println(kafkaService.producer());

        // 不必手动启动，因为实现了SmartLifecycle接口，在容器启动的时候会自动调用start方法。
        // 从而调用doStart
//        KafkaMessageListenerContainer messageListenerContainer =
//                context.getBean("messageListenerContainer", KafkaMessageListenerContainer.class);
//        messageListenerContainer.start();

        Thread.sleep(10000);
    }
}