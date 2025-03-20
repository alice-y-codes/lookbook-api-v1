# Lookbook API

A social wardrobe management API built with Spring Boot and Hexagonal Architecture.

## Project Overview

Lookbook API is a backend service that allows users to:
- Manage their digital wardrobe
- Create and share outfits
- Interact socially with other users
- Access analytics and insights about their wardrobe

## Technology Stack

- Java 21
- Spring Boot 3.2.4
- Spring Data JPA
- Spring Security with JWT
- PostgreSQL (Production)
- H2 (Development)
- Redis (Caching)
- Flyway (Database Migrations)
- OpenAPI/Swagger (API Documentation)
- Docker/Docker Compose

## Architecture

This project follows Hexagonal Architecture (also known as Ports and Adapters) with three main layers:

1. **Domain Layer**: Core business logic, entities, value objects
2. **Application Layer**: Use cases, ports (interfaces)
3. **Infrastructure Layer**: Adapters for external systems

For more details, see [Architecture Diagrams](../lookbook-api-v1/docs/architecture-diagrams.md) and [Project Structure](../lookbook-api-v1/docs/project-structure.md).

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8+
- Docker and Docker Compose (optional, for running dependencies)

### Development Setup

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/lookbook-api.git
   cd lookbook-api
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run with development profile:
   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

4. Access the API at `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui.html`
   - H2 Console: `http://localhost:8080/api/h2-console` (when using dev profile)

### Using Docker

Start the application with all dependencies:

```
docker-compose up -d
```

## API Documentation

API documentation is available via Swagger UI when the application is running.

## Project Structure

The project follows a domain-driven, hexagonal architecture. See [Project Structure](../lookbook-api-v1/docs/project-structure.md) for details.

## Contributing

We follow a structured Git workflow for all contributions. Please review our [Git Workflow & Branching Strategy](../lookbook-api-v1/docs/git-workflow.md) document before contributing.

In summary:
1. Fork the repository
2. Create a feature branch (`feature/your-feature-name`)
3. Make your changes following our coding standards and commit message conventions
4. Run tests and ensure they pass
5. Submit a pull request to the `develop` branch

See [Git Workflow](../lookbook-api-v1/docs/git-workflow.md) for complete details on our branching strategy, commit message format, and pull request process.

## License

[MIT License](LICENSE) 