# Technology Stack

## Core Framework
- **Apache Causeway 3.0.0**: Primary application framework for rapid development
- **Spring Boot**: Application container and dependency injection
- **Java 17**: Programming language (LTS version)
- **Maven**: Build system and dependency management

## Persistence & Database
- **EclipseLink JPA**: ORM with load-time weaving
- **PostgreSQL**: Primary database (default profile)
- **H2**: In-memory database for development/testing
- **Flyway**: Database migration management

## Web & UI
- **Apache Wicket**: Web UI framework via Causeway viewer
- **RESTful Objects**: REST API via JAX-RS/RestEasy
- **Spring Security OAuth2**: Authentication and authorization
- **Markdown/AsciiDoc**: Content markup support

## AI & Integration
- **LangChain4j 0.30.0**: AI integration framework
- **OpenAI**: AI model integration
- **Apache Kafka**: Event streaming and messaging
- **Webhooks**: External system integration

## Development & Operations
- **Docker**: Containerization with multi-stage builds
- **Docker Compose**: Local development orchestration
- **Lombok**: Code generation for boilerplate reduction
- **Spring Boot Actuator**: Application monitoring

## Common Commands

### Development
```bash
# Build project
mvn clean install

# Run application locally
mvn -pl webapp spring-boot:run

# Run with load-time weaving agent
mvn -pl webapp spring-boot:run -javaagent:lib/spring-instrument.jar

# Run tests
mvn test integration-test
```

### Docker Operations
```bash
# Build Docker image
make build-image

# Run with Docker Compose
make run-image

# Development with Docker
make dev-docker

# Start Kafka services
make start-kafka
```

### Database Profiles
- Default: PostgreSQL (`POSTGRESQL` profile)
- SQL Server: Use `SQLSERVER` profile
- H2: Default for development/testing

### Module Configuration
Modules can be enabled/disabled via properties:
```yaml
nexus.modules.organizations.enabled: true
nexus.modules.projects.enabled: true
nexus.modules.franchise.enabled: true
nexus.modules.web.enabled: true
nexus.modules.security.enabled: true
```