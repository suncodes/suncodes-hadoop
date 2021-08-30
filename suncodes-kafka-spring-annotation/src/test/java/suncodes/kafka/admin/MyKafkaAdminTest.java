package suncodes.kafka.admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.concurrent.ExecutionException;

public class MyKafkaAdminTest {

    @Test
    public void f() throws ExecutionException, InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyKafkaAdmin.class);
        KafkaAdmin admin = context.getBean("admin", KafkaAdmin.class);
        NewTopic topic1 = context.getBean("topic1", NewTopic.class);
        admin.createOrModifyTopics(topic1);

        // 对于更高级的功能，例如为副本分配分区，您可以直接使用 AdminClient
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        System.out.println(client.listTopics().names().get());
        client.close();
    }
}