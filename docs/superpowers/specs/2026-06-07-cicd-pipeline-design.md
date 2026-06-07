# Design Doc: CI/CD Pipeline with GitHub Actions and K3s

## Objective
Implement a robust, agnostic, and automated CI/CD pipeline for the AutoMatch project using GitHub Actions. The pipeline will support the GitFlow branching strategy, publish images to GitHub Container Registry (GHCR), and deploy microservices to a VPS running K3s (lightweight Kubernetes).

## Architecture & Workflow

### 1. Branching Strategy (GitFlow)
- **`main`**: Reflects the current production state. Triggers the Production Deployment workflow on merges or tags.
- **`develop`**: The main integration branch for features. Triggers the Staging Deployment workflow on every merge.
- **`feature/*`, `bugfix/*`**: Work branches. Trigger the Continuous Integration (CI) validation workflow (build & test) on Pull Requests.

### 2. CI/CD Workflows

#### Pipeline A: Continuous Integration (PR Validation)
- **Trigger**: Pull Requests to `develop` or `main`.
- **Jobs**:
    - **Build & Test**: Compile the Java project using Maven, run unit/integration tests with JaCoCo.
    - **Quality Gate**: Send coverage reports to SonarCloud (or a hosted SonarQube) to verify quality metrics (>90% coverage).

#### Pipeline B: Staging Deployment (CD - Develop)
- **Trigger**: Push/Merge to `develop`.
- **Jobs**:
    - **Publish**: Build Docker images for all 5 microservices and the API Gateway. Push them to **GHCR** with the `:develop` tag.
    - **Deploy**: Update the K3s cluster on the VPS in the `staging` namespace using `kubectl`.

#### Pipeline C: Production Deployment (CD - Main)
- **Trigger**: Push/Merge to `main` or creation of a SemVer tag (e.g., `v1.0.0`).
- **Jobs**:
    - **Publish**: Build and push Docker images to **GHCR** with `:latest` and version tags.
    - **Deploy**: Update the K3s cluster on the VPS in the `production` namespace.

### 3. Agnostic Deployment Mechanism
Deployment is decoupled from the VPS provider by using the Kubernetes API:
- **KUBECONFIG**: The GitHub Action will use a `KUBECONFIG` secret to authenticate with the remote K3s cluster.
- **Namespaces**: `staging` and `production` namespaces will be used to isolate environments on the same or different VPS instances.
- **Manifests**: Reuses and parameterizes `k8s/full-stack.yml` to support environment-specific configurations (URLs, Secrets).

## Security & Secrets
The following secrets must be configured in the GitHub Repository:
- `GH_TOKEN`: To publish/pull images from GHCR.
- `SONAR_TOKEN`: For quality analysis.
- `KUBECONFIG_STAGING` / `KUBECONFIG_PROD`: To access the remote K3s cluster.
- `DB_PASSWORD_PROD`: For production database integrity.

## Success Criteria
- [ ] Every Pull Request is automatically tested and analyzed for quality.
- [ ] Merges to `develop` result in an updated staging environment within minutes.
- [ ] Releases are tagged and deployed to production with zero manual intervention in the VPS shell.
- [ ] Deployment works on any VPS that provides a standard `KUBECONFIG`.
