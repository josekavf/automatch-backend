package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaEventPublisher kafkaEventPublisher;

    @Test
    void publishProfessionalUpdated_ShouldSendToKafka() {
        ProfessionalUpdatedEvent event = ProfessionalUpdatedEvent.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .specialty("Mechanic")
                .services(List.of("Oil Change"))
                .active(true)
                .build();

        kafkaEventPublisher.handleProfessionalUpdatedEvent(event);

        verify(kafkaTemplate).send("topic-catalog-events", event.getId().toString(), event);
    }
}
