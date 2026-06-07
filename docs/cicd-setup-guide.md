# 🚀 Guia de Configuração CI/CD: GitHub Actions + VPS (K3s)

Este guia detalha os passos necessários para configurar a infraestrutura na VPS e os segredos no GitHub para que a esteira de CI/CD do AutoMatch funcione perfeitamente.

---

## 1. Configuração da VPS (Servidor)

Recomendamos uma VPS com pelo menos **4GB de RAM** (devido ao SonarQube e Kafka) rodando **Ubuntu 22.04 LTS**.

### 1.1. Instalação do K3s
O K3s é um Kubernetes leve ideal para VPS. Execute o comando abaixo na sua VPS:

```bash
curl -sfL https://get.k3s.io | sh -
```

### 1.2. Liberar Portas no Firewall
Certifique-se de que as seguintes portas estão abertas no painel da sua VPS (Security Groups/Firewall):
- `6443`: API do Kubernetes (necessária para o GitHub Actions conectar).
- `8080`: API Gateway do AutoMatch.
- `9000`: Portal do SonarQube.
- `16686`: Jaeger UI (Opcional).

---

## 2. Preparação do Kubeconfig

O GitHub Actions precisa do arquivo de configuração para "mandar" comandos para a sua VPS.

1. No servidor, leia o conteúdo do arquivo gerado pelo K3s:
   ```bash
   sudo cat /etc/rancher/k3s/k3s.yaml
   ```

2. **IMPORTANTE:** O arquivo virá com o endereço `server: https://127.0.0.1:6443`. 
   Você **DEVE** alterar o `127.0.0.1` pelo **IP PÚBLICO** da sua VPS.

   **Exemplo de como deve ficar:**
   ```yaml
   apiVersion: v1
   clusters:
   - cluster:
       certificate-authority-data: <DADOS_OMITIDOS>
       server: https://203.0.113.42:6443 # <-- IP da sua VPS aqui
     name: default
   ...
   ```

3. Copie todo o conteúdo desse arquivo (já com o IP corrigido).

---

## 3. Configuração de Secrets no GitHub

Vá em seu repositório: **Settings > Secrets and variables > Actions > New repository secret**.

### Lista de Secrets Obrigatórios:

| Nome do Secret | Descrição | Exemplo de Valor |
| :--- | :--- | :--- |
| `KUBECONFIG_STAGING` | Conteúdo do arquivo `k3s.yaml` (IP corrigido) | `apiVersion: v1 ...` |
| `KUBECONFIG_PROD` | Conteúdo do arquivo `k3s.yaml` (pode ser o mesmo para fins acadêmicos) | `apiVersion: v1 ...` |
| `SONAR_TOKEN` | Token gerado no portal do SonarCloud ou sua instância local | `sqa_88f6...` |

---

## 4. Variáveis de Ambiente e Namespaces

A esteira está configurada para separar os ambientes por **Namespaces**. Na primeira execução, o script tentará criar se não existirem, mas você pode garantir executando na VPS:

```bash
kubectl create namespace staging
kubectl create namespace production
```

---

## 5. Troubleshooting (Resolução de Problemas)

### Erro: "Input required and not supplied: kubeconfig"
**Causa:** O Secret no GitHub não foi criado ou o nome está diferente de `KUBECONFIG_STAGING`.
**Solução:** Verifique se não há espaços em branco antes ou depois do nome do Secret no painel do GitHub.

### Erro: "connection refused" no passo de Deploy
**Causa:** O IP no Kubeconfig está como `127.0.0.1` ou a porta `6443` está bloqueada na VPS.
**Solução:** Revise o Passo 1.2 e 2 deste guia.

---
*Documentação gerada automaticamente pela equipe de Engenharia AutoMatch.*
