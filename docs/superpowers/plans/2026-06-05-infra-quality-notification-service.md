# Infrastructure and Quality Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Containerize the notification-service, integrate it with the API Gateway (routing and Swagger), and ensure code quality via SonarQube.

**Architecture:** The notification-service will be containerized using a multi-stage Dockerfile. The API Gateway will be updated to route requests to it and aggregate its Swagger documentation. OpenApi documentation will be enabled in the service.

**Tech Stack:** Docker, Docker Compose, Spring Cloud Gateway, SpringDoc OpenAPI, Maven, SonarQube.

---

### Task 1: Update notification-service Dependencies

**Files:**
- Modify: `notification-service/pom.xml`

- [ ] **Step 1: Add springdoc-openapi dependency**
Add the following dependency to `notification-service/pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

- [ ] **Step 2: Commit**
```bash
git add notification-service/pom.xml
git commit -m "chore: add springdoc-openapi dependency to notification-service"
```

### Task 2: Implement OpenApiConfig in notification-service

**Files:**
- Create: `notification-service/src/main/java/com/automatch/notification_service/infrastructure/config/OpenApiConfig.java`

- [ ] **Step 1: Create OpenApiConfig class**
```java
package com.automatch.notification_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API do Serviço de Notificação do AutoMatch")
                        .description("Serviço responsável pelo envio de notificações para clientes e profissionais")
                        .version("1.0.0"));
    }
}
```

- [ ] **Step 2: Commit**
```bash
git add notification-service/src/main/java/com/automatch/notification_service/infrastructure/config/OpenApiConfig.java
git commit -m "feat: add OpenApiConfig to notification-service"
```

### Task 3: Create Dockerfile for notification-service

**Files:**
- Create: `notification-service/Dockerfile`

- [ ] **Step 1: Create multi-stage Dockerfile**
Follow the pattern from `booking-service/Dockerfile`.
```dockerfile
# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Copy child modules pom
COPY iam-service/pom.xml iam-service/
COPY catalog-service/pom.xml catalog-service/
COPY booking-service/pom.xml booking-service/
COPY notification-service/pom.xml notification-service/
COPY api-gateway/pom.xml api-gateway/

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B -pl notification-service -am

COPY notification-service/src notification-service/src
RUN mvn clean package -DskipTests -pl notification-service -am

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- [ ] **Step 2: Commit**
```bash
git add notification-service/Dockerfile
git commit -m "infra: add Dockerfile for notification-service"
```

### Task 4: Update docker-compose.yml

**Files:**
- Modify: `docker-compose.yml`

- [ ] **Step 1: Add notification-service to docker-compose**
```yaml
  notification-service:
    build:
      context: .
      dockerfile: notification-service/Dockerfile
    container_name: automatch-notification
    ports:
      - "8084:8084"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    depends_on:
      - kafka
    networks:
      - automatch-net
```

- [ ] **Step 2: Commit**
```bash
git add docker-compose.yml
git commit -m "infra: add notification-service to docker-compose.yml"
```

### Task 5: Configure API Gateway for Notification Service

**Files:**
- Modify: `api-gateway/src/main/resources/application.yml`
- Modify: `api-gateway/src/main/resources/application-dev.yml`

- [ ] **Step 1: Update Swagger UI aggregation in application.yml**
Add "Serviço de Notificação" to `springdoc.swagger-ui.urls`.
```yaml
      - name: Serviço de Notificação
        url: /notification-service/v3/api-docs
```

- [ ] **Step 2: Update routes in application-dev.yml**
Add notification-service routes.
```yaml
        - id: notification-service
          uri: http://localhost:8084
          predicates:
            - Path=/api/v1/notifications/**
          filters:
            - AuthenticationFilter

        - id: notification-docs
          uri: http://localhost:8084
          predicates:
            - Path=/notification-service/v3/api-docs/**
          filters:
            - RewritePath=/notification-service/(?<path>.*), /${path}
```

- [ ] **Step 3: Commit**
```bash
git add api-gateway/src/main/resources/application.yml api-gateway/src/main/resources/application-dev.yml
git commit -m "infra: configure api-gateway routes for notification-service"
```

### Task 6: Quality Verification

- [ ] **Step 1: Run Maven clean verify**
Run: `mvn clean verify -DskipTests` (to check builds) or just `mvn clean verify` if you want to run all tests. Let's do `mvn clean verify` to be sure.

- [ ] **Step 2: Run SonarQube analysis**
Ensure SonarQube is running (usually it's in another docker-compose or the user expects it to be available).
Run: `mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin`

- [ ] **Step 3: Final Commit**
Ensure all changes are committed.
