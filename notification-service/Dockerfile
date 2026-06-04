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
