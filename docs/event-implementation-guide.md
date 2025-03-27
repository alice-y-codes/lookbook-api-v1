# Domain Events Implementation Guide

## Overview

This guide outlines the complete implementation of domain events in the Lookbook API, following Domain-Driven Design principles and Clean Architecture.

## 1. Event Infrastructure

### 1.1 Base Event Components

```java
// Domain Event Interface
public interface DomainEvent {
    UUID getEventId();
    LocalDateTime getOccurredAt();
    String getEventType();
    Map<String, Object> getMetadata();
}

// Base Event Implementation
public abstract class BaseDomainEvent implements DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredAt;
    private final Map<String, Object> metadata;
    
    protected BaseDomainEvent(Map<String, Object> metadata) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
        this.metadata = Collections.unmodifiableMap(metadata);
    }
}
```

### 1.2 Event Collection

```java
// Base Entity with Event Collection
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

### 1.3 Event Publishing

```java
// Event Publisher Interface
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

// Event Store Interface
public interface EventStore {
    void save(DomainEvent event);
    List<DomainEvent> getEvents(UUID aggregateId);
    List<DomainEvent> getEventsByType(String eventType);
    void markAsProcessed(UUID eventId);
    void markAsFailed(UUID eventId, Exception error);
}
```

## 2. Event Implementation

### 2.1 User Domain Events

```java
// User Registration
public class UserRegisteredEvent extends BaseDomainEvent {
    private final UUID userId;
    private final String username;
    private final String email;
    
    public UserRegisteredEvent(UUID userId, String username, String email) {
        super(createMetadata(userId, username, email));
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}

// User Activation
public class UserActivatedEvent extends BaseDomainEvent {
    private final UUID userId;
    private final String username;
    
    public UserActivatedEvent(UUID userId, String username) {
        super(createMetadata(userId, username));
        this.userId = userId;
        this.username = username;
    }
}
```

### 2.2 Profile Domain Events

```java
// Profile Creation
public class ProfileCreatedEvent extends BaseDomainEvent {
    private final UUID userId;
    private final UUID profileId;
    private final String displayName;
    
    public ProfileCreatedEvent(UUID userId, UUID profileId, String displayName) {
        super(createMetadata(userId, profileId, displayName));
        this.userId = userId;
        this.profileId = profileId;
        this.displayName = displayName;
    }
}

// Profile Update
public class ProfileUpdatedEvent extends BaseDomainEvent {
    private final UUID userId;
    private final UUID profileId;
    private final String displayName;
    private final String biography;
    
    public ProfileUpdatedEvent(UUID userId, UUID profileId, String displayName, String biography) {
        super(createMetadata(userId, profileId, displayName, biography));
        this.userId = userId;
        this.profileId = profileId;
        this.displayName = displayName;
        this.biography = biography;
    }
}
```

## 3. Event Handling

### 3.1 Event Handlers

```java
// Event Handler Interface
public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);
}

// Example Handler Implementation
@Component
public class UserRegistrationHandler implements DomainEventHandler<UserRegisteredEvent> {
    private final EmailService emailService;
    private final NotificationService notificationService;
    
    @Override
    public void handle(UserRegisteredEvent event) {
        // Send welcome email
        emailService.sendWelcomeEmail(event.getUserId());
        
        // Send welcome notification
        notificationService.sendWelcomeNotification(event.getUserId());
    }
}
```

### 3.2 Event Processing

```java
@Service
public class EventProcessor {
    private final EventStore eventStore;
    private final EventHandlers eventHandlers;
    
    @Scheduled(fixedDelay = 1000)
    public void processEvents() {
        List<DomainEvent> events = eventStore.getUnprocessedEvents();
        
        for (DomainEvent event : events) {
            try {
                eventHandlers.handle(event);
                eventStore.markAsProcessed(event.getEventId());
            } catch (Exception e) {
                eventStore.markAsFailed(event.getEventId(), e);
            }
        }
    }
}
```

## 4. Event Persistence

### 4.1 Event Store Implementation

```java
@Entity
@Table(name = "domain_events")
public class JpaDomainEvent {
    @Id
    private UUID eventId;
    private LocalDateTime occurredAt;
    private String eventType;
    private String aggregateId;
    private String eventData; // JSON
    private boolean processed;
    private String errorMessage;
}

@Service
public class JpaEventStore implements EventStore {
    private final EventRepository eventRepository;
    
    @Override
    public void save(DomainEvent event) {
        JpaDomainEvent jpaEvent = new JpaDomainEvent(event);
        eventRepository.save(jpaEvent);
    }
}
```

## 5. Usage in Application Services

### 5.1 Example Service Implementation

```java
@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final DomainEventPublisher eventPublisher;
    
    public void updateProfile(UUID profileId, UpdateProfileRequest request) {
        // Load aggregate
        UserProfile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new EntityNotFoundException(UserProfile.class, profileId));
        
        // Execute domain logic
        profile.updateProfile(request);
        
        // Save changes
        profileRepository.save(profile);
        
        // Publish events
        profile.getDomainEvents().forEach(eventPublisher::publish);
        profile.clearDomainEvents();
    }
}
```

## 6. Testing

### 6.1 Event Tests

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
    
    @Test
    void shouldCreateMetadataWithAllProperties() {
        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);
        Map<String, Object> metadata = event.getMetadata();
        
        assertEquals(userId.toString(), metadata.get("userId"));
        assertEquals(username, metadata.get("username"));
        assertEquals(email, metadata.get("email"));
    }
}
```

### 6.2 Event Handler Tests

```java
@ExtendWith(MockitoExtension.class)
class UserRegistrationHandlerTest {
    @Mock
    private EmailService emailService;
    
    @Mock
    private NotificationService notificationService;
    
    private UserRegistrationHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new UserRegistrationHandler(emailService, notificationService);
    }
    
    @Test
    void shouldSendWelcomeEmailAndNotification() {
        // Given
        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);
        
        // When
        handler.handle(event);
        
        // Then
        verify(emailService).sendWelcomeEmail(userId);
        verify(notificationService).sendWelcomeNotification(userId);
    }
}
```

## 7. Implementation Steps

1. **Infrastructure Setup**
   - Implement base event components
   - Add event collection to BaseEntity
   - Create event publishing interfaces

2. **Event Definition**
   - Define domain events for each significant domain action
   - Implement event classes with proper metadata
   - Add event tests

3. **Event Handling**
   - Create event handlers for each event type
   - Implement event processing infrastructure
   - Add handler tests

4. **Event Persistence**
   - Implement event store
   - Create database schema for events
   - Add persistence tests

5. **Integration**
   - Update application services to publish events
   - Configure event processing
   - Test complete flow

## 8. Best Practices

1. **Event Creation**
   - Events should be immutable
   - Include all relevant data
   - Use meaningful names

2. **Event Handling**
   - Keep handlers focused and single-purpose
   - Handle failures gracefully
   - Log important events

3. **Event Processing**
   - Process events asynchronously
   - Implement retry mechanisms
   - Monitor processing status

4. **Testing**
   - Test event creation
   - Test event handling
   - Test complete flows
   - Test error scenarios

## 9. Monitoring and Maintenance

1. **Event Monitoring**
   - Track event processing status
   - Monitor failed events
   - Set up alerts for processing issues

2. **Event Cleanup**
   - Implement event retention policies
   - Archive old events
   - Clean up failed events

3. **Performance**
   - Monitor event processing latency
   - Optimize event storage
   - Scale event processing as needed 