# AutoMatch Backend

AutoMatch é uma plataforma de microserviços projetada para conectar clientes a profissionais qualificados de forma eficiente e segura. Este repositório contém o ecossistema de backend, construído com Java, Spring Boot e uma arquitetura orientada a eventos.

## 🏗️ Arquitetura

O projeto é composto pelos seguintes microserviços:

- **api-gateway:** Ponto de entrada único, responsável pelo roteamento e segurança (Authentication Filter).
- **iam-service (Identity & Access Management):** Gerenciamento de usuários, autenticação e autorização.
- **catalog-service:** Catálogo de profissionais, permitindo buscas e visualização de perfis.
- **booking-service:** Gerenciamento de agendamentos e solicitações de serviços.

### Tecnologias Principais
- **Java 21** & **Spring Boot 3.2.5**
- **Spring Cloud Gateway**
- **Apache Kafka** (Mensageria/Eventos)
- **PostgreSQL** (Persistência)
- **Redis** (Cache no Catalog Service)
- **SonarQube & JaCoCo** (Qualidade e Cobertura)

---

## 🚀 Ambiente de Desenvolvimento

### Pré-requisitos
- Docker & Docker Compose
- Java 21 JDK
- Maven 3.9+

### Passos para Subir o Projeto

1. **Subir a Infraestrutura (Bancos e Mensageria):**
   ```bash
   docker compose up -d
   ```
   *Isso iniciará o PostgreSQL, Kafka, Zookeeper e Redis.*

2. **Compilar e Instalar as Dependências (Root):**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Executar os Microserviços:**
   Você pode rodar cada serviço individualmente através da sua IDE preferida ou via terminal:
   ```bash
   # Exemplo para o IAM Service
   cd iam-service
   mvn spring-boot:run
   ```
   *Repita para os outros serviços na ordem: iam -> catalog -> booking -> api-gateway.*

---

## 🔍 Qualidade com SonarQube

Estabelecemos um "Sidecar de Qualidade" para análise estática e cobertura de testes.

1. **Subir o Ambiente SonarQube:**
   ```bash
   docker compose -f docker-compose-quality.yml up -d
   ```
   *Aguarde o serviço iniciar em `http://localhost:9000` (User/Pass padrão: `admin/admin`).*

2. **Gerar Relatórios JaCoCo:**
   ```bash
   mvn clean verify
   ```

3. **Executar Análise Sonar:**
   ```bash
   mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
   ```

---

## 🐞 Depuração (Debug)

Para debugar os microserviços:

1. **Via Terminal:**
   Adicione os parâmetros de JVM para permitir conexão remota:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
   ```
   *Nota: Altere a porta `address` para cada serviço se estiver rodando múltiplos ao mesmo tempo.*

2. **Via IDE (VS Code / IntelliJ):**
   - Utilize as configurações de "Run/Debug" nativas da IDE apontando para a classe `Application` principal de cada módulo.

---

## 👥 Autores

- **Daniel Douglas**
- **Jose Carlos Vaz Felipe**
- **Lucas Alves dos Reis**

---

## 🏭 Roteiro para Produção (Roadmap)

Para levar o AutoMatch para um ambiente de produção, siga este roteiro sugerido:

1. **Containerização Avançada:**
   - Build de imagens Docker otimizadas (Multi-stage builds) para cada serviço.
   - Publicação em um Container Registry (Docker Hub, AWS ECR, etc).

2. **Orquestração:**
   - Deploy em um cluster **Kubernetes** (K8s).
   - Utilização de Helm Charts para gerenciar os manifests.

3. **Configuração Externalizada:**
   - Utilizar Spring Cloud Config ou Kubernetes ConfigMaps/Secrets.
   - Configurar perfis de `application-prod.yml`.

4. **Monitoramento e Observabilidade:**
   - Configurar o coletor **OpenTelemetry** (já presente no projeto).
   - Integrar com **Prometheus** (Métricas) e **Grafana** (Dashboards).
   - Centralização de logs (ELK Stack ou Loki).

5. **Infraestrutura Gerenciada:**
   - Migrar bancos de dados Docker para serviços gerenciados (Ex: AWS RDS).
   - Utilizar um serviço de Kafka gerenciado (Ex: Confluent Cloud ou AWS MSK).

6. **Segurança:**
   - Implementar HTTPS/TLS no Gateway.
   - Configurar Ingress Controllers e WAF.
