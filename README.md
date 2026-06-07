# AutoMatch Backend

AutoMatch é uma plataforma de microserviços de alta performance projetada para conectar clientes a profissionais qualificados. Este ecossistema foi construído seguindo princípios de arquitetura orientada a eventos, resiliência e observabilidade avançada.

## 🏗️ Arquitetura

O projeto é modularizado nos seguintes microserviços:

- **api-gateway:** Gateway inteligente com Spring Cloud Gateway, atuando como entrypoint para segurança, roteamento e geração de Trace IDs.
- **iam-service (Identity & Access Management):** Gerenciamento de ciclo de vida de usuários, autenticação JWT e autorização RBAC.
- **catalog-service:** Gerenciamento do catálogo de profissionais com cache em Redis e projeções sincronizadas via eventos Kafka.
- **booking-service:** Core de agendamentos com suporte a **Idempotência** para garantir integridade em operações mutáveis.
- **notification-service:** Serviço de mensageria responsável por processar notificações assíncronas (e-mail/SMS) disparadas por eventos do sistema.

### 🛡️ Engenharia e Resiliência

O sistema implementa padrões modernos de backend para garantir estabilidade e rastreabilidade:

- **Observabilidade (Tracing):** Implementação de Tracing Distribuído com **Micrometer Tracing** e **OpenTelemetry**. Cada requisição recebe um \`traceId\` único que é propagado via headers HTTP e eventos Kafka, injetado automaticamente nos logs (MDC) e retornado em payloads de erro.
- **Resiliência de Broker (DLQ):** Uso de **Dead Letter Queues** e mecanismos de Retry com Backoff Exponencial no Kafka para evitar perda de mensagens em caso de falhas de processamento.
- **Idempotência:** Proteção contra requisições duplicadas no \`booking-service\` utilizando AOP (Aspect Oriented Programming) e persistência em PostgreSQL.
- **Padronização de Erros:** Respostas de erro consistentes em todos os serviços, incluindo metadados como timestamp, status code e o \`traceId\` da transação.

### Tecnologias Principais
- **Java 21** & **Spring Boot 3.2.5**
- **Spring Cloud Gateway**
- **Apache Kafka** (Resiliência com Retry/DLQ)
- **OpenTelemetry & Micrometer** (Observabilidade)
- **PostgreSQL** (Persistência e Controle de Idempotência)
- **Redis** (Cache de Alta Performance)
- **SonarQube & JaCoCo** (Qualidade com +90% de cobertura)

---

## 🚀 Ambiente de Desenvolvimento

### Pré-requisitos
- **Docker & Docker Compose**
- **Kind** (Kubernetes in Docker)
- **kubectl**
- **Java 21 JDK** & **Maven 3.9+**

### Opção 1: Kubernetes com Kind (Recomendado/Completo)
Esta opção sobe todo o ecossistema (microserviços + infra + observabilidade + qualidade) em um cluster K8s local.

1. **Executar o deploy automatizado:**
   \`\`\`bash
   ./k8s/deploy.sh
   \`\`\`
   *O script fará o build das imagens, criará o cluster e aplicará os manifestos.*

2. **Acessos:**
   - **Gateway:** [http://localhost:8080](http://localhost:8080)
   - **SonarQube:** [http://localhost:9000](http://localhost:9000)
   - **Jaeger (Traces):** Execute \`kubectl port-forward -n automatch svc/jaeger-svc 16686:16686\` e acesse [http://localhost:16686](http://localhost:16686)

### Opção 2: Docker Compose (Somente Infraestrutura)
Use esta opção se preferir rodar os microserviços via IDE.

1. **Subir a Infraestrutura:**
   \`\`\`bash
   docker compose up -d
   \`\`\`
2. **Executar os Microserviços via Maven:**
   \`\`\`bash
   mvn spring-boot:run -pl <nome-do-modulo>
   \`\`\`

---

## 🔍 Qualidade e Observabilidade

### Cobertura de Testes
O projeto mantém um rigoroso padrão de qualidade com **+90% de cobertura de instruções** em todos os serviços core.

1. **Gerar Relatórios JaCoCo:**
   \`\`\`bash
   mvn clean verify
   \`\`\`

### Análise Estática (SonarQube)
- No **Kind**: Já disponível em \`http://localhost:9000\`.
- No **Docker Compose**: Suba via \`docker compose -f docker-compose-quality.yml up -d\`.
- **Comando para envio:**
  \`\`\`bash
  mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
  \`\`\`

### Rastreabilidade (Distributed Tracing)
Os traces são coletados via **OpenTelemetry Collector** e podem ser visualizados no **Jaeger**. No ambiente Kubernetes, a malha de observabilidade já está configurada por padrão.

---

## 🚀 CI/CD Pipeline & Deployment

O projeto utiliza **GitHub Actions** para automação de CI/CD, garantindo qualidade contínua e deploys automatizados em ambientes de Staging e Produção.

### 🔄 Modelo de GitFlow e Promoção
A pipeline segue um fluxo estruturado de branches para garantir a estabilidade do código:

- **Pull Requests (PRs):** Disparam a esteira de CI que executa builds, testes unitários, testes de integração e análise de qualidade no **SonarCloud**. A aprovação da PR é obrigatória para o merge.
- **Branch \`develop\` (Staging):** O merge em \`develop\` dispara o deploy automático para o ambiente de **Staging**. É o ambiente de homologação para testes finais.
- **Branch \`main\` (Produção):** O merge em \`main\` (via PR de \`develop\`) dispara o deploy para o ambiente de **Produção**.

### 🔐 Configuração de Secrets no GitHub
Para configurar a pipeline em um novo repositório, os seguintes **GitHub Secrets** devem ser adicionados:

| Secret | Descrição |
|--------|-----------|
| \`SONAR_TOKEN\` | (Opcional) Token para autenticação no SonarCloud/SonarQube. |
| \`KUBECONFIG_STAGING\` | Arquivo \`kubeconfig\` (raw ou Base64) para acesso ao cluster de **Staging**. |
| \`KUBECONFIG_PROD\` | Arquivo \`kubeconfig\` (raw ou Base64) para acesso ao cluster de **Produção**. |

### 📦 Container Registry (GHCR)
As imagens Docker são automaticamente geradas e armazenadas no **GitHub Container Registry (GHCR)**. 
- Por padrão, o repositório deve ser **público** para que os clusters Kubernetes possam baixar as imagens sem autenticação adicional.
- Se o repositório for privado, é necessário configurar um \`imagePullSecret\` nos manifestos do Kubernetes com um **Personal Access Token (PAT)** que tenha permissões de \`read:packages\`.

---

## 👥 Autores

- **Daniel Douglas**
- **Jose Carlos Vaz Felipe**
- **Lucas Alves dos Reis**

---

## 🏭 Roteiro para Produção (Roadmap)

1. **Orquestração:** Deploy em cluster Kubernetes gerenciado (EKS/GKE) via Helm Charts.
2. **Métricas Avançadas:** Ativação de dashboards Grafana integrados ao Prometheus.
3. **Segurança:** Implementação de mTLS via Service Mesh (Istio/Linkerd).
4. **Infra Gerenciada:** Migração para AWS MSK (Kafka) e AWS RDS (Postgres).
