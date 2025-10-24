# Nexus Command Helm Chart

This directory contains the Helm chart for deploying Nexus Command, a multi-tenant business data management platform built on Apache Causeway framework.

## Overview

The Nexus Command Helm chart provides a complete deployment solution for Kubernetes environments, including:

- **Application Deployment**: Containerized Nexus Command application
- **Database Integration**: PostgreSQL database with configurable persistence
- **Security Configuration**: OIDC authentication and security policies
- **Scalability**: Horizontal Pod Autoscaling and resource management
- **Monitoring**: Health checks and readiness probes
- **Ingress Support**: Configurable ingress for external access

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

### Core Application Settings

| Parameter | Description | Default |
|-----------|-------------|---------|
| `replicaCount` | Number of application replicas | `1` |
| `image.repository` | Container image repository | `savantly/nexus-command` |
| `image.tag` | Container image tag | `""` (uses appVersion) |
| `image.pullPolicy` | Image pull policy | `IfNotPresent` |

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

#### Internal PostgreSQL (Default)

| Parameter | Description | Default |
|-----------|-------------|---------|
| `postgresql.enabled` | Enable internal PostgreSQL | `true` |
| `postgresql.auth.postgresPassword` | PostgreSQL admin password | `nexus-admin` |
| `postgresql.auth.username` | Application database user | `nexus` |
| `postgresql.auth.password` | Application database password | `nexus-password` |
| `postgresql.auth.database` | Application database name | `nexus` |

#### External Database

| Parameter | Description | Default |
|-----------|-------------|---------|
| `externalDatabase.enabled` | Use external database | `false` |
| `externalDatabase.type` | Database type (postgresql/sqlserver/h2) | `postgresql` |
| `externalDatabase.host` | Database host | `""` |
| `externalDatabase.port` | Database port | `5432` |
| `externalDatabase.database` | Database name | `nexus` |
| `externalDatabase.username` | Database username | `nexus` |
| `externalDatabase.password` | Database password | `""` |

### OIDC Authentication

| Parameter | Description | Default |
|-----------|-------------|---------|
| `oidc.enabled` | Enable OIDC authentication | `false` |
| `oidc.issuerUri` | OIDC issuer URI | `""` |
| `oidc.clientId` | OIDC client ID | `""` |
| `oidc.clientSecret` | OIDC client secret | `""` |
| `oidc.scope` | OIDC scope | `openid profile email` |

### Module Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `application.modules.organizations.enabled` | Enable organizations module | `true` |
| `application.modules.projects.enabled` | Enable projects module | `true` |
| `application.modules.franchise.enabled` | Enable franchise module | `true` |
| `application.modules.web.enabled` | Enable web module | `true` |
| `application.modules.security.enabled` | Enable security module | `true` |
| `application.modules.ai.enabled` | Enable AI module | `true` |
| `application.modules.flow.enabled` | Enable flow module | `true` |
| `application.modules.kafka.enabled` | Enable Kafka module | `false` |
| `application.modules.webhooks.enabled` | Enable webhooks module | `true` |

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

postgresql:
  auth:
    postgresPassword: "secure-admin-password"
    password: "secure-app-password"
```

### Production Deployment with External Database

```yaml
# production-values.yaml
replicaCount: 3

postgresql:
  enabled: false

externalDatabase:
  enabled: true
  host: "postgres.example.com"
  port: 5432
  database: "nexus_prod"
  username: "nexus_user"
  existingSecret: "nexus-db-secret"
  existingSecretPasswordKey: "password"

oidc:
  enabled: true
  issuerUri: "https://auth.example.com/realms/nexus"
  clientId: "nexus-command"
  existingSecret: "nexus-oidc-secret"

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

postgresql:
  primary:
    persistence:
      enabled: false  # Use emptyDir for development

application:
  database:
    showSql: true
  logging:
    level:
      nexus: "DEBUG"
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
```

## Troubleshooting

### Common Issues

1. **Application won't start**
   - Check database connectivity
   - Verify resource limits
   - Review application logs: `kubectl logs -l app.kubernetes.io/name=nexus-command`

2. **Database connection issues**
   - Verify PostgreSQL is running: `kubectl get pods -l app.kubernetes.io/name=postgresql`
   - Check database credentials in secrets
   - Ensure network policies allow database access

3. **OIDC authentication problems**
   - Verify OIDC configuration values
   - Check issuer URI accessibility
   - Validate client credentials

### Debugging Commands

```bash
# Check pod status
kubectl get pods -l app.kubernetes.io/name=nexus-command

# View application logs
kubectl logs -l app.kubernetes.io/name=nexus-command -f

# Check service endpoints
kubectl get endpoints nexus-command

# Describe pod for events
kubectl describe pod -l app.kubernetes.io/name=nexus-command

# Test database connectivity
kubectl exec -it deployment/nexus-command -- nc -zv postgresql 5432
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