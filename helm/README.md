# Nexus Command Helm Chart

This directory contains the Helm chart for deploying Nexus Command, a multi-tenant business data management platform built on Apache Causeway framework.

## Overview

The Nexus Command Helm chart provides a complete deployment solution for Kubernetes environments, featuring a modern ConfigMap-based configuration approach:

- **ConfigMap-Based Configuration**: Structured application properties via Kubernetes ConfigMaps
- **Unified Secret Management**: Single Secret for all sensitive configuration
- **Database Integration**: Support for both internal and external PostgreSQL
- **Security Configuration**: OAuth2/OIDC authentication with property interpolation
- **Scalability**: Horizontal Pod Autoscaling and resource management
- **Monitoring**: Health checks and readiness probes
- **Ingress Support**: Configurable ingress for external access

## New Configuration Architecture

This chart uses a modern configuration approach that separates concerns cleanly:

- **applicationConfig**: Non-sensitive Spring Boot configuration stored in ConfigMap
- **secrets**: Sensitive configuration stored in Kubernetes Secret
- **Property Interpolation**: ConfigMap references Secret values using `${VAR}` syntax

## Chart Information

- **Chart Name**: nexus-command
- **Chart Version**: 3.4.3
- **App Version**: latest
- **Chart Type**: Application

## Prerequisites

- Kubernetes 1.19+
- Helm 3.8.0+ (for OCI support)
- Persistent Volume provisioner support in the underlying infrastructure (for PostgreSQL persistence)

## Installation

### Quick Start

```bash
# Add the chart repository (if using OCI registry)
helm pull oci://registry-1.docker.io/savantly/nexus-command-helm --version 3.4.3

# Install with default values
helm install nexus-command ./nexus-command

# Or install directly from OCI registry
helm install nexus-command oci://registry-1.docker.io/savantly/nexus-command-helm --version 3.4.3
```

### Custom Installation

```bash
# Install with custom values
helm install nexus-command ./nexus-command -f custom-values.yaml

# Install in specific namespace
helm install nexus-command ./nexus-command --namespace nexus --create-namespace
```

## Configuration

### Configuration Architecture

The chart uses two main configuration sections:

1. **applicationConfig**: Non-sensitive Spring Boot properties mounted as ConfigMap
2. **secrets**: Sensitive values stored in Kubernetes Secret and referenced via environment variables

### Core Application Settings

| Parameter | Description | Default |
|-----------|-------------|---------|
| `replicaCount` | Number of application replicas | `1` |
| `image.repository` | Container image repository | `savantly/nexus-command` |
| `image.tag` | Container image tag | `""` (uses appVersion) |
| `image.pullPolicy` | Image pull policy | `IfNotPresent` |

### Application Configuration (ConfigMap)

The `applicationConfig` section defines Spring Boot properties that will be mounted as `application-k8s.yml`:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `applicationConfig.nexus.modules.*` | Enable/disable application modules | Various |
| `applicationConfig.nexus.security.oauth2.*` | OAuth2 configuration | See values.yaml |
| `applicationConfig.spring.datasource.*` | Database configuration with interpolation | See values.yaml |
| `applicationConfig.spring.mail.*` | Mail server configuration | See values.yaml |
| `applicationConfig.server.*` | Server configuration | See values.yaml |
| `applicationConfig.management.*` | Actuator endpoints configuration | See values.yaml |

### Secrets Configuration

The `secrets` section defines sensitive values stored in Kubernetes Secret:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `secrets.database.url` | Database connection URL | Auto-generated |
| `secrets.database.username` | Database username | Auto-generated |
| `secrets.database.password` | Database password | Required for external DB |
| `secrets.oauth2.*` | OAuth2/OIDC credentials | `""` |
| `secrets.encryption.secret` | Application encryption key | `change-this-32-character-secret!` |
| `secrets.mail.*` | Mail server credentials | See values.yaml |
| `secrets.ai.openaiApiKey` | OpenAI API key | `""` |
| `secrets.custom` | Custom environment variables | `{}` |

### Service Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `service.type` | Kubernetes service type | `ClusterIP` |
| `service.port` | Service port | `8080` |
| `service.targetPort` | Container target port | `8080` |

### Ingress Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `ingress.enabled` | Enable ingress | `false` |
| `ingress.className` | Ingress class name | `nginx` |
| `ingress.hosts[0].host` | Hostname | `nexus-command.local` |
| `ingress.tls` | TLS configuration | See values.yaml |

### Database Configuration

The chart supports both internal PostgreSQL (via Bitnami chart) and external PostgreSQL configurations.

#### Internal PostgreSQL (Default)

| Parameter | Description | Default |
|-----------|-------------|---------|
| `postgresql.enabled` | Enable internal PostgreSQL | `true` |
| `postgresql.auth.postgresPassword` | PostgreSQL admin password | `nexus-admin` |
| `postgresql.auth.username` | Application database user | `nexus` |
| `postgresql.auth.password` | Application database password | `nexus-password` |
| `postgresql.auth.database` | Application database name | `nexus` |

#### External PostgreSQL

| Parameter | Description | Default |
|-----------|-------------|---------|
| `postgresql.enabled` | Set to false for external database | `true` |
| `externalDatabase.type` | Database type (currently postgresql only) | `postgresql` |
| `externalDatabase.host` | External database hostname | `""` |
| `externalDatabase.port` | External database port | `5432` |
| `externalDatabase.database` | Database name | `nexus` |
| `externalDatabase.username` | Database username | `nexus` |
| `externalDatabase.jdbcParams` | Additional JDBC parameters | `""` |
| `externalDatabase.existingSecret` | Use existing secret for password | `""` |
| `externalDatabase.existingSecretPasswordKey` | Key in existing secret | `password` |
| `secrets.database.password` | Database password (if not using existing secret) | `""` |

**Note**: Database URL and credentials are automatically generated based on the configuration. For external databases, set the password in `secrets.database.password` or use `externalDatabase.existingSecret`.

### OAuth2/OIDC Authentication

OAuth2/OIDC configuration is now managed through the applicationConfig and secrets sections:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `applicationConfig.nexus.security.oauth2.enabled` | Enable OAuth2 authentication | `false` |
| `applicationConfig.nexus.security.oauth2.login.enabled` | Enable OAuth2 login | `false` |
| `secrets.oauth2.clientId` | OAuth2 client ID | `""` |
| `secrets.oauth2.clientSecret` | OAuth2 client secret | `""` |
| `secrets.oauth2.issuerUri` | OAuth2 issuer URI | `""` |
| `secrets.oauth2.authorizationUri` | OAuth2 authorization endpoint | `""` |
| `secrets.oauth2.tokenUri` | OAuth2 token endpoint | `""` |
| `secrets.oauth2.userInfoUri` | OAuth2 user info endpoint | `""` |
| `secrets.oauth2.jwkSetUri` | OAuth2 JWK set endpoint | `""` |

### Module Configuration

Application modules are configured in the `applicationConfig.nexus.modules` section:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `applicationConfig.nexus.modules.organizations.enabled` | Enable organizations module | `true` |
| `applicationConfig.nexus.modules.projects.enabled` | Enable projects module | `true` |
| `applicationConfig.nexus.modules.franchise.enabled` | Enable franchise module | `true` |
| `applicationConfig.nexus.modules.web.enabled` | Enable web module | `false` |
| `applicationConfig.nexus.modules.security.enabled` | Enable security module | `true` |
| `applicationConfig.nexus.modules.ai.enabled` | Enable AI module | `true` |
| `applicationConfig.nexus.modules.flow.enabled` | Enable flow module | `true` |
| `applicationConfig.nexus.modules.kafka.enabled` | Enable Kafka module | `false` |
| `applicationConfig.nexus.modules.webhooks.enabled` | Enable webhooks module | `true` |

### Resource Management

| Parameter | Description | Default |
|-----------|-------------|---------|
| `resources.requests.cpu` | CPU request | `500m` |
| `resources.requests.memory` | Memory request | `1Gi` |
| `resources.limits.cpu` | CPU limit | `1000m` |
| `resources.limits.memory` | Memory limit | `2Gi` |

### Autoscaling

| Parameter | Description | Default |
|-----------|-------------|---------|
| `autoscaling.enabled` | Enable HPA | `false` |
| `autoscaling.minReplicas` | Minimum replicas | `1` |
| `autoscaling.maxReplicas` | Maximum replicas | `100` |
| `autoscaling.targetCPUUtilizationPercentage` | Target CPU utilization | `80` |

### Encryption Configuration

Application encryption is configured through the secrets section:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `secrets.encryption.secret` | Encryption secret (must be 32 characters for AES-256) | `change-this-32-character-secret!` |

**Important**: The encryption secret must be exactly 32 characters long for AES-256 encryption.

### Property Interpolation

The chart uses Spring Boot property interpolation to reference sensitive values from environment variables within the ConfigMap:

```yaml
# In applicationConfig (ConfigMap)
applicationConfig:
  spring:
    datasource:
      url: "${SPRING_DATASOURCE_URL}"           # References environment variable
      username: "${SPRING_DATASOURCE_USERNAME}" # References environment variable
      password: "${SPRING_DATASOURCE_PASSWORD}" # References environment variable
    mail:
      host: "${SPRING_MAIL_HOST:localhost}"     # With default value
      port: "${SPRING_MAIL_PORT:25}"           # Numeric with default

# In secrets (Kubernetes Secret -> Environment Variables)
secrets:
  database:
    url: "jdbc:postgresql://postgres.example.com:5432/nexus"
    username: "nexus"
    password: "secure-password"
  mail:
    host: "smtp.example.com"
    port: 587
```

**Interpolation Patterns:**
- `${VAR_NAME}` - Basic variable reference
- `${VAR_NAME:default}` - Variable with default value
- `${VAR_NAME:}` - Variable with empty default

### Custom Environment Variables

| Parameter | Description | Default |
|-----------|-------------|---------|
| `extraEnvVars` | Additional environment variables as key-value pairs | `{}` |
| `extraEnvVarsSecret` | Additional environment variables from secrets | `[]` |
| `extraEnvVarsConfigMap` | Additional environment variables from config maps | `[]` |
| `secrets.custom` | Custom sensitive environment variables | `{}` |

## Migration Guide

### Migrating from Previous Versions

If you're upgrading from a previous version that used the old configuration format, here's how to migrate:

#### Old Configuration (v3.x and earlier)
```yaml
# OLD FORMAT - No longer supported
externalDatabase:
  enabled: true
  host: "postgres.example.com"
  password: "db-password"

oidc:
  enabled: true
  clientId: "nexus"
  clientSecret: "oauth-secret"

application:
  modules:
    ai:
      enabled: true
  encryption:
    secret: "encryption-key"
```

#### New Configuration (v4.0+)
```yaml
# NEW FORMAT - ConfigMap + Secrets approach
postgresql:
  enabled: false

externalDatabase:
  host: "postgres.example.com"
  database: "nexus"
  username: "nexus"

applicationConfig:
  nexus:
    modules:
      ai:
        enabled: true
    security:
      oauth2:
        enabled: true
        login:
          enabled: true

secrets:
  database:
    password: "db-password"
  oauth2:
    clientId: "nexus"
    clientSecret: "oauth-secret"
  encryption:
    secret: "32-character-encryption-key-here!"
```

#### Migration Steps

1. **Update values.yaml structure**: Move configuration to `applicationConfig` and `secrets` sections
2. **Update encryption secret**: Ensure it's exactly 32 characters for AES-256
3. **Review property interpolation**: Sensitive values now use `${VAR}` syntax in applicationConfig
4. **Test configuration**: Use `helm template` to verify the generated ConfigMap and Secret
5. **Deploy incrementally**: Test in development environment first

## Usage Examples

### Basic Deployment

```yaml
# values.yaml
ingress:
  enabled: true
  hosts:
    - host: nexus.example.com
      paths:
        - path: /
          pathType: Prefix

# Use internal PostgreSQL with secure passwords
postgresql:
  auth:
    postgresPassword: "secure-admin-password"
    password: "secure-app-password"

# Configure application encryption
secrets:
  encryption:
    secret: "your-32-character-encryption-key!"
```

### Production Deployment with External Database

```yaml
# production-values.yaml
replicaCount: 3

# Disable internal PostgreSQL
postgresql:
  enabled: false

# Configure external PostgreSQL
externalDatabase:
  host: "postgres.example.com"
  port: 5432
  database: "nexus_prod"
  username: "nexus_user"
  jdbcParams: "?sslmode=require&connectTimeout=30"

# Production secrets configuration
secrets:
  database:
    password: "secure-external-db-password"
  
  oauth2:
    clientId: "nexus-command-prod"
    clientSecret: "oauth-client-secret"
    issuerUri: "https://auth.example.com/realms/nexus"
    authorizationUri: "https://auth.example.com/realms/nexus/protocol/openid-connect/auth"
    tokenUri: "https://auth.example.com/realms/nexus/protocol/openid-connect/token"
    userInfoUri: "https://auth.example.com/realms/nexus/protocol/openid-connect/userinfo"
    jwkSetUri: "https://auth.example.com/realms/nexus/protocol/openid-connect/certs"
  
  encryption:
    secret: "production-32-char-encryption-key"
  
  mail:
    host: "smtp.example.com"
    port: 587
    username: "nexus@example.com"
    password: "smtp-password"
    from: "nexus@example.com"

# Enable OAuth2 authentication
applicationConfig:
  nexus:
    security:
      oauth2:
        enabled: true
        login:
          enabled: true
          name: "Company SSO"

# Production resources
resources:
  requests:
    cpu: 1000m
    memory: 2Gi
  limits:
    cpu: 2000m
    memory: 4Gi

autoscaling:
  enabled: true
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 70
```

### Development Environment

```yaml
# dev-values.yaml
ingress:
  enabled: true
  hosts:
    - host: nexus-dev.local
      paths:
        - path: /
          pathType: Prefix

# Development PostgreSQL (no persistence)
postgresql:
  primary:
    persistence:
      enabled: false

# Development application configuration
applicationConfig:
  spring:
    jpa:
      show-sql: true
  
  # Development logging
  nexus:
    logging:
      level:
        root: "INFO"
        net.savantly.nexus: "DEBUG"
        org.springframework: "INFO"

# Development secrets (use secure values in real environments)
secrets:
  encryption:
    secret: "dev-32-character-encryption-key!"
```

### External Database with Existing Secret

```yaml
# external-db-values.yaml
postgresql:
  enabled: false

externalDatabase:
  host: "postgres.example.com"
  database: "nexus"
  username: "nexus"
  existingSecret: "nexus-db-credentials"
  existingSecretPasswordKey: "password"

# Create the secret separately:
# kubectl create secret generic nexus-db-credentials \
#   --from-literal=password=your-secure-password
```

### AI-Enabled Configuration

```yaml
# ai-values.yaml
# Enable AI modules
applicationConfig:
  nexus:
    modules:
      ai: 
        enabled: true
    ai:
      enabled: true
      openai:
        model: "gpt-4"
        max-tokens: 2000
        temperature: 0.7

# AI service credentials
secrets:
  ai:
    openaiApiKey: "sk-your-openai-api-key"
```

### Custom Environment Variables

```yaml
# custom-env-values.yaml
# Simple key-value environment variables
extraEnvVars:
  JAVA_OPTS: "-Xmx2g -Xms1g -XX:+UseG1GC"
  CUSTOM_FEATURE_FLAG: "true"
  API_TIMEOUT: "30000"
  LOG_FORMAT: "json"

# Environment variables from secrets
extraEnvVarsSecret:
  - name: DATABASE_PASSWORD
    secretName: database-credentials
    secretKey: password
  - name: API_KEY
    secretName: external-api-secret
    secretKey: api-key
  - name: ENCRYPTION_KEY
    secretName: encryption-secret
    secretKey: key

# Environment variables from config maps
extraEnvVarsConfigMap:
  - name: FEATURE_TOGGLES
    configMapName: feature-config
    configMapKey: toggles
  - name: CACHE_CONFIG
    configMapName: cache-settings
    configMapKey: redis-config
```

## Health Checks

The chart includes comprehensive health checks:

- **Startup Probe**: `/actuator/health` - Ensures application starts properly
- **Liveness Probe**: `/actuator/health/liveness` - Detects application failures
- **Readiness Probe**: `/actuator/health/readiness` - Determines traffic readiness

## Security

### Pod Security Context

- Runs as non-root user (UID 1000)
- Drops all capabilities
- Uses read-only root filesystem where possible
- Sets fsGroup for volume permissions

### Network Policies

Consider implementing network policies to restrict traffic between pods and external services.

### Data Encryption

Nexus Command includes built-in encryption capabilities for sensitive data using the AttributeEncryptor:

- **AES Encryption**: Uses AES algorithm for encrypting sensitive database fields
- **Key Requirements**: Encryption keys must be exactly 128 bits (16 characters) or 256 bits (32 characters)
- **Automatic Encryption**: Sensitive fields are automatically encrypted/decrypted when stored/retrieved
- **Secret Management**: Use Kubernetes secrets to securely store encryption keys

### Secrets Management

Use Kubernetes secrets for sensitive data:

```bash
# Create database secret
kubectl create secret generic nexus-db-secret \
  --from-literal=password=your-secure-password

# Create OIDC secret
kubectl create secret generic nexus-oidc-secret \
  --from-literal=client-id=your-client-id \
  --from-literal=client-secret=your-client-secret

# Create encryption secret
kubectl create secret generic nexus-encryption-secret \
  --from-literal=secret=your-encryption-key
```

#### Encryption Secret Generation

The AttributeEncryptor requires a secret that is either **128 bits (16 bytes)** or **256 bits (32 bytes)** long. Here are several ways to generate a secure encryption key:

**Generate 128-bit (16 byte) key:**
```bash
# Using openssl
openssl rand -hex 16

# Using base64 (16 random bytes)
openssl rand -base64 16 | cut -c1-16

# Using /dev/urandom
head -c 16 /dev/urandom | base64 | cut -c1-16
```

**Generate 256-bit (32 byte) key:**
```bash
# Using openssl
openssl rand -hex 32

# Using base64 (32 random bytes)  
openssl rand -base64 32 | cut -c1-32

# Using /dev/urandom
head -c 32 /dev/urandom | base64 | cut -c1-32
```

**Configure in values.yaml:**

Option 1 - Direct secret in values (not recommended for production):
```yaml
application:
  encryption:
    enabled: true
    secret: "your-16-or-32-char-key"  # Must be exactly 16 or 32 characters
```

Option 2 - Use existing Kubernetes secret (recommended):
```yaml
application:
  encryption:
    enabled: true
    existingSecret: "nexus-encryption-secret"
    existingSecretKey: "secret"
```

**Create the secret:**
```bash
# Generate a 256-bit key and create secret
ENCRYPTION_KEY=$(openssl rand -hex 32 | cut -c1-32)
kubectl create secret generic nexus-encryption-secret \
  --from-literal=secret="$ENCRYPTION_KEY"
```

## Troubleshooting

### Common Issues

1. **Application won't start**
   - Check ConfigMap exists: `kubectl get configmap -l app.kubernetes.io/name=nexus-command`
   - Verify Secret exists: `kubectl get secret -l app.kubernetes.io/name=nexus-command`
   - Check database connectivity
   - Verify resource limits
   - Review application logs: `kubectl logs -l app.kubernetes.io/name=nexus-command`

2. **ConfigMap/Secret issues**
   - Verify ConfigMap is mounted: `kubectl describe pod -l app.kubernetes.io/name=nexus-command`
   - Check Secret environment variables: `kubectl exec deployment/nexus-command -- env | grep SPRING`
   - Validate YAML syntax in applicationConfig section
   - Ensure property interpolation syntax is correct: `${VAR_NAME}`

3. **Database connection issues**
   - Check generated database URL: `kubectl get secret nexus-command-env -o yaml | base64 -d`
   - Verify PostgreSQL is running: `kubectl get pods -l app.kubernetes.io/name=postgresql`
   - For external databases, verify host connectivity
   - Check database credentials in secrets section

4. **OAuth2/OIDC authentication problems**
   - Verify OAuth2 configuration in applicationConfig section
   - Check OAuth2 credentials in secrets section
   - Validate issuer URI accessibility from cluster
   - Ensure all required OAuth2 endpoints are configured

5. **Property interpolation issues**
   - Check environment variables are set: `kubectl exec deployment/nexus-command -- env`
   - Verify interpolation syntax: `${VAR_NAME}` or `${VAR_NAME:default}`
   - Ensure referenced environment variables exist in Secret
   - Check for typos in variable names

6. **Encryption configuration issues**
   - Ensure encryption secret is exactly 32 characters long
   - Verify secret exists: `kubectl get secret nexus-command-env`
   - Check application logs for encryption errors
   - Validate secret is properly base64 encoded in Secret

### Debugging Commands

```bash
# Check pod status
kubectl get pods -l app.kubernetes.io/name=nexus-command

# View application logs
kubectl logs -l app.kubernetes.io/name=nexus-command -f

# Check ConfigMap content
kubectl get configmap nexus-command-config -o yaml

# Check Secret content (base64 encoded)
kubectl get secret nexus-command-env -o yaml

# Decode Secret values
kubectl get secret nexus-command-env -o jsonpath='{.data.SPRING_DATASOURCE_URL}' | base64 -d

# Check environment variables in container
kubectl exec deployment/nexus-command -- env | grep -E "(SPRING|NEXUS|OAUTH2)"

# Verify ConfigMap mount
kubectl exec deployment/nexus-command -- ls -la /config/

# Check application-k8s.yml content
kubectl exec deployment/nexus-command -- cat /config/application-k8s.yml

# Describe pod for events and mounts
kubectl describe pod -l app.kubernetes.io/name=nexus-command

# Test database connectivity
kubectl exec -it deployment/nexus-command -- nc -zv postgresql 5432

# Check Spring profiles
kubectl exec deployment/nexus-command -- env | grep SPRING_PROFILES_ACTIVE

# Validate Helm template output
helm template nexus-command ./nexus-command --debug
```

## Upgrading

```bash
# Upgrade to new version
helm upgrade nexus-command ./nexus-command --version 3.4.3

# Upgrade with new values
helm upgrade nexus-command ./nexus-command -f updated-values.yaml

# Rollback if needed
helm rollback nexus-command 1
```

## Uninstalling

```bash
# Uninstall the release
helm uninstall nexus-command

# Remove persistent volumes (if needed)
kubectl delete pvc -l app.kubernetes.io/instance=nexus-command
```

## Development

### Local Development

For local development with the chart:

```bash
# Lint the chart
helm lint ./nexus-command

# Template and review output
helm template nexus-command ./nexus-command

# Dry run installation
helm install nexus-command ./nexus-command --dry-run --debug
```

### Chart Dependencies

Update chart dependencies:

```bash
cd nexus-command
helm dependency update
```

## Support

For issues and questions:

- **Documentation**: Check the main project documentation
- **Issues**: Report issues in the project repository
- **Community**: Join the project community channels

## License

This chart is licensed under the same terms as the Nexus Command project.