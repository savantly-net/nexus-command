# Design Document

## Overview

This design outlines the configuration of the Nexus Command Helm chart to provide a production-ready deployment with flexible database and authentication options. The chart will support both embedded PostgreSQL (via Bitnami chart) and external database configurations, along with optional OIDC authentication.

## Architecture

### Chart Dependencies

The Helm chart will include the Bitnami PostgreSQL chart as a conditional dependency:

```yaml
dependencies:
  - name: postgresql
    version: "~16.7.27"
    repository: "https://charts.bitnami.com/bitnami"
    condition: postgresql.enabled
```

### Configuration Structure

The values.yaml will be organized into logical sections:
- Application configuration (image, resources, modules)
- Database configuration (embedded vs external)
- Authentication configuration (OIDC settings)
- Kubernetes resources (service, ingress, security)

## Components and Interfaces

### 1. Application Container Configuration

**Image Configuration:**
- Repository: `savantly/nexus-command`
- Tag: `latest` (configurable)
- Pull policy: `IfNotPresent`

**Port Configuration:**
- Container port: 8080
- Service port: 80 (mapped to container 8080)

**Health Checks:**
- Liveness probe: HTTP GET `/actuator/health`
- Readiness probe: HTTP GET `/actuator/health`
- Initial delay: 60 seconds
- Period: 30 seconds

### 2. Database Configuration

**Embedded PostgreSQL (Default):**
```yaml
postgresql:
  enabled: true
  auth:
    postgresPassword: "nexus-admin"
    username: "nexus"
    password: "nexus"
    database: "nexus"
  primary:
    persistence:
      enabled: true
      size: 8Gi
```

**External Database:**
```yaml
externalDatabase:
  enabled: false
  host: ""
  port: 5432
  database: "nexus"
  username: "nexus"
  password: ""
  existingSecret: ""
  existingSecretPasswordKey: "password"
```

### 3. Authentication Configuration

**OIDC Configuration:**
```yaml
auth:
  oidc:
    enabled: false
    clientId: ""
    clientSecret: ""
    issuerUri: ""
    scope: "openid profile email"
    usernameAttribute: "preferred_username"
    existingSecret: ""
    existingSecretClientIdKey: "client-id"
    existingSecretClientSecretKey: "client-secret"
```

### 4. Application Modules Configuration

```yaml
modules:
  organizations:
    enabled: true
  projects:
    enabled: true
  franchise:
    enabled: true
  web:
    enabled: true
  orgWeb:
    enabled: true
  security:
    enabled: true
  flow:
    enabled: true
  ai:
    enabled: true
  kafka:
    enabled: true
  webhooks:
    enabled: true
```

## Data Models

### Environment Variables Template

The deployment template will generate environment variables based on configuration:

**Database Variables:**
- `SPRING_PROFILES_ACTIVE`: Set to "POSTGRESQL" when using PostgreSQL
- `SPRING_DATASOURCE_URL`: Generated from database configuration
- `SPRING_DATASOURCE_USERNAME`: From database configuration
- `SPRING_DATASOURCE_PASSWORD`: From secret or direct value

**Authentication Variables:**
- `OAUTH2_CLIENT_ID`: From OIDC configuration
- `OAUTH2_CLIENT_SECRET`: From secret
- `OAUTH2_CLIENT_ISSUER_URI`: From OIDC configuration

**Module Variables:**
- `nexus.modules.{module}.enabled`: For each configurable module

### Secret Management

**Database Secret (when using external database):**
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: {{ include "nexus-command.fullname" . }}-db
data:
  password: {{ .Values.externalDatabase.password | b64enc }}
```

**OIDC Secret (when using OIDC):**
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: {{ include "nexus-command.fullname" . }}-oidc
data:
  client-id: {{ .Values.auth.oidc.clientId | b64enc }}
  client-secret: {{ .Values.auth.oidc.clientSecret | b64enc }}
```

## Error Handling

### Configuration Validation

**Template Validation:**
- Fail deployment if external database is enabled but required fields are missing
- Validate that either embedded or external database is configured
- Ensure OIDC required fields are present when OIDC is enabled

**Runtime Error Handling:**
- Configure application logging levels for production
- Set up proper health check timeouts
- Configure graceful shutdown periods

### Fallback Strategies

- Default to embedded PostgreSQL if no database configuration is provided
- Disable OIDC authentication if configuration is incomplete
- Use sensible defaults for all optional configuration values

## Testing Strategy

### Configuration Testing

**Template Rendering Tests:**
- Test with embedded PostgreSQL enabled
- Test with external database configuration
- Test with OIDC enabled and disabled
- Test with various module combinations

**Integration Testing:**
- Deploy with default values in test cluster
- Verify database connectivity
- Test OIDC authentication flow (if configured)
- Validate all enabled modules are functional

### Validation Scripts

Create helper scripts to validate configuration:
- Database connectivity test
- OIDC provider reachability test
- Application startup verification

## Implementation Notes

### Chart.yaml Updates

```yaml
apiVersion: v2
name: nexus-command
description: A Helm chart for Nexus Command application
type: application
version: 0.1.0
appVersion: "latest"
dependencies:
  - name: postgresql
    version: "~16.7.27"
    repository: "https://charts.bitnami.com/bitnami"
    condition: postgresql.enabled
```

### Template Organization

- `deployment.yaml`: Main application deployment with environment variables
- `service.yaml`: Service configuration
- `ingress.yaml`: Optional ingress configuration
- `secrets.yaml`: Database and OIDC secrets
- `configmap.yaml`: Application configuration
- `_helpers.tpl`: Template helper functions

### Default Resource Allocation

```yaml
resources:
  limits:
    cpu: 1000m
    memory: 2Gi
  requests:
    cpu: 500m
    memory: 1Gi
```

This design ensures a flexible, production-ready Helm chart that can accommodate various deployment scenarios while maintaining security best practices and operational simplicity.