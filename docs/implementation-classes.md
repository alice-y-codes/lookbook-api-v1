# Lookbook API: Implementation Classes Overview

This document outlines all the classes required for implementing the Lookbook API, organized by domain and architectural layers.

## Base Components

### Domain Layer Base

- `BaseEntity` (abstract class)
- `BaseValueObject` (abstract class)
- `DomainEvent` (interface)
- `BaseDomainEvent` (abstract class)
- `DomainException` (abstract class)
- `ValidationException` (extends DomainException)
- `EntityNotFoundException` (extends DomainException)
- `DuplicateEntityException` (extends DomainException)
- `ValidationResult` (class)
- `SelfValidating` (interface)

### Application Layer Base

- `BaseRepository` (interface)
- `EntityRepository` (interface, extends BaseRepository)
- `ReadOnlyRepository` (interface, extends BaseRepository)
- `FileStorageService` (interface)
- `EmailService` (interface)
- `NotificationService` (interface)

### Infrastructure Layer Base

- `BaseRepositoryImpl` (class)
- `BaseController` (class)
- `GlobalExceptionHandler` (class)
- `ApiResponse<T>` (class)
- `ErrorResponse` (class)
- `ValidationErrorResponse` (class)

## User Authentication

### Domain Layer

- `User` (entity)
- `Email` (value object)
- `Password` (value object)
- `Username` (value object)
- `UserStatus` (enum)
- `UserRegisteredEvent` (event)
- `UserActivatedEvent` (event)
- `PasswordChangedEvent` (event)
- `UserRepository` (interface)

### Application Layer

- `JwtService` (interface)
- `AuthenticationService` (class)
- `UserService` (class)
- `RegisterUserRequest` (DTO)
- `LoginRequest` (DTO)
- `TokenRefreshRequest` (DTO)
- `UserResponse` (DTO)
- `AuthenticationResponse` (DTO)
- `TokenRefreshResponse` (DTO)
- `UserMapper` (mapper)
- `AuthenticationMapper` (mapper)

### Infrastructure Layer

- `SecurityConfig` (configuration)
- `JwtAuthenticationFilter` (filter)
- `JwtAuthenticationEntryPoint` (class)
- `UserJpaEntity` (JPA entity)
- `UserJpaRepository` (JPA repository)
- `UserRepositoryAdapter` (adapter)
- `JwtServiceImpl` (implementation)
- `AuthenticationController` (controller)
- `UserController` (controller)

## User Profiles

### Domain Layer

- `UserProfile` (entity)
- `ProfileImage` (value object)
- `DisplayName` (value object)
- `Biography` (value object)
- `ProfileCreatedEvent` (event)
- `ProfileUpdatedEvent` (event)
- `ProfileImageChangedEvent` (event)
- `ProfileRepository` (interface)
- `FileStorageRepository` (interface)

### Application Layer

- `ProfileService` (class)
- `ProfileImageService` (class)
- `CreateProfileRequest` (DTO)
- `UpdateProfileRequest` (DTO)
- `ProfileResponse` (DTO)
- `ProfileMapper` (mapper)

### Infrastructure Layer

- `LocalFileStorageAdapter` (adapter)
- `S3FileStorageAdapter` (adapter)
- `ProfileJpaEntity` (JPA entity)
- `ProfileJpaRepository` (JPA repository)
- `ProfileRepositoryAdapter` (adapter)
- `ImageProcessingService` (service)
- `ProfileController` (controller)
- `ProfileImageController` (controller)

## Friends and Connections

### Domain Layer

- `FriendRelationship` (entity)
- `FriendshipStatus` (enum)
- `FriendRequestSentEvent` (event)
- `FriendRequestAcceptedEvent` (event)
- `FriendRequestRejectedEvent` (event)
- `FriendRepository` (interface)
- `FriendshipDomainService` (service)

### Application Layer

- `FriendshipService` (class)
- `FriendQueryService` (class)
- `FriendRequestDto` (DTO)
- `FriendActionDto` (DTO)
- `FriendshipResponseDto` (DTO)
- `FriendDto` (DTO)
- `FriendshipMapper` (mapper)

### Infrastructure Layer

- `FriendshipJpaEntity` (JPA entity)
- `FriendshipJpaRepository` (JPA repository)
- `FriendRepositoryAdapter` (adapter)
- `FriendSuggestionService` (service)
- `FriendController` (controller)
- `FriendSuggestionController` (controller)

## Basic Wardrobe

### Domain Layer

- `Wardrobe` (entity)
- `Item` (entity)
- `Category` (value object)
- `ItemAttribute` (value object)
- `ItemImage` (value object)
- `WardrobeCreatedEvent` (event)
- `ItemAddedEvent` (event)
- `ItemUpdatedEvent` (event)
- `WardrobeRepository` (interface)
- `ItemRepository` (interface)

### Application Layer

- `WardrobeService` (class)
- `ItemService` (class)
- `ItemImageService` (class)
- `CreateWardrobeRequest` (DTO)
- `AddItemRequest` (DTO)
- `WardrobeResponseDto` (DTO)
- `ItemResponseDto` (DTO)
- `WardrobeMapper` (mapper)
- `ItemMapper` (mapper)

### Infrastructure Layer

- `WardrobeJpaEntity` (JPA entity)
- `ItemJpaEntity` (JPA entity)
- `CategoryJpaEntity` (JPA entity)
- `ItemAttributeJpaEntity` (JPA entity)
- `ItemImageJpaEntity` (JPA entity)
- `WardrobeJpaRepository` (JPA repository)
- `ItemJpaRepository` (JPA repository)
- `CategoryJpaRepository` (JPA repository)
- `WardrobeRepositoryAdapter` (adapter)
- `ItemRepositoryAdapter` (adapter)
- `ItemImageProcessor` (service)
- `WardrobeController` (controller)
- `ItemController` (controller)
- `ItemImageController` (controller)

## Outfit Creation

### Domain Layer

- `Outfit` (entity)
- `OutfitItem` (entity)
- `OutfitOccasion` (value object)
- `Season` (value object)
- `ItemPosition` (value object)
- `OutfitCreatedEvent` (event)
- `OutfitUpdatedEvent` (event)
- `ItemAddedToOutfitEvent` (event)
- `OutfitRepository` (interface)

### Application Layer

- `OutfitService` (class)
- `OutfitItemService` (class)
- `CreateOutfitRequest` (DTO)
- `UpdateOutfitRequest` (DTO)
- `AddItemToOutfitRequest` (DTO)
- `OutfitResponseDto` (DTO)
- `OutfitItemResponseDto` (DTO)
- `OutfitMapper` (mapper)
- `OutfitItemMapper` (mapper)

### Infrastructure Layer

- `OutfitJpaEntity` (JPA entity)
- `OutfitItemJpaEntity` (JPA entity)
- `OutfitJpaRepository` (JPA repository)
- `OutfitItemJpaRepository` (JPA repository)
- `OutfitRepositoryAdapter` (adapter)
- `OutfitImageGenerator` (service)
- `OutfitController` (controller)
- `OutfitItemController` (controller)

## Visibility Controls

### Domain Layer

- `PrivacySettings` (entity)
- `VisibilityLevel` (enum)
- `EntityType` (enum)
- `PermissionEvaluator` (service)
- `PrivacySettingsRepository` (interface)

### Application Layer

- `PrivacyService` (class)
- `VisibilityQueryService` (class)
- `UpdatePrivacyRequest` (DTO)
- `PrivacySettingsResponseDto` (DTO)
- `PrivacySettingsMapper` (mapper)

### Infrastructure Layer

- `PrivacySettingsJpaEntity` (JPA entity)
- `PrivacySettingsJpaRepository` (JPA repository)
- `PrivacySettingsRepositoryAdapter` (adapter)
- `CustomPermissionEvaluator` (service)
- `PrivacyController` (controller)
- `VisibilityCheck` (annotation)

## Configuration and Setup

- `ApplicationConfig` (configuration)
- `SecurityConfig` (configuration)
- `PersistenceConfig` (configuration)
- `WebConfig` (configuration) 