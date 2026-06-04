# Notification Service Setup Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create the base structure for the `notification-service` microservice, including its Maven configuration, main class, and basic settings.

**Architecture:** A new Spring Boot module within the existing parent project, configured to use Kafka for event processing and OpenTelemetry for observability.

**Tech Stack:** Java 21, Spring Boot 3.2.5, Maven, Spring Kafka, OpenTelemetry, Lombok.

---

### Task 1: Register Module in Parent POM

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: Add notification-service to modules**

```xml
<<<<
        <module>api-gateway</module>
    </modules>
====
        <module>api-gateway</module>
        <module>notification-service</module>
    </modules>
>>>>
```

- [ ] **Step 2: Commit**

```bash
git add pom.xml
git commit -m "build: register notification-service module in parent pom"
```

### Task 2: Create Notification Service POM

**Files:**
- Create: `notification-service/pom.xml`

- [ ] **Step 1: Create notification-service/pom.xml with required dependencies**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.automatch</groupId>
        <artifactId>automatch-backend-parent</artifactId> 
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
	<groupId>com.automatch</groupId>
	<artifactId>notification-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>notification-service</name>
	<description>Notification Service for AutoMatch</description>
	<url/>

	<properties>
		<java.version>21</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka</artifactId>
		</dependency>
		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-tracing-bridge-otel</artifactId>
		</dependency>
		<dependency>
			<groupId>io.opentelemetry</groupId>
			<artifactId>opentelemetry-exporter-otlp</artifactId>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
							<version>${lombok.version}</version>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>
```

- [ ] **Step 2: Commit**

```bash
git add notification-service/pom.xml
git commit -m "build: create notification-service pom.xml"
```

### Task 3: Create Main Application Class

**Files:**
- Create: `notification-service/src/main/java/com/automatch/notification_service/NotificationServiceApplication.java`

- [ ] **Step 1: Create the main class**

```java
package com.automatch.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
```

- [ ] **Step 2: Commit**

```bash
git add notification-service/src/main/java/com/automatch/notification_service/NotificationServiceApplication.java
git commit -m "feat: add NotificationServiceApplication main class"
```

### Task 4: Configure Application Properties

**Files:**
- Create: `notification-service/src/main/resources/application.properties`

- [ ] **Step 1: Create application.properties with port 8084 and Kafka settings**

```properties
spring.application.name=notification-service
server.port=8084

# Profile
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}

# Kafka Configuration
spring.kafka.consumer.group-id=notification-service-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# OpenTelemetry (Basic configuration)
management.tracing.sampling.probability=1.0
```

- [ ] **Step 2: Commit**

```bash
git add notification-service/src/main/resources/application.properties
git commit -m "config: add notification-service application properties"
```

### Task 5: Verification

**Files:**
- Create: `notification-service/src/test/java/com/automatch/notification_service/NotificationServiceApplicationTests.java`

- [ ] **Step 1: Create basic context load test**

```java
package com.automatch.notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
```

- [ ] **Step 2: Run verification**

Run: `mvn clean compile test -pl notification-service`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add notification-service/src/test/java/com/automatch/notification_service/NotificationServiceApplicationTests.java
git commit -m "test: add context load test for notification-service"
```
