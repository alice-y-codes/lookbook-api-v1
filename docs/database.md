# Database Documentation

This document provides detailed information about the Lookbook API database schema, including tables, relationships, and migrations.

## Database Schema

The application uses a relational database with the following tables:

### Core Tables

#### Users Table

The `users` table stores user account information and credentials.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| created_at | TIMESTAMP | When the user was created |
| updated_at | TIMESTAMP | When the user was last updated |
| username | VARCHAR(50) | Unique username for login and identification |
| email | VARCHAR(100) | User email address (unique) |
| password_hash | VARCHAR(255) | Bcrypt hash of the user password |
| password_salt | VARCHAR(255) | Salt used for password hashing |
| status | VARCHAR(20) | Current status: ACTIVE, INACTIVE, or PENDING |

**Indexes:**
- `idx_users_username`: For fast username lookups
- `idx_users_email`: For fast email lookups
- `idx_users_status`: For filtering users by status

#### Refresh Tokens Table

The `refresh_tokens` table stores JWT refresh tokens for authentication.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| created_at | TIMESTAMP | When the token was created |
| updated_at | TIMESTAMP | When the token was last updated |
| token | VARCHAR(255) | The actual refresh token value (unique) |
| user_id | UUID | Foreign key to users.id |
| expires_at | TIMESTAMP | When the token expires |
| revoked | BOOLEAN | Whether the token has been revoked |

**Indexes:**
- `idx_refresh_tokens_token`: For fast token lookups
- `idx_refresh_tokens_user_id`: For finding a user's tokens
- `idx_refresh_tokens_expires_at`: For token cleanup

**Foreign Keys:**
- `fk_refresh_tokens_user`: `user_id` references `users.id` (CASCADE)

### Authorization Tables

#### Roles Table

The `roles` table defines user roles in the system.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| created_at | TIMESTAMP | When the role was created |
| updated_at | TIMESTAMP | When the role was last updated |
| name | VARCHAR(50) | Unique role name (e.g., ADMIN, USER) |
| description | VARCHAR(255) | Description of the role |

**Default Roles:**
- `ADMIN`: Administrator with full access
- `USER`: Regular user with standard permissions

#### Permissions Table

The `permissions` table defines granular permissions in the system.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| created_at | TIMESTAMP | When the permission was created |
| updated_at | TIMESTAMP | When the permission was last updated |
| name | VARCHAR(100) | Unique permission name (e.g., USER_READ) |
| description | VARCHAR(255) | Description of the permission |

**Default Permissions:**
- `USER_READ`: Can view user details
- `USER_CREATE`: Can create users
- `USER_UPDATE`: Can update users
- `USER_DELETE`: Can delete users
- `USER_ADMIN`: Can perform administrative actions on users

#### Role Permissions Table

The `role_permissions` table maps roles to their granted permissions.

| Column | Type | Description |
|--------|------|-------------|
| role_id | UUID | Foreign key to roles.id |
| permission_id | UUID | Foreign key to permissions.id |

**Primary Key:** Composite key (role_id, permission_id)

**Foreign Keys:**
- `fk_role_permissions_role`: `role_id` references `roles.id` (CASCADE)
- `fk_role_permissions_permission`: `permission_id` references `permissions.id` (CASCADE)

#### User Roles Table

The `user_roles` table maps users to their assigned roles.

| Column | Type | Description |
|--------|------|-------------|
| user_id | UUID | Foreign key to users.id |
| role_id | UUID | Foreign key to roles.id |

**Primary Key:** Composite key (user_id, role_id)

**Foreign Keys:**
- `fk_user_roles_user`: `user_id` references `users.id` (CASCADE)
- `fk_user_roles_role`: `role_id` references `roles.id` (CASCADE)

## Entity Relationship Diagram

```
+----------------+       +------------------+
|     users      |       |  refresh_tokens  |
+----------------+       +------------------+
| id (PK)        |       | id (PK)          |
| username       |       | token            |
| email          |<------| user_id (FK)     |
| password_hash  |       | expires_at       |
| password_salt  |       | revoked          |
| status         |       +------------------+
+----------------+
       |
       |
+------+------+
|             |
|             |
v             v
+--------+    +------------+
| roles  |    | user_roles |
+--------+    +------------+
| id (PK)|<---| role_id(FK)|
| name   |    | user_id(FK)|
+--------+    +------------+
       |
       |
+------+------+
|             |
v             |
+---------------+    +----------------+
|  permissions  |    |role_permissions|
+---------------+    +----------------+
| id (PK)       |<---| permission_id  |
| name          |    | role_id        |
+---------------+    +----------------+
```

## Database Migrations

All database changes are managed through Flyway migrations. 

### Migration Files

| File | Description |
|------|-------------|
| V1__Create_users_table.sql | Creates the users table |
| V2__Create_refresh_tokens_table.sql | Creates the refresh tokens table |
| V3__Create_roles_and_permissions.sql | Creates the roles, permissions, and joins tables |
| V4__Assign_default_role_permissions.sql | Assigns default permissions to roles |
| V5__Add_test_user.sql | Adds initial test users |
| R__Test_Data.sql | Adds repeatable test data for development |

## Development & Testing

### Production Database

The production environment uses PostgreSQL. Configuration is in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lookbook
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
```

### Test Database

Tests use H2 in-memory database. Configuration is in `application-test.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=none
```

## Adding New Migrations

When making database changes:

1. Create a new migration script in `src/main/resources/db/migration`
2. Name it following the pattern `V{number}__{description}.sql`
3. Write the SQL changes (both forward and rollback if possible)
4. Test the migration by running the application

## Best Practices

1. **Never modify existing migrations**: Create new ones instead
2. **Test migrations thoroughly**: Ensure they work in both directions
3. **Keep migrations atomic**: Each should do one thing
4. **Document changes**: Add comments to SQL and update this document
5. **Include indexes**: Add appropriate indexes for performance 