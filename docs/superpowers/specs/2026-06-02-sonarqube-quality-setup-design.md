# Design Spec: SonarQube Quality Sidecar Setup

**Date:** 2026-06-02
**Status:** Draft
**Topic:** Code Quality and Test Coverage Environment

## 1. Objective
Establish a local dockerized environment for code quality analysis using SonarQube and JaCoCo, integrated into the AutoMatch microservices project (Maven). The goal is to provide developers with actionable insights into code smells, bugs, vulnerabilities, and test coverage without impacting the main application stack.

## 2. Architecture: The "Quality Sidecar"
The solution uses a "Sidecar" approach, where quality tools run in an isolated Docker Compose environment.

### Components:
- **SonarQube (Community Edition):** Central dashboard for quality metrics.
- **PostgreSQL:** Dedicated database for SonarQube persistence.
- **JaCoCo:** Maven plugin for generating code coverage reports.
- **Sonar Maven Plugin:** Scanner to push results to the SonarQube server.

## 3. Infrastructure Configuration

### `docker-compose-quality.yml`
A separate compose file to keep development and quality environments isolated.

```yaml
version: '3.8'
services:
  sonarqube:
    image: sonarqube:10.5-community
    container_name: automatch-sonarqube
    ports:
      - "9000:9000"
    environment:
      - SONAR_JDBC_URL=jdbc:postgresql://sonardb:5432/sonar
      - SONAR_JDBC_USERNAME=sonar
      - SONAR_JDBC_PASSWORD=sonar
    depends_on:
      - sonardb
    networks:
      - quality-net

  sonardb:
    image: postgres:16-alpine
    container_name: automatch-sonardb
    environment:
      - POSTGRES_USER=sonar
      - POSTGRES_PASSWORD=sonar
      - POSTGRES_DB=sonar
    volumes:
      - sonardb_data:/var/lib/postgresql/data
    networks:
      - quality-net

networks:
  quality-net:
    driver: bridge

volumes:
  sonardb_data:
```

## 4. Build System Integration (Maven)

### Parent `pom.xml` Updates
Centralize quality properties and plugin management.

#### Properties
```xml
<properties>
    <sonar.java.binaries>target/classes</sonar.java.binaries>
    <sonar.junit.reportPaths>target/surefire-reports</sonar.junit.reportPaths>
    <sonar.coverage.jacoco.xmlReportPaths>
        ${project.basedir}/target/site/jacoco/jacoco.xml
    </sonar.coverage.jacoco.xmlReportPaths>
    <!-- Common Exclusions -->
    <sonar.exclusions>
        **/dto/**,
        **/entity/**,
        **/config/**,
        **/exception/**,
        **/*Application.java
    </sonar.exclusions>
</properties>
```

#### Plugins
```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>3.11.0.3922</version>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.11</version>
            </plugin>
        </plugins>
    </pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## 5. Workflow
1. **Start Environment:** `docker-compose -f docker-compose-quality.yml up -d`
2. **Build & Test:** `mvn clean verify` (Generates JaCoCo XML reports).
3. **Analyze:** `mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin`
4. **Access Dashboard:** `http://localhost:9000`

## 6. Success Criteria
- SonarQube dashboard accessible with history persistence.
- Coverage reports generated for all modules (iam, catalog, booking, gateway).
- Analysis results include both static analysis (bugs/smells) and dynamic analysis (coverage).
