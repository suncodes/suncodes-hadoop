## Producer API

引入 pom

```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.8.0</version>
</dependency>
```

需要用到的类：

- KafkaProducer：需要创建一个生产者对象，用来发送数据
- ProducerConfig：获取所需的一系列配置参数
- ProducerRecord：每条数据都要封装成一个ProducerRecord 对象

### 异步非回调：

```java
package suncodes.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 异步，不带回调函数的生产者
 */
public class AsyncNoCallbackProducer {
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

        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> topic =
                    new ProducerRecord<>("first", Integer.toString(i), Integer.toString(i));
            producer.send(topic);
        }

        producer.close();
    }
}
```

### 异步回调：

只是简单的增加回调方法：
```java
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
```

### 分区：

在配置中增加
```text
// 设置自定义分区类
props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "suncodes.kafka.partitioner.MyPartitioner");

```

```java
package suncodes.kafka.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        return 1;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }
}

```

### 同步：

同步发送的意思就是，一条消息发送之后，会阻塞当前线程，直至返回 ack。

由于 send 方法返回的是一个 Future 对象，根据 Futrue 对象的特点，我们也可以实现同步发送的效果，只需在调用 Future 对象的 get 方发即可。

```java
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
```

## Consumer API

```java
package suncodes.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

public class BaseConsumer {
    public static void main(String[] args) {

        Properties props = new Properties();
        // kafka 集 群 ，broker-list
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop01:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // 开启自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交的间隔为 1000 ms
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList("first"));
        while (true) {
            ConsumerRecords<String, String> records =
                    kafkaConsumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("key: " + record.key() + ", value: " + record.value());
            }
        }
    }
}

```


### 自动提交 offset：

```text
自动提交 offset 的相关参数：
enable.auto.commit：是否开启自动提交 offset 功能
auto.commit.interval.ms：自动提交 offset 的时间间隔
```

当开启自动提交，当进行消费数据的时候，会先把 offset 保存到内存中，之后每隔一段时间之后把 offset 提交到 kafka 集群中。

如果不开启自动提交，则只会把 offset 保存到内存中，而不会提交到 kafka 集群。这样的话，当kafka 消费者进行关闭的时候， offset 就丢失了。

当kafka消费者重启之后，会从头开始进行消费。


### 有关 offset 重置：

``` 
AUTO_OFFSET_RESET_CONFIG = "auto.offset.reset"
```

如果Kafka中没有初始偏移量，或者如果服务器上不再存在当前偏移量（例如，因为该数据已被删除）,设置此属性的时候生效。

- earliest：自动将偏移量重置为最早偏移量
- latest：自动将偏移量重置为最新偏移量

假设设置了 earliest，当消费者新加入一个组或者该数据已被删除的时候，可以从头读取数据，否则依旧是从 offset 处读取。


### 手动提交offset

虽然自动提交 offset 十分简介便利，但由于其是基于时间提交的，开发人员难以把握 offset 提交的时机。因此 Kafka 还提供了手动提交 offset 的 API。

手动提交 offset 的方法有两种：分别是 commitSync（同步提交）和 commitAsync（异步提交）。两者的相同点是，都会将本次 poll 的一批数据最高的偏移量提交；不同点是， commitSync 阻塞当前线程，一直到提交成功，并且会自动失败重试（由不可控因素导致， 也会出现提交失败）；而 commitAsync 则没有失败重试机制，故有可能提交失败。

同步提交：

```java
package suncodes.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

/**
 * 手动提交 offset，同步提交
 */
public class HandSyncConsumer {
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
        kafkaConsumer.subscribe(Arrays.asList("first"));
        while (true) {
            ConsumerRecords<String, String> records =
                    kafkaConsumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("key: " + record.key() + ", value: " + record.value());
            }
            // 同步提交，当前线程会阻塞直到 offset 提交成功
            kafkaConsumer.commitSync();
        }
    }
}

```

异步提交：
```text
// 异步提交
kafkaConsumer.commitAsync(new OffsetCommitCallback() {
    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
        if (e != null) {
            System.out.println("错误！！！");
        }
    }
});
```

无论是同步提交还是异步提交 offset，都有可能会造成数据的漏消费或者重复消费。先提交 offset 后消费，有可能造成数据的漏消费；而先消费后提交 offset，有可能会造成数据的重复消费。

### 自定义存储 offset





