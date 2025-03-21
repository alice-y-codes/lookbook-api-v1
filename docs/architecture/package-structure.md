# Package Structure

## Current Structure
```
src/main/java/com/lookbook/
├── domain/
│   ├── model/user/       # Should be moved to domain/user/model
│   ├── shared/          # Common domain components
│   └── port/           # Repository interfaces
├── application/
└── infrastructure/
```

## Recommended Structure
```
src/main/java/com/lookbook/
├── common/                         # Cross-cutting concerns
│   ├── config/                     # Application configuration
│   ├── exceptions/                 # Global exception handling
│   ├── validation/                 # Validation utilities
│   └── utils/                      # Common utilities
│
├── [feature1]/                     # Feature-specific packages (e.g., user, auth, wardrobe)
│   ├── domain/                     # Domain layer
│   │   ├── model/                  # Domain entities
│   │   │   ├── [Entity].java       # Domain entity classes
│   │   │   └── [ValueObject].java  # Value objects
│   │   ├── events/                 # Domain events
│   │   ├── repositories/           # Repository interfaces
│   │   └── services/               # Domain services
│   │
│   ├── application/                # Application layer
│   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── request/            # Request DTOs
│   │   │   └── response/           # Response DTOs
│   │   ├── mappers/                # DTO to domain mappers
│   │   └── services/               # Application services
│   │
│   └── infrastructure/             # Infrastructure layer
│       ├── persistence/            # Persistence implementation
│       │   ├── entities/           # JPA entities
│       │   ├── repositories/       # Spring Data repositories
│       │   ├── mappers/            # JPA mappers
│       │   └── adapters/           # Repository implementations
│       └── api/                    # API endpoints
│           ├── controllers/        # REST controllers
│           ├── advices/            # Controller advices
│           └── exceptions/         # API-specific exceptions
│
├── [feature2]/                     # Another feature package
│   ├── domain/
│   ├── application/
│   └── infrastructure/
│
└── [feature3]/                     # Another feature package
    ├── domain/
    ├── application/
    └── infrastructure/
```

## Example Structure for User Feature

```
src/main/java/com/lookbook/user/
├── domain/
│   ├── model/
│   │   ├── User.java              # User entity
│   │   ├── Email.java             # Email value object
│   │   ├── Username.java          # Username value object
│   │   └── UserStatus.java        # Status enum
│   ├── events/
│   │   ├── UserCreatedEvent.java  # Domain event
│   │   └── UserActivatedEvent.java
│   ├── repositories/
│   │   └── UserRepository.java    # Repository interface
│   └── services/
│       └── UserDomainService.java # Domain service
│
├── application/
│   ├── dto/
│   │   ├── request/
│   │   │   ├── CreateUserRequest.java
│   │   │   └── UpdateUserRequest.java
│   │   └── response/
│   │       └── UserResponse.java
│   ├── mappers/
│   │   └── UserMapper.java
│   └── services/
│       └── UserService.java      # Application service
│
└── infrastructure/
    ├── persistence/
    │   ├── entities/
    │   │   └── UserJpaEntity.java
    │   ├── repositories/
    │   │   └── UserJpaRepository.java
    │   ├── mappers/
    │   │   └── UserJpaMapper.java
    │   └── adapters/
    │       └── UserRepositoryAdapter.java
    └── api/
        ├── controllers/
        │   └── UserController.java
        └── exceptions/
            └── UserExceptionHandler.java
```

## Rationale

1. **Bounded Context Separation**
   - Each domain concept gets its own package (user, auth, wardrobe, etc.)
   - Clear boundaries between different parts of the system
   - Easier to maintain and understand relationships
   - New developers can focus on specific features

2. **Layer Separation**
   - Clear separation between domain, application, and infrastructure
   - Domain model remains pure and focused on business logic
   - Infrastructure concerns are properly isolated
   - Changes to one layer don't affect other layers

3. **Package Naming**
   - Descriptive and consistent naming across the codebase
   - Clear purpose for each package
   - Easy to locate related code
   - Self-documenting structure

4. **Feature-Based Structure**
   - Code organized by business feature rather than technical layer
   - Related code is kept together
   - Makes navigation and maintenance simpler
   - Supports clean architecture principles

## Migration Plan

1. **Analyze Current Structure**
   - Identify all components needing reorganization
   - Document dependencies between components

2. **Create New Package Structure**
   - Create directories for the new structure
   - Set up base packages for each feature

3. **Migrate Components**
   - Move domain entities to their respective bounded context packages
   - Reorganize infrastructure code to match domain structure
   - Update imports and fix any broken references
   - Update tests to match new structure

4. **Validate Changes**
   - Ensure all tests pass after migration
   - Verify application functionality
   - Check for any circular dependencies

5. **Documentation**
   - Update documentation to reflect new structure
   - Create package-level documentation with purpose and content
   - Add guidelines for where to place new code

## Recommendations for New Development

When developing new features:

1. **Follow the established pattern**
   - Create all layers (domain, application, infrastructure)
   - Maintain proper separation of concerns
   - Use consistent naming conventions

2. **Start with the domain model**
   - Define entities, value objects, and domain services first
   - Create repository interfaces in the domain layer
   - Implement business logic in domain entities and services

3. **Create application services**
   - Implement use cases in the application layer
   - Define DTOs for request/response objects
   - Create mappers between domain and DTOs

4. **Implement infrastructure**
   - Create JPA entities and repositories
   - Implement domain repository interfaces
   - Create REST controllers and exception handlers

Following this structure ensures maintainability, testability, and adherence to clean architecture principles. 