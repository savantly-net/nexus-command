# Project Structure

## Root Level Organization
```
nexus-command/
├── module-*/                    # Feature modules (see below)
├── webapp/                      # Main web application
├── docker/                      # Docker configuration
├── docs/                        # Documentation and images
├── postgres-data/               # Local PostgreSQL data
├── lib/                         # External libraries (spring-instrument.jar)
├── pom.xml                      # Root Maven configuration
├── Makefile                     # Build and deployment commands
└── docker-compose*.yml          # Container orchestration configs
```

## Module Architecture
Each `module-*` follows consistent structure:
```
module-{name}/
├── src/main/java/net/savantly/nexus/{name}/
│   ├── api/                     # REST API controllers
│   ├── dom/                     # Domain objects and services
│   ├── fixture/                 # Test data fixtures
│   └── {Name}Module.java        # Module configuration
├── src/test/                    # Unit and integration tests
└── pom.xml                      # Module dependencies
```

## Core Modules
- **module-common-types**: Shared types and utilities
- **module-audited-entity**: Base auditing functionality
- **module-encryption**: Security and encryption services
- **module-security**: Authentication and authorization
- **module-organizations**: Multi-tenant organization management
- **module-projects**: Project and task management
- **module-franchise**: Franchise-specific functionality
- **module-web**: Web content management
- **module-ai**: AI integration services
- **module-flow**: Workflow management
- **module-kafka**: Event streaming
- **module-webhooks**: External integrations

## Webapp Structure
```
webapp/
├── src/main/java/               # Application entry point
├── src/main/resources/
│   ├── application.yml          # Main configuration
│   └── application-{profile}.properties  # Profile-specific configs
├── src/test/                    # Integration tests
└── pom.xml                      # Webapp dependencies (includes all modules)
```

## Package Naming Convention
- Root package: `net.savantly.nexus`
- Module packages: `net.savantly.nexus.{module-name}`
- API controllers: `{module}.api`
- Domain objects: `{module}.dom`
- Test fixtures: `{module}.fixture`

## Configuration Patterns
- **Module Configuration**: Each module has a `{Name}Module.java` class
- **Profile-based**: Different configs for development, production, database types
- **Environment Variables**: Docker and deployment configuration
- **Feature Flags**: Modules can be enabled/disabled via properties

## Testing Structure
- **Unit Tests**: In each module's `src/test/java`
- **Integration Tests**: In `webapp/src/test` and `module-*-tests`
- **Fixture Data**: Organized by module in `fixture/` packages
- **Test Profiles**: Separate configurations for testing

## Build Artifacts
- **JAR Modules**: Each module builds to a JAR
- **Executable JAR**: Webapp builds to Spring Boot executable JAR
- **Docker Image**: Multi-stage build creates optimized container
- **Maven Central**: Modules published for external consumption