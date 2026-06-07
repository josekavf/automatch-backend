#!/bin/bash

CLUSTER_NAME="automatch-cluster"

echo "🚀 Iniciando deploy do AutoMatch no Kind..."

# 1. Build das imagens (opcional se já existirem)
echo "📦 Buildando imagens dos microserviços..."
docker build -t automatch-api-gateway:latest -f api-gateway/Dockerfile .
docker build -t automatch-iam-service:latest -f iam-service/Dockerfile .
docker build -t automatch-catalog-service:latest -f catalog-service/Dockerfile .
docker build -t automatch-booking-service:latest -f booking-service/Dockerfile .
docker build -t automatch-notification-service:latest -f notification-service/Dockerfile .

# 2. Criar cluster Kind se não existir
if ! kind get clusters | grep -q "$CLUSTER_NAME"; then
  echo "🏗️ Criando cluster Kind..."
  kind create cluster --name "$CLUSTER_NAME" --config - <<KINDCONF
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 30080
    hostPort: 8080
  - containerPort: 30000
    hostPort: 9000
KINDCONF
fi

# 3. Carregar imagens no Kind
echo "🚚 Carregando imagens no cluster..."
kind load docker-image automatch-api-gateway:latest --name "$CLUSTER_NAME"
kind load docker-image automatch-iam-service:latest --name "$CLUSTER_NAME"
kind load docker-image automatch-catalog-service:latest --name "$CLUSTER_NAME"
kind load docker-image automatch-booking-service:latest --name "$CLUSTER_NAME"
kind load docker-image automatch-notification-service:latest --name "$CLUSTER_NAME"

# 4. Aplicar manifestos
echo "☸️ Aplicando manifestos Kubernetes..."
kubectl apply -f k8s/full-stack.yml

echo "✅ Deploy finalizado! Aguarde alguns minutos para os pods subirem."
echo "🔗 Gateway: http://localhost:8080"
echo "📊 SonarQube: http://localhost:9000"
echo "🔍 Jaeger: kubectl port-forward -n automatch svc/jaeger-svc 16686:16686 & (http://localhost:16686)"
