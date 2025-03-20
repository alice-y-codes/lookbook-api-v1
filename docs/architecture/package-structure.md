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
├── domain/              # Domain layer
│   ├── user/           # User bounded context
│   │   ├── model/      # User domain model
│   │   │   ├── User.java
│   │   │   ├── Email.java
│   │   │   ├── Password.java
│   │   │   └── Username.java
│   │   ├── repository/ # Repository interfaces
│   │   └── service/    # Domain services
│   ├── wardrobe/       # Wardrobe bounded context
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── shared/         # Shared domain components
│       ├── model/      # Base entities and value objects
│       └── exception/  # Domain exceptions
├── application/        # Application layer
│   ├── user/          # User use cases
│   ├── wardrobe/      # Wardrobe use cases
│   └── shared/        # Common application services
├── infrastructure/     # Infrastructure layer
│   ├── persistence/   # Database implementations
│   │   ├── entity/   # JPA entities
│   │   ├── mapper/   # Entity mappers
│   │   └── repository/ # Repository implementations
│   ├── security/      # Security implementations
│   └── service/       # External service integrations
└── interface/         # Interface layer
    ├── rest/         # REST controllers
    ├── graphql/      # GraphQL resolvers (if needed)
    └── websocket/    # WebSocket handlers
```

## Rationale

1. **Bounded Context Separation**
   - Each domain concept gets its own package
   - Clear boundaries between different parts of the system
   - Easier to maintain and understand relationships

2. **Layer Separation**
   - Clear separation between domain, application, and infrastructure
   - Domain model remains pure and focused
   - Infrastructure concerns are properly isolated

3. **Package Naming**
   - Descriptive and consistent naming
   - Clear purpose for each package
   - Easy to locate related code

## Migration Plan

1. Move domain entities to their respective bounded context packages
2. Reorganize infrastructure code to match domain structure
3. Update imports and fix any broken references
4. Update tests to match new structure 