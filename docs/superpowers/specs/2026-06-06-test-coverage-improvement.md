# Design Doc: Test Coverage Improvement to 90%

## Objective
Increase the project's test coverage to at least 90% instruction coverage, as measured by JaCoCo, by excluding boilerplate code and implementing missing unit/integration tests for critical business logic and infrastructure components.

## Current Status
-   **Overall Coverage**: ~15-35% (initial)
-   **After Lombok Exclusion**: ~20-65%
-   **Key Gaps**: Infrastructure implementations (Repositories, Messaging), Use Cases (in some services), and Global Exception Handlers.

## Strategy

### 1. Boilerplate Exclusion
-   Maintain `lombok.config` with `lombok.addLombokGeneratedAnnotation = true` to ensure JaCoCo ignores Lombok-generated methods (getters, setters, builders).
-   Exclude configuration classes and main Application classes from coverage if they contain no logic.

### 2. Infrastructure Layer Testing
-   Implement unit tests for `*RepositoryImpl` classes using `Mockito` to verify mapping between Domain and Entity models.
-   Implement unit tests for `KafkaEventPublisher` classes.
-   Test `JwtTokenServiceImpl` for correct token generation and claims.

### 3. Application Layer (Use Cases)
-   Complete missing test cases for all Use Cases, including edge cases and exception paths (e.g., entity not found, validation errors).

### 4. Presentation Layer
-   Implement `GlobalExceptionHandlerTest` for each service to ensure error responses are correctly formatted and include the `traceId`.
-   Add missing controller tests using `MockMvc`.

## Detailed Implementation Plan

### iam-service
-   `UserRepositoryImplTest`: Verify `save`, `findByEmail`, `findById`, `existsByEmail`.
-   `JwtTokenServiceImplTest`: Verify token claims (role, firstName, lastName) and expiration.
-   `KafkaEventPublisherTest`: Verify `UserRegisteredEvent` publishing.

### catalog-service
-   `ProfessionalRepositoryImplTest`: Verify mapping and JPA calls.
-   `UpdateProfessionalUseCaseTest`: Verify success and "not found" scenarios.
-   `ProfessionalEventListenerTest`: Verify listener correctly calls use cases.
-   `GlobalExceptionHandlerTest`: Verify JSON error response structure.

### booking-service
-   `BookingRepositoryImplTest`: Verify booking persistence mapping.
-   `KafkaEventPublisherTest`: Verify `BookingRequestedEvent` publishing.
-   `GlobalExceptionHandlerTest`: Verify traceId inclusion.

### notification-service
-   `NotificationEventListenerTest`: Verify listener calls the process notification use case.
-   `GlobalExceptionHandlerTest`: Verify consistency.

## Verification
-   Run `mvn clean verify` and use the Python script to extract and report the new coverage percentages.
-   Ensure all tests pass and coverage meets the 90% threshold for each module (excluding boilerplates).
