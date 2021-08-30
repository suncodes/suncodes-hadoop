//package suncodes.kafka;
//
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
///**
// * 运行之前把 kafka-clients:2.8.0的依赖注释掉
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApplicationTests.class)
//@EmbeddedKafka(count = 4, ports = {9092, 9093, 9094, 9095})
//public class ApplicationTests {
//    @Test
//    public void contextLoads() throws IOException, ExecutionException, InterruptedException {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
//                "localhost:9092,localhost:9093,localhost:9094,localhost:9095");
//        AdminClient client = AdminClient.create(configs);
//        NewTopic topic1 = new NewTopic("foo", 10, (short) 2);
//        client.createTopics(Collections.singleton(topic1));
//        System.out.println(client.listTopics().names().get());
//        client.close();
//    }
//}