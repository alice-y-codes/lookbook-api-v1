# Lookbook API

A modern fashion social platform backend API built with Spring Boot, featuring user management, authentication, and rich domain models following clean architecture principles.

## Features

- **User Management**: Registration, authentication, profile management
- **JWT Authentication**: Secure token-based authentication with refresh tokens
- **Role-Based Access Control**: Fine-grained permissions system
- **Clean Architecture**: Separation of concerns with domain-driven design principles
- **Database Migrations**: Versioned database schema changes using Flyway

## Technology Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security** with JWT
- **PostgreSQL** for production database
- **H2** for testing
- **Flyway** for database migrations
- **JUnit 5** and **Mockito** for testing
- **Maven** for dependency management and build

## Project Structure

The project follows a clean architecture approach with a domain-centric design:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── lookbook/
│   │           ├── auth/          # Authentication and security
│   │           ├── base/          # Base classes and common utilities
│   │           ├── config/        # Application configuration
│   │           ├── item/          # Item domain
│   │           ├── outfit/        # Outfit domain
│   │           ├── user/          # User domain
│   │           ├── wardrobe/      # Wardrobe domain
│   │           └── ...
│   └── resources/
│       ├── db/
│       │   ├── migration/        # Flyway migration scripts
│       │   └── testdata/         # Test data scripts
│       ├── application.properties
│       └── application-test.properties
└── test/
    └── java/
        └── com/
            └── lookbook/
                └── ...           # Test classes
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 14+

### Database Setup

1. Create a PostgreSQL database:

```sql
CREATE DATABASE lookbook;
CREATE USER lookbook WITH ENCRYPTED PASSWORD 'lookbook';
GRANT ALL PRIVILEGES ON DATABASE lookbook TO lookbook;
```

2. The application will automatically run the migrations on startup.

### Building and Running

1. Clone the repository:

```bash
git clone https://github.com/yourusername/lookbook-api.git
cd lookbook-api
```

2. Build the project:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

Alternatively, you can run with a specific profile:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Running Tests

```bash
mvn test
```

To run with the test profile:

```bash
mvn test -Dspring.profiles.active=test
```

## API Documentation

### Authentication Endpoints

- **POST /api/v1/auth/register** - Register a new user
- **POST /api/v1/auth/login** - Authenticate a user
- **POST /api/v1/auth/refresh** - Refresh authentication token

### User Endpoints

- **GET /api/v1/users** - Get all users (admin only)
- **GET /api/v1/users/{id}** - Get user by ID
- **GET /api/v1/users/me** - Get current user profile
- **PUT /api/v1/users/{id}/email** - Update user email
- **PUT /api/v1/users/{id}/password** - Change user password
- **POST /api/v1/users/{id}/activate** - Activate a user
- **POST /api/v1/users/{id}/deactivate** - Deactivate a user

## Database Schema

The database consists of the following core tables:

- **users** - Stores user account information
- **refresh_tokens** - Stores JWT refresh tokens
- **roles** - Defines system roles
- **permissions** - Defines granular permissions
- **role_permissions** - Maps roles to permissions
- **user_roles** - Maps users to roles

See the [Database Documentation](docs/database.md) for more details.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 