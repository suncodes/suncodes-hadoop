package suncodes.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 自定义存储 offset
 * 比如 offset 存储在 mysql 中
 */
public class CustomOffsetConsumer {

    private static Map<TopicPartition, Long> currentOffset = new HashMap<>();

    public static void main(String[] args) {

        Properties props = new Properties();
        // kafka 集 群 ，broker-list
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop01:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // 关闭自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                // 该方法会在 Rebalance 之前调用
                commitOffset(currentOffset);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                // 该方法会在 Rebalance 之后调用
                currentOffset.clear();
                for (TopicPartition partition : partitions) {
                    // 定位到最近提交的 offset 位置继续消费
                    kafkaConsumer.seek(partition, getOffset(partition));
                }
            }
        });
        while (true) {
            // 消费者拉取数据
            ConsumerRecords<String, String> records =
                    kafkaConsumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("key: " + record.key() + ", value: " + record.value());
            }
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset	=	%d,	key	=	%s,	value = %s%n",
                        record.offset(), record.key(), record.value());
                currentOffset.put(new TopicPartition(record.topic(), record.partition()), record.offset());
            }
            // 异步提交
            commitOffset(currentOffset);
        }
    }

    /**
     * 获取某分区的最新 offset
     */
    private static long getOffset(TopicPartition partition) {
        return 0;
    }

    /**
     * 提交该消费者所有分区的 offset
     */
    private static void commitOffset(Map<TopicPartition, Long> currentOffset) {

    }
}
