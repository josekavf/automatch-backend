# CI/CD Pipeline Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Automate build, test, quality analysis, and multi-environment deployment using GitHub Actions and K3s.

**Architecture:** 
- **CI**: PR validation (Maven + SonarCloud).
- **CD Staging**: Merge to `develop` -> GHCR (`:develop`) -> K3s `staging`.
- **CD Prod**: Release/Merge to `main` -> GHCR (`:latest`) -> K3s `production`.

**Tech Stack:** GitHub Actions, GHCR, Docker, Kubernetes (K3s), Maven, SonarCloud.

---

### Task 1: Continuous Integration (PR Validation)

**Files:**
- Create: `.github/workflows/ci.yml`

- [ ] **Step 1: Create the CI workflow file**
Create `.github/workflows/ci.yml` to trigger on Pull Requests to `develop` and `main`.

- [ ] **Step 2: Commit CI workflow**

---

### Task 2: Parameterize Kubernetes Manifests

**Files:**
- Modify: `k8s/full-stack.yml`

- [ ] **Step 1: Update image tags and namespaces to use placeholders**
Prepare `k8s/full-stack.yml` for dynamic deployment.

- [ ] **Step 2: Commit parameterized manifest**

---

### Task 3: Staging Deployment (CD - Develop)

**Files:**
- Create: `.github/workflows/cd-staging.yml`

- [ ] **Step 1: Create the Staging CD workflow**
Trigger on push to `develop`. Build and push images to GHCR with `:develop` tag, then deploy to `staging` namespace.

- [ ] **Step 2: Commit Staging workflow**

---

### Task 4: Production Deployment (CD - Main)

**Files:**
- Create: `.github/workflows/cd-prod.yml`

- [ ] **Step 1: Create the Production CD workflow**
Trigger on push to `main`. Use `:latest` tag and `production` namespace.

- [ ] **Step 2: Commit Production workflow**

---

### Task 5: Final Documentation

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Add CI/CD Setup section**
Instructions for GitHub Secrets.

- [ ] **Step 2: Final commit**
