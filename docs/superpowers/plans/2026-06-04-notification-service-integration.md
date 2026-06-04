# Implementação do Microserviço de Notificação e Integração com Booking

> **Para trabalhadores agentes:** REQUISITO DE SUB-HABILIDADE: Use superpowers:subagent-driven-development (recomendado) ou superpowers:executing-plans para implementar este plano tarefa por tarefa. Os passos usam a sintaxe de checkbox (`- [ ]`) para rastreamento.

**Objetivo:** Criar um novo microserviço `notification-service` que consome eventos do Kafka para enviar notificações (emuladas) para clientes e profissionais quando um agendamento é criado.

**Arquitetura:**
- O `booking-service` passa a enviar eventos de notificação para o tópico `topic-notification-events`.
- O `notification-service` consome este tópico e processa o envio, sendo agnóstico ao tipo de mensagem.
- Integração com OpenTelemetry para telemetria e JaCoCo/SonarQube para qualidade.

**Tech Stack:**
- Java 21, Spring Boot 3.2.5
- Spring Kafka
- OpenTelemetry
- JUnit 5, Mockito
- Docker & Docker Compose

---

### Tarefa 1: Criação do Notification Service (Estrutura Base)

**Arquivos:**
- Criar: `notification-service/pom.xml`
- Criar: `notification-service/src/main/resources/application.properties`
- Criar: `notification-service/src/main/java/com/automatch/notification_service/NotificationServiceApplication.java`
- Modificar: `pom.xml` (root)

- [ ] **Passo 1: Adicionar o módulo ao pom.xml principal**
- [ ] **Passo 2: Criar o pom.xml do notification-service com dependências de Kafka, Web, OTel e Lombok**
- [ ] **Passo 3: Criar a classe principal do Spring Boot**
- [ ] **Passo 4: Configurar o application.properties com porta 8084 e configurações de Kafka**
- [ ] **Passo 5: Commit**

### Tarefa 2: Implementação do Domínio e Lógica de Notificação

**Arquivos:**
- Criar: `notification-service/src/main/java/com/automatch/notification_service/domain/event/NotificationEvent.java`
- Criar: `notification-service/src/main/java/com/automatch/notification_service/application/usecase/ProcessNotificationUseCase.java`
- Criar: `notification-service/src/main/java/com/automatch/notification_service/infrastructure/messaging/NotificationEventListener.java`

- [ ] **Passo 1: Definir o DTO NotificationEvent (destinatario, mensagem)**
- [ ] **Passo 2: Implementar o UseCase ProcessNotificationUseCase que apenas loga o envio (emulação)**
- [ ] **Passo 3: Implementar o KafkaListener para consumir do tópico `topic-notification-events`**
- [ ] **Passo 4: Escrever teste unitário para o UseCase**
- [ ] **Passo 5: Commit**

### Tarefa 3: Atualização do Booking Service para Disparar Notificações

**Arquivos:**
- Modificar: `booking-service/src/main/java/com/automatch/booking_service/application/dto/CreateBookingRequest.java`
- Criar: `booking-service/src/main/java/com/automatch/booking_service/domain/event/NotificationEvent.java`
- Modificar: `booking-service/src/main/java/com/automatch/booking_service/application/service/EventPublisher.java`
- Modificar: `booking-service/src/main/java/com/automatch/booking_service/infrastructure/messaging/KafkaEventPublisher.java`
- Modificar: `booking-service/src/main/java/com/automatch/booking_service/application/usecase/CreateBookingUseCase.java`

- [ ] **Passo 1: Adicionar `clientEmail` e `professionalEmail` ao CreateBookingRequest**
- [ ] **Passo 2: Adicionar método `publishNotification` à interface EventPublisher**
- [ ] **Passo 3: Implementar o envio de notificação no KafkaEventPublisher**
- [ ] **Passo 4: Atualizar CreateBookingUseCase para disparar duas notificações (cliente e profissional)**
- [ ] **Passo 5: Atualizar testes unitários do Booking Service**
- [ ] **Passo 6: Commit**

### Tarefa 4: Infraestrutura e Qualidade

**Arquivos:**
- Criar: `notification-service/Dockerfile`
- Criar: `notification-service/src/main/java/com/automatch/notification_service/infrastructure/config/OpenApiConfig.java`
- Modificar: `docker-compose.yml`
- Modificar: `api-gateway/src/main/resources/application.yml`
- Modificar: `api-gateway/src/main/resources/application-dev.yml`

- [ ] **Passo 1: Criar Dockerfile para o notification-service**
- [ ] **Passo 2: Adicionar o serviço ao docker-compose.yml**
- [ ] **Passo 3: Configurar rotas e Swagger no API Gateway**
- [ ] **Passo 4: Configurar OpenApiConfig no novo serviço**
- [ ] **Passo 5: Executar mvn clean verify e validar no SonarQube**
- [ ] **Passo 6: Commit final**
