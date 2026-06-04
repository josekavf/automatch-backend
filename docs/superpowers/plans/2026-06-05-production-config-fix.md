# Production Config Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update production configurations for the API Gateway and Notification Service to ensure proper routing and service communication in the production environment.

**Architecture:** Update `api-gateway` routes to include `notification-service` using Docker internal DNS names. Create a production properties file for `notification-service` to configure its Kafka connection.

**Tech Stack:** Spring Cloud Gateway, Spring Boot, YAML, Properties files.

---

### Task 1: Update API Gateway Production Configuration

**Files:**
- Modify: `api-gateway/src/main/resources/application-prod.yml`

- [ ] **Step 1: Add notification-service routes to application-prod.yml**

```yaml
<<<<
        - id: booking-docs
          uri: http://booking-service:8083
          predicates:
            - Path=/booking-service/v3/api-docs/**
          filters:
            - RewritePath=/booking-service/(?<path>.*), /${path}
====
        - id: booking-docs
          uri: http://booking-service:8083
          predicates:
            - Path=/booking-service/v3/api-docs/**
          filters:
            - RewritePath=/booking-service/(?<path>.*), /${path}

        - id: notification-service
          uri: http://notification-service:8084
          predicates:
            - Path=/api/v1/notifications/**
          filters:
            - AuthenticationFilter

        - id: notification-docs
          uri: http://notification-service:8084
          predicates:
            - Path=/notification-service/v3/api-docs/**
          filters:
            - RewritePath=/notification-service/(?<path>.*), /${path}
>>>>
```

- [ ] **Step 2: Commit changes**

```bash
git add api-gateway/src/main/resources/application-prod.yml
git commit -m "chore: add notification-service routes to production api-gateway config"
```

### Task 2: Create Notification Service Production Configuration

**Files:**
- Create: `notification-service/src/main/resources/application-prod.properties`

- [ ] **Step 1: Create application-prod.properties with Kafka production settings**

```properties
spring.kafka.bootstrap-servers=kafka:9092
```

- [ ] **Step 2: Commit new file**

```bash
git add notification-service/src/main/resources/application-prod.properties
git commit -m "chore: create production properties for notification-service"
```
