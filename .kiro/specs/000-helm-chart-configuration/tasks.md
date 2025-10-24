# Implementation Plan

-   [x] 1. Update Chart.yaml with dependencies and metadata

    -   Add Bitnami PostgreSQL dependency with conditional enablement
    -   Update chart metadata (name, description, version)
    -   Set appropriate appVersion
    -   _Requirements: 1.1, 4.1_

-   [x] 2. Create comprehensive values.yaml configuration
-   [x] 2.1 Configure application image and container settings

    -   Set savantly/nexus-command as default image repository
    -   Configure container port 8080 and service mapping
    -   Set resource requests and limits
    -   _Requirements: 4.1, 4.2, 4.3, 4.4_

-   [x] 2.2 Configure embedded PostgreSQL settings

    -   Set up Bitnami PostgreSQL chart configuration
    -   Configure database name, credentials, and persistence
    -   Set appropriate storage size and persistence settings
    -   _Requirements: 1.1, 1.2, 1.4, 1.5_

-   [x] 2.3 Configure external database options

    -   Add external database configuration section
    -   Define connection parameters and secret references
    -   Set up validation for required fields
    -   _Requirements: 2.1, 2.2, 2.3, 2.4_

-   [x] 2.4 Configure OIDC authentication settings

    -   Add OIDC configuration section with all required parameters
    -   Set up secret references for sensitive data
    -   Configure default scope and username attributes
    -   _Requirements: 3.2, 3.3, 3.5_

-   [x] 2.5 Configure application modules and features

    -   Set default enabled state for all core modules
    -   Add configuration for logging levels and encryption
    -   Configure database profiles and schema settings
    -   _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

-   [x] 2.6 Configure Kubernetes service and ingress

    -   Set up ClusterIP service configuration
    -   Add optional ingress configuration with TLS support
    -   Configure service account and security contexts
    -   _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

-   [x] 3. Update deployment template with environment variables
-   [x] 3.1 Configure database environment variables

    -   Add conditional logic for embedded vs external database
    -   Generate appropriate JDBC URL and credentials
    -   Set POSTGRESQL profile when using PostgreSQL
    -   _Requirements: 1.2, 2.2, 2.5_

-   [x] 3.2 Configure OIDC environment variables

    -   Add conditional OIDC configuration based on enabled flag
    -   Reference secrets for sensitive OIDC parameters
    -   Set OAUTH2 profile when OIDC is enabled
    -   _Requirements: 3.1, 3.4_

-   [x] 3.3 Configure application module environment variables

    -   Generate environment variables for each configurable module
    -   Set logging and encryption configuration
    -   Configure database schema auto-creation settings
    -   _Requirements: 5.1, 5.3, 5.5_

-   [x] 4. Create secret templates for sensitive data
-   [x] 4.1 Create database secret template

    -   Generate secret for external database password
    -   Add conditional logic to use existing secrets
    -   Reference secret in deployment environment variables
    -   _Requirements: 2.4_

-   [x] 4.2 Create OIDC secret template

    -   Generate secret for OIDC client credentials
    -   Support existing secret references
    -   Reference secret in deployment environment variables
    -   _Requirements: 3.3_

-   [x] 5. Update service template configuration

    -   Configure service to target application port 8080
    -   Set appropriate service type and port mapping
    -   Add service account configuration
    -   _Requirements: 6.1, 6.2, 6.5_

-   [x] 6. Update ingress template with proper defaults

    -   Add conditional ingress creation based on enabled flag
    -   Configure TLS support and certificate management
    -   Set appropriate path and host configuration
    -   _Requirements: 6.3, 6.4_

-   [x] 7. Configure health checks and probes

    -   Set up liveness and readiness probes for actuator endpoints
    -   Configure appropriate timeouts and intervals
    -   Add startup probe for initial application boot
    -   _Requirements: 4.4_

-   [ ]\* 8. Create validation and testing utilities
-   [ ]\* 8.1 Create configuration validation scripts

    -   Write script to validate database connectivity
    -   Create OIDC provider reachability test
    -   Add application startup verification
    -   _Requirements: All requirements validation_

-   [ ]\* 8.2 Create template rendering tests
    -   Test chart rendering with embedded PostgreSQL
    -   Test chart rendering with external database
    -   Test chart rendering with OIDC enabled/disabled
    -   _Requirements: All requirements verification_
