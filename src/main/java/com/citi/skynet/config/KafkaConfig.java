package com.citi.skynet.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.config.*;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.producer.servers}")
    private String servers;
    @Value("${kafka.producer.retries}")
    private int retries;
    @Value("${kafka.producer.batch.size}")
    private int batchSize;
    @Value("${kafka.producer.linger}")
    private int linger;
    @Value("${kafka.producer.buffer.memory}")
    private int bufferMemory;

//    #kafka\u76F8\u5173\u914D\u7F6E
//    spring.kafka.bootstrap-servers=localhost:9092
//    #\u8BBE\u7F6E\u4E00\u4E2A\u9ED8\u8BA4\u7EC4
//    spring.kafka.consumer.group-id=defaultGroup
//    #key-value\u5E8F\u5217\u5316\u53CD\u5E8F\u5217\u5316
//    spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
//    spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
//    spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
//    spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
//    spring.kafka.producer.batch-size=65536
//    spring.kafka.producer.buffer-memory=524288
//    

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<String, String>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }
}
