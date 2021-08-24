package suncodes.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class PartitionerProducer {
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

        // 设置自定义分区类
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "suncodes.kafka.partitioner.MyPartitioner");

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> topic =
                    new ProducerRecord<>("first", Integer.toString(i), Integer.toString(i));
            producer.send(topic, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("partition: " + recordMetadata.partition()
                                + ", offset: " + recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        producer.close();
    }
}
