package suncodes.kafka;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import suncodes.kafka.service.ProducerService;

public class KafkaTest {

    @Test
    public void f() throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(KafkaConfig.class);
        ProducerService producerService = context.getBean(ProducerService.class);
        producerService.send();
        Thread.sleep(10000);
    }
}
