# Domain Events Implementation Guide

## Overview

This guide outlines the implementation of domain events and application events in the Lookbook API, following Domain-Driven Design principles and Clean Architecture.

Domain Entity -> Domain Event -> Application Service -> Application Event -> Event Listeners

## 1. Event Types

### 1.1 Domain Events

Domain events represent facts about what happened in the domain. They are:
- Immutable
- Focused on domain facts
- Part of the domain model
- Raised by domain entities

```java
// Example Domain Event
public class UserRegisteredEvent extends BaseDomainEvent {
    private final UUID userId;
    private final String username;
    private final String email;
    
    public UserRegisteredEvent(UUID userId, String username, String email) {
        super(Map.of(
            "userId", userId,
            "username", username,
            "email", email));
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
```

### 1.2 Application Events

Application events represent actions to be taken as a result of domain events. They are:
- Part of the application layer
- Focused on side effects
- Transaction-aware
- Handled by event listeners

```java
// Example Application Event
public class SendWelcomeEmailEvent {
    private final String email;
    private final String username;
    
    public SendWelcomeEmailEvent(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
```

## 2. Event Flow

### 2.1 Domain Layer

1. **Event Definition**:
   ```java
   public class User {
       public User(UUID id, String username, String email) {
           // Domain logic
           addDomainEvent(new UserRegisteredEvent(id, username, email));
       }
   }
   ```

2. **Event Collection**:
   ```java
   public abstract class BaseEntity {
       private List<DomainEvent> domainEvents = new ArrayList<>();
       
       protected void addDomainEvent(DomainEvent event) {
           domainEvents.add(event);
       }
       
       public List<DomainEvent> getDomainEvents() {
           return Collections.unmodifiableList(domainEvents);
       }
       
       public void clearDomainEvents() {
           domainEvents.clear();
       }
   }
   ```

### 2.2 Application Layer

1. **Service Layer**:
   ```java
   @Service
   @Transactional
   public class UserService {
       private final UserRepository userRepository;
       private final ApplicationEventPublisher eventPublisher;
       
       public User registerUser(RegisterUserRequest request) {
           // Create and validate user
           User user = new User(UUID.randomUUID(), request.getUsername(), request.getEmail());
           
           // Save user in same transaction
           user = userRepository.save(user);
           
           // Publish application event for side effects
           eventPublisher.publishEvent(new SendWelcomeEmailEvent(user.getEmail(), user.getUsername()));
           
           return user;
       }
   }
   ```

2. **Event Listeners**:
   ```java
   @Component
   public class UserRegistrationListener {
       private final EmailService emailService;
       
       @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
       public void handleUserRegistered(SendWelcomeEmailEvent event) {
           emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
       }
   }
   ```

## 3. Best Practices

### 3.1 Domain Events

1. **Immutability**:
   - Events should be immutable
   - Use final fields
   - No setters

2. **Focused Data**:
   - Include only relevant domain facts
   - Avoid application-specific data
   - Keep events small and focused

3. **Naming**:
   - Use past tense (e.g., `UserRegisteredEvent`)
   - Be specific about what happened
   - Follow domain language

### 3.2 Application Events

1. **Transaction Awareness**:
   - Use `@TransactionalEventListener`
   - Specify transaction phase
   - Handle failures appropriately

2. **Side Effects**:
   - Keep listeners focused
   - Handle one type of side effect
   - Log important actions

3. **Error Handling**:
   - Implement retry mechanisms
   - Log failures
   - Monitor processing

## 4. Testing

### 4.1 Domain Event Tests

```java
@DisplayName("UserRegisteredEvent")
class UserRegisteredEventTest {
    @Test
    void shouldCreateEventWithValidData() {
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";
        
        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);
        
        assertEquals(userId, event.getUserId());
        assertEquals(username, event.getUsername());
        assertEquals(email, event.getEmail());
    }
}
```

### 4.2 Application Event Tests

```java
@ExtendWith(MockitoExtension.class)
class UserRegistrationListenerTest {
    @Mock
    private EmailService emailService;
    
    private UserRegistrationListener listener;
    
    @BeforeEach
    void setUp() {
        listener = new UserRegistrationListener(emailService);
    }
    
    @Test
    void shouldSendWelcomeEmail() {
        // Given
        SendWelcomeEmailEvent event = new SendWelcomeEmailEvent("test@example.com", "testuser");
        
        // When
        listener.handleUserRegistered(event);
        
        // Then
        verify(emailService).sendWelcomeEmail("test@example.com", "testuser");
    }
}
```

## 5. Implementation Steps

1. **Domain Layer**:
   - Define domain events
   - Add event collection to entities
   - Implement event raising

2. **Application Layer**:
   - Define application events
   - Update services to publish events
   - Implement event listeners

3. **Infrastructure Layer**:
   - Configure transaction management
   - Set up event processing
   - Implement monitoring

4. **Testing**:
   - Test domain events
   - Test application events
   - Test complete flows

## 6. Monitoring and Maintenance

1. **Event Processing**:
   - Monitor event processing status
   - Track processing times
   - Alert on failures

2. **Performance**:
   - Monitor event volume
   - Track processing latency
   - Optimize as needed

3. **Maintenance**:
   - Regular event cleanup
   - Monitor storage usage
   - Update documentation 