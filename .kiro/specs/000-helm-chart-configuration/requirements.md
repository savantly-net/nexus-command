# Requirements Document

## Introduction

This specification defines the requirements for configuring the Nexus Command Helm chart with comprehensive default values that support both deployed and external PostgreSQL instances, optional OIDC configuration, and proper application settings based on the existing Docker Compose configurations.

## Glossary

- **Helm_Chart**: The Kubernetes deployment package for Nexus Command application
- **Bitnami_PostgreSQL**: The official Bitnami PostgreSQL Helm chart used as a dependency
- **OIDC_Provider**: OpenID Connect authentication provider for user authentication
- **External_Database**: PostgreSQL database instance managed outside of the Kubernetes cluster
- **Deployed_Database**: PostgreSQL database instance deployed within the Kubernetes cluster using Bitnami chart
- **Application_Configuration**: Environment variables and settings for the Nexus Command application

## Requirements

### Requirement 1

**User Story:** As a DevOps engineer, I want to deploy Nexus Command with a bundled PostgreSQL database, so that I can have a complete working system without external dependencies.

#### Acceptance Criteria

1. WHEN deploying with default values, THE Helm_Chart SHALL include the Bitnami PostgreSQL chart as a dependency
2. WHEN PostgreSQL is deployed, THE Helm_Chart SHALL configure the application to connect to the deployed database
3. WHEN PostgreSQL is deployed, THE Helm_Chart SHALL create appropriate database credentials and connection strings
4. THE Helm_Chart SHALL set the PostgreSQL database name to "nexus" by default
5. THE Helm_Chart SHALL configure persistent storage for the PostgreSQL data

### Requirement 2

**User Story:** As a DevOps engineer, I want to connect Nexus Command to an external PostgreSQL database, so that I can use my existing database infrastructure.

#### Acceptance Criteria

1. WHEN external database is configured, THE Helm_Chart SHALL disable the bundled PostgreSQL deployment
2. WHEN external database is configured, THE Helm_Chart SHALL accept external database connection parameters
3. THE Helm_Chart SHALL validate that required database connection parameters are provided for external databases
4. THE Helm_Chart SHALL support configuring database credentials via Kubernetes secrets
5. THE Helm_Chart SHALL configure the POSTGRESQL profile when using PostgreSQL databases

### Requirement 3

**User Story:** As a system administrator, I want to optionally configure OIDC authentication, so that I can integrate with my organization's identity provider.

#### Acceptance Criteria

1. WHEN OIDC is disabled, THE Helm_Chart SHALL configure the application without OAuth2 authentication
2. WHEN OIDC is enabled, THE Helm_Chart SHALL configure all required OAuth2 client parameters
3. THE Helm_Chart SHALL support configuring OIDC credentials via Kubernetes secrets
4. WHEN OIDC is enabled, THE Helm_Chart SHALL set the OAUTH2 profile active
5. THE Helm_Chart SHALL provide sensible defaults for OIDC scope and username attributes

### Requirement 4

**User Story:** As a developer, I want the Helm chart to use the correct Nexus Command container image, so that the application deploys successfully.

#### Acceptance Criteria

1. THE Helm_Chart SHALL use "savantly/nexus-command" as the default container image repository
2. THE Helm_Chart SHALL use "latest" as the default image tag
3. THE Helm_Chart SHALL configure the container to expose port 8080
4. THE Helm_Chart SHALL configure appropriate health check endpoints
5. THE Helm_Chart SHALL set reasonable resource requests and limits

### Requirement 5

**User Story:** As a DevOps engineer, I want to configure application modules and features, so that I can enable only the functionality I need.

#### Acceptance Criteria

1. THE Helm_Chart SHALL enable all core modules by default (organizations, projects, franchise, web, security, flow)
2. THE Helm_Chart SHALL allow individual modules to be disabled via configuration
3. THE Helm_Chart SHALL configure logging levels appropriately for production use
4. THE Helm_Chart SHALL set the encryption secret via a configurable parameter
5. THE Helm_Chart SHALL configure database schema auto-creation for all enabled modules

### Requirement 6

**User Story:** As a Kubernetes administrator, I want proper service and ingress configuration, so that the application is accessible within my cluster.

#### Acceptance Criteria

1. THE Helm_Chart SHALL create a ClusterIP service by default
2. THE Helm_Chart SHALL configure the service to target port 8080 on the application pods
3. THE Helm_Chart SHALL provide optional ingress configuration with sensible defaults
4. THE Helm_Chart SHALL support TLS configuration for ingress
5. THE Helm_Chart SHALL configure appropriate service account and security contexts