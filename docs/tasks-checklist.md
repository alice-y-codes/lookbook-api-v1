# Lookbook API: Comprehensive Iterative Implementation Plan

This document outlines an iterative approach to developing the Lookbook API, with each iteration delivering usable functionality while maintaining technical depth.

## Development Approach

- **Test-Driven Development**: Write tests before implementing features
- **Domain-Driven Design**: Focus on the core domain model and business logic
- **Hexagonal Architecture**: Isolate the domain from external concerns
- **Iterative Development**: Deliver working features in short iterations

## Core Infrastructure (Applied Throughout)

### Domain Layer Base Components

- [x] `BaseEntity` abstract class
  - [x] Path: `com.lookbook.base.domain.entities.BaseEntity`
  - [x] UUID-based entity identity
  - [x] Creation/modification timestamps
  - [x] Equality based on identity
  - [x] Tests: `BaseEntityTest`

- [x] `BaseValueObject` abstract class
  - [x] Path: `com.lookbook.base.domain.valueobjects.BaseValueObject`
  - [x] Immutability enforcement
  - [x] Value-based equality
  - [x] Self-validation integration
  - [x] Tests: `BaseValueObjectTest`

- [x] Domain Events
  - [x] `DomainEvent` interface
    - [x] Path: `com.lookbook.base.domain.events.DomainEvent`
    - [x] Event ID and timestamp properties
    - [x] Event type and metadata
  - [x] `BaseDomainEvent` implementation
    - [x] Path: `com.lookbook.base.domain.events.BaseDomainEvent`
    - [x] Immutable event properties
    - [x] Metadata handling
  - [x] Tests: `BaseDomainEventTest`

- [x] Domain Exceptions
  - [x] `DomainException` abstract class
    - [x] Path: `com.lookbook.base.domain.exceptions.DomainException`
    - [x] Error code management
    - [x] Message formatting
  - [x] Common exception types
    - [x] `ValidationException`: For invariant violations
    - [x] `EntityNotFoundException`: For missing entities
    - [x] `DuplicateEntityException`: For uniqueness violations
  - [x] Tests: Exception test cases

- [x] Validation Framework
  - [x] `ValidationResult` class
    - [x] Path: `com.lookbook.base.domain.validation.ValidationResult`
    - [x] Error collection and reporting
    - [x] Validation result combination
  - [x] `SelfValidating` interface
    - [x] Path: `com.lookbook.base.domain.validation.SelfValidating`
    - [x] Contract for self-validating objects
  - [x] Tests: Validation utilities tests

### Application Layer Base Components

#### Application Ports (To be implemented)

- [x] Repository Interfaces
  - [x] `BaseRepository` interface
    - [x] CRUD operations
    - [x] Query specifications
  - [x] Specific repository interfaces
    - [x] `EntityRepository`
    - [x] `ReadOnlyRepository`
  - [x] Tests: Repository contract tests

- [ ] Service Interfaces
  - [ ] External system interfaces
    - [ ] `FileStorageService`
    - [ ] `EmailService`
    - [ ] `NotificationService`
  - [ ] Tests: Service contract tests


### Infrastructure Layer Base Components

#### Persistence

- [x] Repository Implementations
  - [x] `BaseRepositoryImpl`
    - [x] Implementation of `BaseRepository`
    - [x] Transaction handling
    - [x] Query building
  - [x] Tests: Repository implementation tests

#### Web

- [x] Base Controller
  - [x] `BaseController`
    - [x] Common request/response handling
    - [x] Error translation
    - [x] Authentication integration
  - [x] Tests: Controller behavior tests
  - [x] Implementation plan created

- [x] Exception Handler
  - [x] `GlobalExceptionHandler`
    - [x] Domain exception translation
    - [x] HTTP status mapping
    - [x] Response formatting
  - [x] Tests: Exception handling tests
  - [x] Implementation plan created

- [x] Response Models
  - [x] `ApiResponse<T>`
  - [x] `ErrorResponse`
  - [x] `ValidationErrorResponse`
  - [x] Tests: Response model tests
  - [x] Implementation plan created

#### Security (To be implemented)

- [ ] Authentication Components
  - [ ] JWT utilities
  - [ ] Security configuration
  - [ ] Tests: Security tests

## MVP Iterations

### MVP 0: Project Foundation (1 week)

#### Deliverable
A working application skeleton with CI/CD pipeline and development environment

#### Tasks
- [x] Set up project structure with hexagonal architecture
  - [x] Create directory structure based on DDD
  - [x] Set up package organization for domain, application, infrastructure
  - [x] Establish layer isolation
  - [x] Set up resources directory

- [x] Configure Maven dependencies
  - [x] Set up Spring Boot starter dependencies
  - [x] Add validation dependencies
  - [x] Add security dependencies
  - [x] Configure build plugins

- [ ] Create development environment configuration
  - [ ] Configure application properties
  - [ ] Set up environment-specific properties
  - [ ] Configure logging

- [x] Set up Docker Compose for local development
  - [x] Create Docker Compose file for database
  - [x] Configure database environment variables
  - [x] Set up network configuration

- [x] Configure testing framework
  - [x] Create test folder structure
  - [x] Add JUnit, Mockito, TestContainers dependencies
  - [ ] Configure test properties
  - [ ] Set up test profiles

- [x] Set up Git repository with branching strategy
  - [x] Initialize Git repository
  - [x] Configure .gitignore
  - [x] Define branch naming convention

- [ ] Configure CI/CD pipeline
  - [ ] Create GitHub Actions workflow
  - [ ] Set up testing with JUnit
  - [ ] Configure code quality checks

- [ ] Create basic application documentation
  - [ ] Document architecture
  - [ ] Create README with setup instructions
  - [ ] Document project structure

- [x] Implement core shared domain components
  - [x] Create `BaseEntity` with identity and timestamps
  - [x] Implement `BaseValueObject` with validation
  - [x] Add `DomainEvent` interface and base implementation
  - [x] Create domain exception hierarchy
  - [x] Implement validation utilities
  - [x] Write tests for base components

### MVP 1: User Authentication (2 weeks)

#### Deliverable
Users can register, log in, and receive JWT tokens

#### Domain Layer
- [ ] Define user domain model
  - [ ] `User` aggregate root
    - [ ] Path: `com.lookbook.user.domain.model.User`
    - [ ] Fields: id, username, email, password, status, createdAt, updatedAt
    - [ ] Methods: register, activate, deactivate, changePassword
    - [ ] Invariants: email format, password strength, username format
    - [ ] Tests: User entity tests

  - [ ] Value objects
    - [ ] `Email` value object
      - [ ] Path: `com.lookbook.user.domain.model.Email`
      - [ ] Validation: format, length, domain
      - [ ] Tests: Email validation tests
    - [ ] `Password` value object
      - [ ] Path: `com.lookbook.user.domain.model.Password`
      - [ ] Hashing and strength validation
      - [ ] Tests: Password hashing and validation tests
    - [ ] `Username` value object
      - [ ] Path: `com.lookbook.user.domain.model.Username`
      - [ ] Format validation
      - [ ] Tests: Username validation tests

  - [ ] Enums and constants
    - [ ] `UserStatus` enum
      - [ ] Path: `com.lookbook.user.domain.model.UserStatus`
      - [ ] Values: ACTIVE, INACTIVE, PENDING
      - [ ] Tests: Status transition tests

  - [ ] Domain events
    - [ ] `UserRegisteredEvent`
    - [ ] `UserActivatedEvent`
    - [ ] `PasswordChangedEvent`
    - [ ] Tests: Event property tests

  - [ ] Repository interfaces (ports)
    - [ ] `UserRepository` interface
      - [ ] Path: `com.lookbook.user.application.ports.repositories.UserRepository`
      - [ ] Methods: findById, findByEmail, findByUsername, save, delete
      - [ ] Tests: Repository contract tests

#### Application Layer
- [ ] Authentication application services
  - [ ] `JwtService` interface
    - [ ] Path: `com.lookbook.auth.domain.services.JwtService`
    - [ ] Methods: generateToken, validateToken, refreshToken, extractClaims
    - [ ] Tests: JWT service tests

  - [ ] `AuthenticationService`
    - [ ] Path: `com.lookbook.auth.application.services.AuthenticationService`
    - [ ] Methods: register, login, refreshToken, resetPassword
    - [ ] Tests: Authentication flow tests

  - [ ] `UserService`
    - [ ] Path: `com.lookbook.user.application.services.UserService`
    - [ ] Methods: createUser, updateUser, activateUser, deactivateUser
    - [ ] Tests: User management tests

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] Path: `com.lookbook.auth.application.dto.request.*`
    - [ ] RegisterUserRequest, LoginRequest, TokenRefreshRequest
    - [ ] Tests: Request validation tests

  - [ ] Response DTOs
    - [ ] Path: `com.lookbook.auth.application.dto.response.*`
    - [ ] UserResponse, AuthenticationResponse, TokenRefreshResponse
    - [ ] Tests: Response formatting tests

  - [ ] Mappers
    - [ ] Path: `com.lookbook.user.application.mappers.*`
    - [ ] UserMapper, AuthenticationMapper
    - [ ] Tests: Mapping tests

#### Infrastructure Layer
- [ ] Security configuration
  - [ ] Path: `com.lookbook.auth.infrastructure.security.*`
  - [ ] Security config, JWT filter, authentication entry point
  - [ ] Tests: Security configuration tests

- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] Path: `com.lookbook.user.infrastructure.persistence.entities.*`
    - [ ] UserJpaEntity and related entities
    - [ ] Tests: JPA mapping tests

  - [ ] JPA repositories
    - [ ] Path: `com.lookbook.user.infrastructure.persistence.repositories.*`
    - [ ] Spring Data interfaces
    - [ ] Tests: Repository query tests

  - [ ] Repository adapters
    - [ ] Path: `com.lookbook.user.infrastructure.persistence.adapters.*`
    - [ ] Implementation of domain repository interfaces
    - [ ] Tests: Adapter behavior tests

- [ ] JWT implementation
  - [ ] Path: `com.lookbook.auth.infrastructure.security.*`
  - [ ] JWT token generation, validation, refresh
  - [ ] Tests: JWT lifecycle tests

#### API Layer
- [ ] REST controllers
  - [ ] Path: `com.lookbook.auth.infrastructure.web.controllers.*`
  - [ ] Authentication endpoints
  - [ ] User endpoints
  - [ ] Tests: Controller endpoint tests

- [ ] Exception handling
  - [ ] Path: `com.lookbook.auth.infrastructure.web.exceptions.*`
  - [ ] Error response formatting
  - [ ] Tests: Exception handling tests

#### Integration Tests
- [ ] Authentication flow tests
  - [ ] Registration and validation
  - [ ] Login and token generation
  - [ ] Token refresh
  - [ ] Password reset

### MVP 2: User Profiles (2 weeks)

#### Deliverable
Users can create and manage their profiles with profile images

#### Domain Layer
- [ ] Extend user domain model
  - [ ] `UserProfile` entity
    - [ ] Fields: id, userId, displayName, bio, location, profileImageUrl
    - [ ] Methods: update, addProfileImage, removeProfileImage
    - [ ] Tests for profile creation, modification, validation

  - [ ] Value objects
    - [ ] `ProfileImage` with metadata
    - [ ] `DisplayName` with validation
    - [ ] `Biography` with length validation
    - [ ] Tests for value object validation

  - [ ] Domain events
    - [ ] `ProfileCreatedEvent`
    - [ ] `ProfileUpdatedEvent`
    - [ ] `ProfileImageChangedEvent`
    - [ ] Tests for event creation and properties

  - [ ] Domain repositories (ports)
    - [ ] `ProfileRepository` interface
      - [ ] Methods: findByUserId, save, delete
    - [ ] `FileStorageRepository` interface
      - [ ] Methods: storeFile, getFile, deleteFile
    - [ ] Tests for repository contracts

#### Application Layer
- [ ] Profile application services
  - [ ] `ProfileService`
    - [ ] Methods: createProfile, updateProfile, getProfile, deleteProfile
    - [ ] Business rule enforcement
    - [ ] Tests for profile management

  - [ ] `ProfileImageService`
    - [ ] Methods: uploadImage, getImage, deleteImage
    - [ ] Image processing and validation
    - [ ] Tests for image operations

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] `CreateProfileRequest`: displayName, bio, location
    - [ ] `UpdateProfileRequest`: displayName, bio, location
    - [ ] Tests for DTO validation

  - [ ] Response DTOs
    - [ ] `ProfileResponse`: id, userId, displayName, bio, location, profileImageUrl
    - [ ] Tests for DTO mapping

  - [ ] Mappers
    - [ ] `ProfileMapper`: Domain <-> DTO
    - [ ] Tests for mapping behavior

#### Infrastructure Layer
- [ ] File storage
  - [ ] `LocalFileStorageAdapter`: Store files on disk
  - [ ] `S3FileStorageAdapter`: Store files in S3
  - [ ] Tests for storage operations

- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] `ProfileJpaEntity`: JPA mapping of profile
    - [ ] Tests for entity mapping

  - [ ] JPA repositories
    - [ ] `ProfileJpaRepository`: Spring Data interface
    - [ ] Tests for repository queries

  - [ ] Repository adapters
    - [ ] `ProfileRepositoryAdapter`: Implements domain repository
    - [ ] Tests for adapter behavior

- [ ] Image processing
  - [ ] `ImageProcessingService`: Resize, crop, optimize images
  - [ ] Tests for image processing

#### API Layer
- [ ] REST controllers
  - [ ] `ProfileController`
    - [ ] Endpoints: `/api/profiles/me`, `/api/profiles/{userId}`
    - [ ] Authorization for profile operations
    - [ ] Tests for controller endpoints

  - [ ] `ProfileImageController`
    - [ ] Endpoints: `/api/profiles/images`, `/api/profiles/images/{imageId}`
    - [ ] Multipart file handling
    - [ ] Tests for image upload/download

#### Integration Tests
- [ ] Profile management flow
  - [ ] Test profile creation and validation
  - [ ] Test profile update
  - [ ] Test profile retrieval

- [ ] Image handling
  - [ ] Test image upload
  - [ ] Test image retrieval
  - [ ] Test image deletion

### MVP 3: Friends and Connections (2 weeks)

#### Deliverable
Users can send/accept friend requests and view their connections

#### Domain Layer
- [ ] Friend relationship domain
  - [ ] `FriendRelationship` entity
    - [ ] Fields: id, requesterId, addresseeId, status, createdAt, updatedAt
    - [ ] Methods: request, accept, reject, cancel, block
    - [ ] Tests for relationship lifecycle

  - [ ] Value objects
    - [ ] `FriendshipStatus` enum (PENDING, ACCEPTED, REJECTED, BLOCKED)
    - [ ] Tests for status transitions

  - [ ] Domain events
    - [ ] `FriendRequestSentEvent`
    - [ ] `FriendRequestAcceptedEvent`
    - [ ] `FriendRequestRejectedEvent`
    - [ ] Tests for event creation and properties

  - [ ] Domain repositories (ports)
    - [ ] `FriendRepository` interface
      - [ ] Methods: findByUser, findByUsers, findByStatus, save, delete
    - [ ] Tests for repository contract

  - [ ] Domain services
    - [ ] `FriendshipDomainService`: Handle complex friendship operations
    - [ ] Tests for domain service logic

#### Application Layer
- [ ] Friend application services
  - [ ] `FriendshipService`
    - [ ] Methods: sendRequest, acceptRequest, rejectRequest, cancelRequest, blockUser
    - [ ] Business rule enforcement
    - [ ] Tests for friendship operations

  - [ ] `FriendQueryService`
    - [ ] Methods: getFriends, getPendingRequests, getSentRequests
    - [ ] Query optimization
    - [ ] Tests for query operations

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] `FriendRequestDto`: addresseeId
    - [ ] `FriendActionDto`: friendshipId, action
    - [ ] Tests for DTO validation

  - [ ] Response DTOs
    - [ ] `FriendshipResponseDto`: id, requesterId, addresseeId, status, createdAt
    - [ ] `FriendDto`: id, username, displayName, profileImageUrl
    - [ ] Tests for DTO mapping

  - [ ] Mappers
    - [ ] `FriendshipMapper`: Domain <-> DTO
    - [ ] Tests for mapping behavior

#### Infrastructure Layer
- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] `FriendshipJpaEntity`: JPA mapping of relationship
    - [ ] Tests for entity mapping

  - [ ] JPA repositories
    - [ ] `FriendshipJpaRepository`: Spring Data interface
    - [ ] Tests for repository queries

  - [ ] Repository adapters
    - [ ] `FriendRepositoryAdapter`: Implements domain repository
    - [ ] Tests for adapter behavior

- [ ] Friend suggestion engine
  - [ ] `FriendSuggestionService`: Suggest potential friends
  - [ ] Tests for suggestion algorithm

#### API Layer
- [ ] REST controllers
  - [ ] `FriendController`
    - [ ] Endpoints: `/api/friends`, `/api/friends/requests`, `/api/friends/requests/{id}`
    - [ ] Authorization for friendship operations
    - [ ] Tests for controller endpoints

  - [ ] `FriendSuggestionController`
    - [ ] Endpoints: `/api/friends/suggestions`
    - [ ] Pagination and filtering
    - [ ] Tests for suggestion retrieval

#### Integration Tests
- [ ] Friendship flow
  - [ ] Test friend request creation
  - [ ] Test request acceptance/rejection
  - [ ] Test friendship management
  - [ ] Test edge cases (double requests, etc.)

### MVP 4: Basic Wardrobe (3 weeks)

#### Deliverable
Users can create a personal wardrobe and add clothing items

#### Domain Layer
- [ ] Wardrobe domain model
  - [ ] `Wardrobe` aggregate root
    - [ ] Fields: id, userId, name, description, createdAt, updatedAt
    - [ ] Methods: addItem, removeItem, updateDetails
    - [ ] Tests for wardrobe operations

  - [ ] `Item` entity
    - [ ] Fields: id, wardrobeId, name, description, category, attributes, images
    - [ ] Methods: update, addImage, removeImage, categorize
    - [ ] Tests for item operations

  - [ ] Value objects
    - [ ] `Category` for item classification
    - [ ] `ItemAttribute` for size, color, brand, etc.
    - [ ] `ItemImage` with metadata and ordering
    - [ ] Tests for value object validation

  - [ ] Domain events
    - [ ] `WardrobeCreatedEvent`
    - [ ] `ItemAddedEvent`
    - [ ] `ItemUpdatedEvent`
    - [ ] Tests for event creation and properties

  - [ ] Domain repositories (ports)
    - [ ] `WardrobeRepository` interface
      - [ ] Methods: findByUserId, findById, save, delete
    - [ ] `ItemRepository` interface
      - [ ] Methods: findByWardrobeId, findById, findByCategory, save, delete
    - [ ] Tests for repository contracts

#### Application Layer
- [ ] Wardrobe application services
  - [ ] `WardrobeService`
    - [ ] Methods: createWardrobe, updateWardrobe, deleteWardrobe, getWardrobe
    - [ ] Business rule enforcement
    - [ ] Tests for wardrobe management

  - [ ] `ItemService`
    - [ ] Methods: addItem, updateItem, removeItem, getItem, listItems
    - [ ] Categorization logic
    - [ ] Item attribute management
    - [ ] Tests for item operations

  - [ ] `ItemImageService`
    - [ ] Methods: addItemImage, removeItemImage, reorderImages
    - [ ] Image processing and validation
    - [ ] Tests for image operations

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] `CreateWardrobeRequest`: name, description
    - [ ] `AddItemRequest`: name, description, category, attributes
    - [ ] Tests for DTO validation

  - [ ] Response DTOs
    - [ ] `WardrobeResponseDto`: id, userId, name, description, itemCount
    - [ ] `ItemResponseDto`: id, name, description, category, attributes, images
    - [ ] Tests for DTO mapping

  - [ ] Mappers
    - [ ] `WardrobeMapper`: Domain <-> DTO
    - [ ] `ItemMapper`: Domain <-> DTO
    - [ ] Tests for mapping behavior

#### Infrastructure Layer
- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] `WardrobeJpaEntity`
    - [ ] `ItemJpaEntity`
    - [ ] `CategoryJpaEntity`
    - [ ] `ItemAttributeJpaEntity`
    - [ ] `ItemImageJpaEntity`
    - [ ] Tests for entity mappings

  - [ ] JPA repositories
    - [ ] `WardrobeJpaRepository`
    - [ ] `ItemJpaRepository`
    - [ ] `CategoryJpaRepository`
    - [ ] Tests for repository queries

  - [ ] Repository adapters
    - [ ] `WardrobeRepositoryAdapter`
    - [ ] `ItemRepositoryAdapter`
    - [ ] Tests for adapter behavior

- [ ] Image processing
  - [ ] `ItemImageProcessor`: Process, resize, optimize item images
  - [ ] Tests for image processing

#### API Layer
- [ ] REST controllers
  - [ ] `WardrobeController`
    - [ ] Endpoints: `/api/wardrobes`, `/api/wardrobes/{id}`
    - [ ] Authorization for wardrobe operations
    - [ ] Tests for controller endpoints

  - [ ] `ItemController`
    - [ ] Endpoints: `/api/wardrobes/{id}/items`, `/api/items/{id}`
    - [ ] Item filtering and search
    - [ ] Tests for controller endpoints

  - [ ] `ItemImageController`
    - [ ] Endpoints: `/api/items/{id}/images`, `/api/items/images/{id}`
    - [ ] Multipart image upload
    - [ ] Tests for image handling

#### Integration Tests
- [ ] Wardrobe management flow
  - [ ] Test wardrobe creation and retrieval
  - [ ] Test item addition and management
  - [ ] Test category and attribute operations
  - [ ] Test image upload and processing

### MVP 5: Outfit Creation (2 weeks)

#### Deliverable
Users can create outfits from their wardrobe items

#### Domain Layer
- [ ] Outfit domain model
  - [ ] `Outfit` aggregate root
    - [ ] Fields: id, userId, name, description, items, occasion, season, createdAt, updatedAt
    - [ ] Methods: addItem, removeItem, updateDetails, categorize
    - [ ] Tests for outfit operations

  - [ ] `OutfitItem` entity
    - [ ] Fields: id, outfitId, itemId, position, notes
    - [ ] Methods: updatePosition, addNotes
    - [ ] Tests for outfit item operations

  - [ ] Value objects
    - [ ] `OutfitOccasion` (formal, casual, work, etc.)
    - [ ] `Season` (spring, summer, fall, winter)
    - [ ] `ItemPosition` for visual arrangement
    - [ ] Tests for value object validation

  - [ ] Domain events
    - [ ] `OutfitCreatedEvent`
    - [ ] `OutfitUpdatedEvent`
    - [ ] `ItemAddedToOutfitEvent`
    - [ ] Tests for event creation and properties

  - [ ] Domain repositories (ports)
    - [ ] `OutfitRepository` interface
      - [ ] Methods: findByUserId, findById, findByOccasion, findBySeason, save, delete
    - [ ] Tests for repository contract

#### Application Layer
- [ ] Outfit application services
  - [ ] `OutfitService`
    - [ ] Methods: createOutfit, updateOutfit, deleteOutfit, getOutfit, listOutfits
    - [ ] Business rule enforcement
    - [ ] Tests for outfit management

  - [ ] `OutfitItemService`
    - [ ] Methods: addItemToOutfit, removeItemFromOutfit, updateItemPosition
    - [ ] Position management
    - [ ] Tests for outfit item operations

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] `CreateOutfitRequest`: name, description, occasion, season, items
    - [ ] `UpdateOutfitRequest`: name, description, occasion, season
    - [ ] `AddItemToOutfitRequest`: itemId, position, notes
    - [ ] Tests for DTO validation

  - [ ] Response DTOs
    - [ ] `OutfitResponseDto`: id, userId, name, description, occasion, season, items, createdAt
    - [ ] `OutfitItemResponseDto`: id, itemId, position, notes, item
    - [ ] Tests for DTO mapping

  - [ ] Mappers
    - [ ] `OutfitMapper`: Domain <-> DTO
    - [ ] `OutfitItemMapper`: Domain <-> DTO
    - [ ] Tests for mapping behavior

#### Infrastructure Layer
- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] `OutfitJpaEntity`
    - [ ] `OutfitItemJpaEntity`
    - [ ] Tests for entity mappings

  - [ ] JPA repositories
    - [ ] `OutfitJpaRepository`
    - [ ] `OutfitItemJpaRepository`
    - [ ] Tests for repository queries

  - [ ] Repository adapters
    - [ ] `OutfitRepositoryAdapter`
    - [ ] Tests for adapter behavior

- [ ] Outfit image generation
  - [ ] `OutfitImageGenerator`: Create composite image of outfit
  - [ ] Tests for image generation

#### API Layer
- [ ] REST controllers
  - [ ] `OutfitController`
    - [ ] Endpoints: `/api/outfits`, `/api/outfits/{id}`
    - [ ] Authorization for outfit operations
    - [ ] Tests for controller endpoints

  - [ ] `OutfitItemController`
    - [ ] Endpoints: `/api/outfits/{id}/items`, `/api/outfits/items/{id}`
    - [ ] Item position management
    - [ ] Tests for controller endpoints

#### Integration Tests
- [ ] Outfit management flow
  - [ ] Test outfit creation and retrieval
  - [ ] Test adding items to outfits
  - [ ] Test outfit categorization
  - [ ] Test outfit image generation

### MVP 6: Visibility Controls (2 weeks)

#### Deliverable
Users can control visibility of their wardrobe, items, and outfits

#### Domain Layer
- [ ] Privacy settings domain model
  - [ ] `PrivacySettings` entity
    - [ ] Fields: id, entityId, entityType, visibilityLevel, allowedUserIds
    - [ ] Methods: updateVisibility, addAllowedUser, removeAllowedUser
    - [ ] Tests for privacy settings operations

  - [ ] Value objects
    - [ ] `VisibilityLevel` enum (PUBLIC, FRIENDS_ONLY, SPECIFIC_USERS, PRIVATE)
    - [ ] `EntityType` enum (WARDROBE, ITEM, OUTFIT)
    - [ ] Tests for value object validation

  - [ ] Domain services
    - [ ] `PermissionEvaluator`: Check if a user has permission to access an entity
    - [ ] Tests for permission evaluation logic

  - [ ] Domain repositories (ports)
    - [ ] `PrivacySettingsRepository` interface
      - [ ] Methods: findByEntityIdAndType, save, delete
    - [ ] Tests for repository contract

#### Application Layer
- [ ] Privacy application services
  - [ ] `PrivacyService`
    - [ ] Methods: updatePrivacySettings, getPrivacySettings, checkPermission
    - [ ] Business rule enforcement
    - [ ] Tests for privacy management

  - [ ] `VisibilityQueryService`
    - [ ] Methods: getVisibleWardrobes, getVisibleOutfits, getVisibleItems
    - [ ] Visibility filtering
    - [ ] Tests for visibility queries

- [ ] DTOs
  - [ ] Request DTOs
    - [ ] `UpdatePrivacyRequest`: entityId, entityType, visibilityLevel, allowedUserIds
    - [ ] Tests for DTO validation

  - [ ] Response DTOs
    - [ ] `PrivacySettingsResponseDto`: id, entityId, entityType, visibilityLevel, allowedUsers
    - [ ] Tests for DTO mapping

  - [ ] Mappers
    - [ ] `PrivacySettingsMapper`: Domain <-> DTO
    - [ ] Tests for mapping behavior

#### Infrastructure Layer
- [ ] Persistence adapters
  - [ ] JPA entities
    - [ ] `PrivacySettingsJpaEntity`
    - [ ] Tests for entity mapping

  - [ ] JPA repositories
    - [ ] `PrivacySettingsJpaRepository`
    - [ ] Tests for repository queries

  - [ ] Repository adapters
    - [ ] `PrivacySettingsRepositoryAdapter`
    - [ ] Tests for adapter behavior

- [ ] Security integration
  - [ ] `CustomPermissionEvaluator`: Integrate with Spring Security
  - [ ] Tests for security integration

#### API Layer
- [ ] REST controllers
  - [ ] `PrivacyController`
    - [ ] Endpoints: `/api/privacy`, `/api/privacy/{entityType}/{entityId}`
    - [ ] Authorization for privacy operations
    - [ ] Tests for controller endpoints

- [ ] Security annotations
  - [ ] `@VisibilityCheck`: Custom annotation for visibility enforcement
  - [ ] Tests for annotation behavior

#### Integration Tests
- [ ] Privacy management flow
  - [ ] Test privacy settings creation and update
  - [ ] Test visibility filtering
  - [ ] Test permission checking
  - [ ] Test edge cases with different visibility levels

## Future Enhancements

### Analytics and Insights
- [ ] Implement wardrobe analytics
- [ ] Create style statistics and reporting
- [ ] Develop usage insights

### Sustainability Features
- [ ] Design cost-per-wear tracking
- [ ] Implement sustainability metrics
- [ ] Create wardrobe value analysis

### Seasonal Management
- [ ] Implement seasonal rotation features
- [ ] Create seasonal wardrobe suggestions
- [ ] Develop weather-based outfit recommendations

### Advanced Search and Discovery
- [ ] Implement advanced search capabilities
- [ ] Create outfit recommendation algorithm
- [ ] Develop style matching features

### Content Moderation
- [ ] Design and implement content reporting
- [ ] Create moderation queue
- [ ] Implement automated content filtering 