package suncodes.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 同步方法
 */
public class SyncProducer {
    public static void main(String[] args) {
        Properties props = new Properties();

        // kafka 集 群 ，broker-list
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop01:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        // 批次大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 等待时间
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // RecordAccumulator 缓冲区大小
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 5; i++) {
            ProducerRecord<String, String> topic =
                    new ProducerRecord<>("first", Integer.toString(i), Integer.toString(i));
            Future<RecordMetadata> send = producer.send(topic);
            try {
                RecordMetadata recordMetadata = send.get();
                System.out.println("partition: " + recordMetadata.partition()
                        + ", offset: " + recordMetadata.offset());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        producer.close();
    }
}
