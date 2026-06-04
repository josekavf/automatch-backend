# Atualização do Booking Service para Disparar Notificações Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update Booking Service to send notifications to both client and professional when a booking is created.

**Architecture:**
- Update `CreateBookingRequest` DTO to include client and professional emails.
- Create `NotificationEvent` domain event.
- Update `EventPublisher` interface and its Kafka implementation.
- Update `CreateBookingUseCase` to publish notification events.

**Tech Stack:** Java, Spring Boot, Kafka, JUnit 5, Mockito.

---

### Task 1: Update DTO

**Files:**
- Modify: `booking-service/src/main/java/com/automatch/booking_service/application/dto/CreateBookingRequest.java`

- [ ] **Step 1: Add emails to CreateBookingRequest**

```java
package com.automatch.booking_service.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBookingRequest {
    @NotNull
    private UUID clientId;
    
    @NotBlank
    @Email
    private String clientEmail;
    
    @NotNull
    private UUID professionalId;
    
    @NotBlank
    @Email
    private String professionalEmail;
    
    @NotBlank
    private String serviceName;
    
    @NotNull
    private LocalDateTime appointmentTime;
}
```

- [ ] **Step 2: Commit**

```bash
git add booking-service/src/main/java/com/automatch/booking_service/application/dto/CreateBookingRequest.java
git commit -m "feat(booking): add client and professional emails to CreateBookingRequest"
```

### Task 2: Create NotificationEvent

**Files:**
- Create: `booking-service/src/main/java/com/automatch/booking_service/domain/event/NotificationEvent.java`

- [ ] **Step 1: Implement NotificationEvent**

```java
package com.automatch.booking_service.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String email;
    private String message;
}
```

- [ ] **Step 2: Commit**

```bash
git add booking-service/src/main/java/com/automatch/booking_service/domain/event/NotificationEvent.java
git commit -m "feat(booking): create NotificationEvent domain event"
```

### Task 3: Update EventPublisher Interface

**Files:**
- Modify: `booking-service/src/main/java/com/automatch/booking_service/application/service/EventPublisher.java`

- [ ] **Step 1: Add publishNotification method**

```java
package com.automatch.booking_service.application.service;

import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;

public interface EventPublisher {
    void publishBookingRequested(BookingRequestedEvent event);
    void publishNotification(NotificationEvent event);
}
```

- [ ] **Step 2: Commit**

```bash
git add booking-service/src/main/java/com/automatch/booking_service/application/service/EventPublisher.java
git commit -m "feat(booking): add publishNotification to EventPublisher interface"
```

### Task 4: Implement Notification Publishing in KafkaEventPublisher

**Files:**
- Modify: `booking-service/src/main/java/com/automatch/booking_service/infrastructure/messaging/KafkaEventPublisher.java`

- [ ] **Step 1: Implement publishNotification**

```java
package com.automatch.booking_service.infrastructure.messaging;

import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String BOOKING_EVENTS_TOPIC = "topic-booking-events";
    private static final String NOTIFICATION_EVENTS_TOPIC = "topic-notification-events";

    @Override
    public void publishBookingRequested(BookingRequestedEvent event) {
        log.info("Publishing booking requested event: {}", event);
        kafkaTemplate.send(BOOKING_EVENTS_TOPIC, event.getBookingId().toString(), event);
    }

    @Override
    public void publishNotification(NotificationEvent event) {
        log.info("Publishing notification event: {}", event);
        kafkaTemplate.send(NOTIFICATION_EVENTS_TOPIC, event.getEmail(), event);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add booking-service/src/main/java/com/automatch/booking_service/infrastructure/messaging/KafkaEventPublisher.java
git commit -m "feat(booking): implement publishNotification in KafkaEventPublisher"
```

### Task 5: Update CreateBookingUseCase and Tests (TDD)

**Files:**
- Modify: `booking-service/src/main/java/com/automatch/booking_service/application/usecase/CreateBookingUseCase.java`
- Modify: `booking-service/src/test/java/com/automatch/booking_service/application/usecase/CreateBookingUseCaseTest.java`

- [ ] **Step 1: Update test to expect two notifications (RED)**

```java
    @Test
    void execute_ShouldSaveBookingAndPublishEvents() {
        // Preparação
        CreateBookingRequest request = new CreateBookingRequest();
        request.setClientId(UUID.randomUUID());
        request.setClientEmail("client@example.com");
        request.setProfessionalId(UUID.randomUUID());
        request.setProfessionalEmail("pro@example.com");
        request.setServiceName("Oil Change");
        LocalDateTime time = LocalDateTime.of(2026, 6, 10, 10, 0);
        request.setAppointmentTime(time);

        Booking savedBooking = Booking.builder()
                .id(UUID.randomUUID())
                .clientId(request.getClientId())
                .professionalId(request.getProfessionalId())
                .serviceName(request.getServiceName())
                .appointmentTime(request.getAppointmentTime())
                .status(BookingStatus.REQUESTED)
                .build();

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // Execução
        Booking result = createBookingUseCase.execute(request);

        // Verificação
        assertNotNull(result);
        assertEquals(BookingStatus.REQUESTED, result.getStatus());
        
        verify(bookingRepository).save(any(Booking.class));
        verify(eventPublisher).publishBookingRequested(any());
        
        // Verify notifications
        verify(eventPublisher).publishNotification(argThat(event -> 
            event.getEmail().equals("client@example.com") && 
            event.getMessage().contains("Você solicitou um agendamento para Oil Change")
        ));
        
        verify(eventPublisher).publishNotification(argThat(event -> 
            event.getEmail().equals("pro@example.com") && 
            event.getMessage().contains("Você recebeu uma nova solicitação de agendamento para Oil Change")
        ));
    }
```

- [ ] **Step 2: Run test and verify failure**

Run: `mvn test -Dtest=CreateBookingUseCaseTest -pl booking-service`
Expected: FAIL (compilation error or verification failure)

- [ ] **Step 3: Update CreateBookingUseCase to trigger notifications (GREEN)**

```java
    @Transactional
    public Booking execute(CreateBookingRequest request) {
        Booking booking = Booking.builder()
                .clientId(request.getClientId())
                .professionalId(request.getProfessionalId())
                .serviceName(request.getServiceName())
                .appointmentTime(request.getAppointmentTime())
                .status(BookingStatus.REQUESTED)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        eventPublisher.publishBookingRequested(BookingRequestedEvent.builder()
                .bookingId(savedBooking.getId())
                .clientId(savedBooking.getClientId())
                .professionalId(savedBooking.getProfessionalId())
                .serviceName(savedBooking.getServiceName())
                .appointmentTime(savedBooking.getAppointmentTime())
                .build());

        String formattedDate = savedBooking.getAppointmentTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // Notificação para o cliente
        eventPublisher.publishNotification(NotificationEvent.builder()
                .email(request.getClientEmail())
                .message(String.format("Você solicitou um agendamento para %s com o profissional em %s.", 
                        savedBooking.getServiceName(), formattedDate))
                .build());

        // Notificação para o profissional
        eventPublisher.publishNotification(NotificationEvent.builder()
                .email(request.getProfessionalEmail())
                .message(String.format("Você recebeu uma nova solicitação de agendamento para %s em %s.", 
                        savedBooking.getServiceName(), formattedDate))
                .build());

        return savedBooking;
    }
```

- [ ] **Step 4: Run test and verify success**

Run: `mvn test -Dtest=CreateBookingUseCaseTest -pl booking-service`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add booking-service/src/main/java/com/automatch/booking_service/application/usecase/CreateBookingUseCase.java booking-service/src/test/java/com/automatch/booking_service/application/usecase/CreateBookingUseCaseTest.java
git commit -m "feat(booking): trigger notifications on booking creation"
```
