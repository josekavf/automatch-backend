package com.automatch.iam_service.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("topic-user-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public org.springframework.kafka.core.ProducerFactory<String, Object> producerFactory(org.springframework.boot.autoconfigure.kafka.KafkaProperties kafkaProperties) {
        java.util.Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new org.springframework.kafka.core.DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate(org.springframework.kafka.core.ProducerFactory<String, Object> producerFactory) {
        return new org.springframework.kafka.core.KafkaTemplate<>(producerFactory);
    }
}
