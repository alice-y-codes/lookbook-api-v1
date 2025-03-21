# Feature Development Guide

This document provides a comprehensive step-by-step guide on how to develop a new feature for the Lookbook API.

## Feature Development Workflow

### 1. Feature Planning

Before writing any code, follow these planning steps:

1. **Create a feature branch**:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/your-feature-name
   ```

2. **Define requirements**:
   - Document the feature's purpose and scope
   - Define acceptance criteria
   - Identify edge cases and error scenarios

3. **Plan database changes**:
   - Determine if new tables are needed
   - Plan for schema migrations
   - Create Flyway migration scripts if needed

### 2. Domain Layer Implementation

Start with the domain model, which represents the core business logic:

1. **Identify domain entities and value objects**:
   - Create domain entities in `com.lookbook.[feature].domain.model`
   - Implement value objects for complex attributes
   - Example for a new "comment" feature:
     ```java
     // com.lookbook.comment.domain.model.Comment.java
     public class Comment extends BaseEntity {
         private final UUID authorId;
         private final UUID targetId;
         private final CommentType targetType;
         private CommentContent content;
         private CommentStatus status;
         private final LocalDateTime createdAt;
         private LocalDateTime updatedAt;
         
         // Constructor, getters, methods for domain logic
         
         public void updateContent(CommentContent newContent) {
             this.content = newContent;
             this.updatedAt = LocalDateTime.now();
         }
         
         public void archive() {
             this.status = CommentStatus.ARCHIVED;
             this.updatedAt = LocalDateTime.now();
         }
     }
     ```

2. **Define domain events**:
   - Create event classes in `com.lookbook.[feature].domain.events`
   - Example:
     ```java
     // com.lookbook.comment.domain.events.CommentCreatedEvent.java
     public class CommentCreatedEvent extends BaseDomainEvent {
         private final UUID commentId;
         private final UUID authorId;
         private final UUID targetId;
         
         // Constructor, getters
     }
     ```

3. **Create repository interfaces**:
   - Define repository interfaces in `com.lookbook.[feature].domain.repositories`
   - Example:
     ```java
     // com.lookbook.comment.domain.repositories.CommentRepository.java
     public interface CommentRepository {
         Optional<Comment> findById(UUID id);
         List<Comment> findByTargetId(UUID targetId, CommentType targetType);
         Comment save(Comment comment);
         void delete(UUID id);
     }
     ```

4. **Implement domain services**:
   - Create domain services for complex operations
   - Example:
     ```java
     // com.lookbook.comment.domain.services.CommentDomainService.java
     public class CommentDomainService {
         public boolean validateComment(Comment comment) {
             // Domain validation logic
             return !comment.getContent().getValue().isEmpty();
         }
     }
     ```

5. **Write unit tests**:
   - Create tests for domain entities, value objects, and services
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/domain/model/CommentTest.java
     public class CommentTest {
         @Test
         public void updateContent_ShouldChangeContentAndUpdateTimestamp() {
             // Test code
         }
     }
     ```

### 3. Application Layer Implementation

The application layer orchestrates the domain layer and implements use cases:

1. **Create DTOs**:
   - Create request/response DTOs in `com.lookbook.[feature].application.dto`
   - Example:
     ```java
     // com.lookbook.comment.application.dto.request.CreateCommentRequest.java
     public record CreateCommentRequest(
         UUID authorId,
         UUID targetId, 
         String targetType,
         String content
     ) {}
     
     // com.lookbook.comment.application.dto.response.CommentResponse.java
     public record CommentResponse(
         UUID id,
         UUID authorId,
         UUID targetId,
         String targetType,
         String content,
         String status,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
     ) {}
     ```

2. **Create mappers**:
   - Implement mapper interfaces in `com.lookbook.[feature].application.mappers`
   - Use MapStruct for automatic implementation
   - Example:
     ```java
     // com.lookbook.comment.application.mappers.CommentMapper.java
     @Mapper(componentModel = "spring")
     public interface CommentMapper {
         CommentResponse toResponse(Comment comment);
         List<CommentResponse> toResponseList(List<Comment> comments);
         
         Comment toDomain(CreateCommentRequest request);
     }
     ```

3. **Implement application services**:
   - Create service classes in `com.lookbook.[feature].application.services`
   - Inject domain repositories and services
   - Example:
     ```java
     // com.lookbook.comment.application.services.CommentService.java
     @Service
     public class CommentService {
         private final CommentRepository commentRepository;
         private final CommentMapper commentMapper;
         private final CommentDomainService domainService;
         
         public CommentService(CommentRepository commentRepository, 
                            CommentMapper commentMapper,
                            CommentDomainService domainService) {
             this.commentRepository = commentRepository;
             this.commentMapper = commentMapper;
             this.domainService = domainService;
         }
         
         @Transactional
         public CommentResponse createComment(CreateCommentRequest request) {
             Comment comment = commentMapper.toDomain(request);
             
             if (!domainService.validateComment(comment)) {
                 throw new ValidationException("Invalid comment content");
             }
             
             Comment savedComment = commentRepository.save(comment);
             return commentMapper.toResponse(savedComment);
         }
         
         // Other methods for listing, updating, deleting comments
     }
     ```

4. **Write unit tests**:
   - Test application services with mocked dependencies
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/application/services/CommentServiceTest.java
     @ExtendWith(MockitoExtension.class)
     public class CommentServiceTest {
         @Mock
         private CommentRepository commentRepository;
         
         @Mock
         private CommentMapper commentMapper;
         
         @Mock
         private CommentDomainService domainService;
         
         @InjectMocks
         private CommentService commentService;
         
         @Test
         public void createComment_ValidRequest_ShouldReturnCommentResponse() {
             // Test code with Mockito
         }
     }
     ```

### 4. Infrastructure Layer Implementation

Implement the infrastructure components:

1. **Create JPA entities**:
   - Create JPA entities in `com.lookbook.[feature].infrastructure.persistence.entities`
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.persistence.entities.CommentJpaEntity.java
     @Entity
     @Table(name = "comments")
     public class CommentJpaEntity {
         @Id
         private UUID id;
         
         @Column(name = "author_id", nullable = false)
         private UUID authorId;
         
         @Column(name = "target_id", nullable = false)
         private UUID targetId;
         
         @Enumerated(EnumType.STRING)
         @Column(name = "target_type", nullable = false)
         private CommentTypeJpa targetType;
         
         @Column(name = "content", nullable = false)
         private String content;
         
         @Enumerated(EnumType.STRING)
         @Column(name = "status", nullable = false)
         private CommentStatusJpa status;
         
         @Column(name = "created_at", nullable = false)
         private LocalDateTime createdAt;
         
         @Column(name = "updated_at")
         private LocalDateTime updatedAt;
         
         // Getters and setters
     }
     ```

2. **Create JPA repositories**:
   - Implement Spring Data JPA repositories in `com.lookbook.[feature].infrastructure.persistence.repositories`
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.persistence.repositories.CommentJpaRepository.java
     public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, UUID> {
         List<CommentJpaEntity> findByTargetIdAndTargetType(UUID targetId, CommentTypeJpa targetType);
     }
     ```

3. **Create JPA mappers**:
   - Implement mappers in `com.lookbook.[feature].infrastructure.persistence.mappers`
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.persistence.mappers.CommentJpaMapper.java
     @Mapper(componentModel = "spring")
     public interface CommentJpaMapper {
         CommentJpaEntity toJpaEntity(Comment comment);
         Comment toDomain(CommentJpaEntity entity);
         List<Comment> toDomainList(List<CommentJpaEntity> entities);
     }
     ```

4. **Implement repository adapters**:
   - Create repository implementations in `com.lookbook.[feature].infrastructure.persistence.adapters`
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.persistence.adapters.CommentRepositoryAdapter.java
     @Repository
     public class CommentRepositoryAdapter implements CommentRepository {
         private final CommentJpaRepository jpaRepository;
         private final CommentJpaMapper jpaMapper;
         
         public CommentRepositoryAdapter(CommentJpaRepository jpaRepository, 
                                      CommentJpaMapper jpaMapper) {
             this.jpaRepository = jpaRepository;
             this.jpaMapper = jpaMapper;
         }
         
         @Override
         public Optional<Comment> findById(UUID id) {
             return jpaRepository.findById(id)
                     .map(jpaMapper::toDomain);
         }
         
         @Override
         public List<Comment> findByTargetId(UUID targetId, CommentType targetType) {
             CommentTypeJpa typeJpa = CommentTypeJpa.valueOf(targetType.name());
             List<CommentJpaEntity> entities = jpaRepository.findByTargetIdAndTargetType(targetId, typeJpa);
             return jpaMapper.toDomainList(entities);
         }
         
         @Override
         public Comment save(Comment comment) {
             CommentJpaEntity entity = jpaMapper.toJpaEntity(comment);
             CommentJpaEntity savedEntity = jpaRepository.save(entity);
             return jpaMapper.toDomain(savedEntity);
         }
         
         @Override
         public void delete(UUID id) {
             jpaRepository.deleteById(id);
         }
     }
     ```

5. **Create Flyway migrations**:
   - Add migration scripts in `src/main/resources/db/migration`
   - Example:
     ```sql
     -- src/main/resources/db/migration/V5__create_comments_table.sql
     CREATE TABLE comments (
         id UUID PRIMARY KEY,
         author_id UUID NOT NULL,
         target_id UUID NOT NULL,
         target_type VARCHAR(50) NOT NULL,
         content TEXT NOT NULL,
         status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
         created_at TIMESTAMP NOT NULL,
         updated_at TIMESTAMP,
         CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id)
     );
     
     CREATE INDEX idx_comments_target ON comments(target_id, target_type);
     ```

6. **Write repository integration tests**:
   - Test repository implementations with test containers
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/infrastructure/persistence/adapters/CommentRepositoryAdapterTest.java
     @DataJpaTest
     @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
     @Testcontainers
     public class CommentRepositoryAdapterTest {
         @Container
         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
                 .withDatabaseName("testdb")
                 .withUsername("test")
                 .withPassword("test");
                 
         // Test code
     }
     ```

### 5. API Layer Implementation

Create the REST API endpoints:

1. **Implement controllers**:
   - Create controllers in `com.lookbook.[feature].infrastructure.api.controllers`
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.api.controllers.CommentController.java
     @RestController
     @RequestMapping("/api/v1/comments")
     public class CommentController {
         private final CommentService commentService;
         
         public CommentController(CommentService commentService) {
             this.commentService = commentService;
         }
         
         @PostMapping
         @ResponseStatus(HttpStatus.CREATED)
         public ApiResponse<CommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
             CommentResponse response = commentService.createComment(request);
             return ApiResponse.success("Comment created successfully", response);
         }
         
         @GetMapping("/target/{targetId}")
         public ApiResponse<List<CommentResponse>> getCommentsByTarget(
                 @PathVariable UUID targetId,
                 @RequestParam String targetType) {
             List<CommentResponse> comments = commentService.getCommentsByTarget(targetId, targetType);
             return ApiResponse.success("Comments retrieved successfully", comments);
         }
         
         @PutMapping("/{id}")
         public ApiResponse<CommentResponse> updateComment(
                 @PathVariable UUID id,
                 @RequestBody @Valid UpdateCommentRequest request) {
             CommentResponse response = commentService.updateComment(id, request);
             return ApiResponse.success("Comment updated successfully", response);
         }
         
         @DeleteMapping("/{id}")
         @ResponseStatus(HttpStatus.NO_CONTENT)
         public ApiResponse<Void> deleteComment(@PathVariable UUID id) {
             commentService.deleteComment(id);
             return ApiResponse.success("Comment deleted successfully");
         }
     }
     ```

2. **Implement exception handlers**:
   - Create custom exception handlers if needed
   - Example:
     ```java
     // com.lookbook.comment.infrastructure.api.exceptions.CommentExceptionHandler.java
     @RestControllerAdvice
     public class CommentExceptionHandler {
         @ExceptionHandler(CommentNotFoundException.class)
         @ResponseStatus(HttpStatus.NOT_FOUND)
         public ApiResponse<Void> handleCommentNotFoundException(CommentNotFoundException ex) {
             return ApiResponse.error(ex.getMessage());
         }
     }
     ```

3. **Document API with OpenAPI**:
   - Add OpenAPI annotations to controllers
   - Example:
     ```java
     @Operation(summary = "Create a new comment", 
                description = "Creates a new comment for the specified target")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "201", 
                     description = "Comment created successfully",
                     content = @Content(schema = @Schema(implementation = CommentResponse.class))),
         @ApiResponse(responseCode = "400", 
                     description = "Invalid input data",
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
         @ApiResponse(responseCode = "401", 
                     description = "Unauthorized", 
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
     })
     @PostMapping
     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse<CommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
         // Method implementation
     }
     ```

4. **Write API tests**:
   - Test controllers with MockMvc
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/infrastructure/api/controllers/CommentControllerTest.java
     @WebMvcTest(CommentController.class)
     public class CommentControllerTest {
         @Autowired
         private MockMvc mockMvc;
         
         @MockBean
         private CommentService commentService;
         
         @Test
         public void createComment_ValidRequest_ShouldReturnCreated() throws Exception {
             // Test code with MockMvc
         }
     }
     ```

### 6. Integration and End-to-End Testing

Implement integration and E2E tests:

1. **Create integration tests**:
   - Test multiple components together
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/integration/CommentIntegrationTest.java
     @SpringBootTest
     @ActiveProfiles("test")
     public class CommentIntegrationTest {
         @Autowired
         private CommentService commentService;
         
         @Autowired
         private CommentRepository commentRepository;
         
         @Test
         public void createAndRetrieveComment_ShouldWorkEndToEnd() {
             // Test code
         }
     }
     ```

2. **Create end-to-end tests**:
   - Test the complete feature flow
   - Example:
     ```java
     // src/test/java/com/lookbook/comment/e2e/CommentE2ETest.java
     @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
     @Testcontainers
     @ActiveProfiles("test")
     public class CommentE2ETest {
         @Container
         static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
                 .withDatabaseName("testdb")
                 .withUsername("test")
                 .withPassword("test");
                 
         @Autowired
         private TestRestTemplate restTemplate;
         
         @Test
         public void commentLifecycle_ShouldWorkEndToEnd() {
             // Test code using TestRestTemplate
         }
     }
     ```

### 7. Documentation

Update documentation for the new feature:

1. **Update API documentation**:
   - Add OpenAPI annotations
   - Update Swagger UI documentation

2. **Update README or relevant docs**:
   - Document the new feature
   - Provide usage examples

### 8. Code Review and Submission

Finalize your feature:

1. **Run all tests**:
   ```bash
   ./mvnw test
   ```

2. **Check code quality**:
   ```bash
   ./mvnw verify
   ```

3. **Submit pull request**:
   - Push your branch to the remote repository
   - Create a pull request to merge into develop
   - Fill out the PR template with details about your changes

4. **Address feedback**:
   - Make requested changes
   - Push additional commits to the same branch

5. **Merge**:
   - Once approved, squash and merge into develop

## Feature Development Checklist

Use this checklist to track your progress:

- [ ] Create feature branch
- [ ] Define requirements and acceptance criteria
- [ ] Domain layer:
  - [ ] Entities and value objects
  - [ ] Repository interfaces
  - [ ] Domain events
  - [ ] Domain services
  - [ ] Domain unit tests
- [ ] Application layer:
  - [ ] DTOs
  - [ ] Mappers
  - [ ] Application services
  - [ ] Service unit tests
- [ ] Infrastructure layer:
  - [ ] JPA entities
  - [ ] JPA repositories
  - [ ] Repository adapters
  - [ ] Flyway migrations
  - [ ] Repository integration tests
- [ ] API layer:
  - [ ] Controllers
  - [ ] Exception handlers
  - [ ] API documentation
  - [ ] Controller tests
- [ ] Integration tests
- [ ] End-to-end tests
- [ ] Documentation updates
- [ ] Pull request
- [ ] Code review
- [ ] Merge to develop

## TDD Feature Development Approach

For a Test-Driven Development approach:

1. **Write failing domain unit tests first**
2. **Implement domain models to pass tests**
3. **Write failing application service tests**
4. **Implement application services to pass tests**
5. **Write failing repository tests**
6. **Implement repository adapters to pass tests**
7. **Write failing controller tests**
8. **Implement controllers to pass tests**
9. **Write integration tests**
10. **Fix any integration issues**

## Example: Adding a Comment Feature

Here's a condensed example of adding a comment feature:

1. **Create branch**:
   ```bash
   git checkout -b feature/comments
   ```

2. **Define domain model**:
   ```java
   // Comment.java
   public class Comment extends BaseEntity {
       private final UUID authorId;
       private final UUID targetId;
       private final CommentType targetType;
       private CommentContent content;
       private CommentStatus status;
       private final LocalDateTime createdAt;
       private LocalDateTime updatedAt;
       
       // Methods...
   }
   
   // CommentRepository.java
   public interface CommentRepository {
       Optional<Comment> findById(UUID id);
       List<Comment> findByTargetId(UUID targetId, CommentType targetType);
       Comment save(Comment comment);
       void delete(UUID id);
   }
   ```

3. **Create application service**:
   ```java
   // CommentService.java
   @Service
   public class CommentService {
       private final CommentRepository commentRepository;
       
       // Methods...
       
       public CommentResponse createComment(CreateCommentRequest request) {
           Comment comment = commentMapper.toDomain(request);
           Comment savedComment = commentRepository.save(comment);
           return commentMapper.toResponse(savedComment);
       }
   }
   ```

4. **Create repository adapter**:
   ```java
   // CommentRepositoryAdapter.java
   @Repository
   public class CommentRepositoryAdapter implements CommentRepository {
       private final CommentJpaRepository jpaRepository;
       private final CommentJpaMapper jpaMapper;
       
       // Methods...
   }
   ```

5. **Create controller**:
   ```java
   // CommentController.java
   @RestController
   @RequestMapping("/api/v1/comments")
   public class CommentController {
       private final CommentService commentService;
       
       // Endpoints...
   }
   ```

6. **Update documentation and test**

## Common Pitfalls and Best Practices

### Pitfalls to Avoid

1. **Anemic Domain Model**: Don't create domain models without behavior
2. **Leaky Abstractions**: Don't expose infrastructure concerns to the domain
3. **Monolithic Services**: Keep services focused on specific use cases
4. **Untested Code**: Always write tests for all layers
5. **Poor Error Handling**: Implement proper exception handling

### Best Practices

1. **Domain-First Design**: Start with the domain model
2. **Test-Driven Development**: Write tests before implementation
3. **Clean Architecture**: Maintain proper separation of concerns
4. **Consistent Naming**: Use consistent naming conventions
5. **Comprehensive Documentation**: Document your code and APIs
6. **Small PRs**: Keep pull requests focused and manageable 