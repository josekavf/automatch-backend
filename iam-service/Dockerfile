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
RUN mvn dependency:go-offline -B -pl iam-service -am

COPY iam-service/src iam-service/src
RUN mvn clean package -DskipTests -pl iam-service -am

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/iam-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
