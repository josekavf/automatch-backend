# Validation Fix for Booking Service Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix validation issues in the booking service by adding the missing validation starter and updating tests to include required email fields.

**Architecture:** Standard Spring Boot Validation with jakarta annotations in DTOs.

**Tech Stack:** Spring Boot, Java 21, Maven, JUnit 5, MockMvc.

---

### Task 1: Add validation dependency

**Files:**
- Modify: `booking-service/pom.xml`

- [ ] **Step 1: Add spring-boot-starter-validation dependency**

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
```

Add it after `springdoc-openapi-starter-webmvc-ui` in `booking-service/pom.xml`.

- [ ] **Step 2: Commit**

```bash
git add booking-service/pom.xml
git commit -m "fix(booking): add spring-boot-starter-validation dependency"
```

### Task 2: Update BookingControllerTest

**Files:**
- Modify: `booking-service/src/test/java/com/automatch/booking_service/presentation/controller/BookingControllerTest.java`

- [ ] **Step 1: Update CreateBookingRequest in create_ShouldReturnCreated test**

```java
    @Test
    void create_ShouldReturnCreated() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setClientId(UUID.randomUUID());
        request.setClientEmail("client@example.com");
        request.setProfessionalId(UUID.randomUUID());
        request.setProfessionalEmail("pro@example.com");
        request.setServiceName("Oil Change");
        request.setAppointmentTime(LocalDateTime.now().plusDays(1));
        // ...
```

- [ ] **Step 2: Run tests to verify they pass**

Run: `mvn test -pl booking-service`
Expected: 0 failures

- [ ] **Step 3: Commit**

```bash
git add booking-service/src/test/java/com/automatch/booking_service/presentation/controller/BookingControllerTest.java
git commit -m "test(booking): update controller test to include required email fields"
```
