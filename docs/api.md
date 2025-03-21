# API Documentation

This document provides detailed information about the Lookbook API endpoints, including request/response formats, authentication, and examples.

## Base URL

All API endpoints are prefixed with: `/api/v1`

## Authentication

Most endpoints require authentication via JWT tokens.

### Authentication Flow

1. Register a user account
2. Login to get access and refresh tokens
3. Include the access token in the `Authorization` header for protected requests
4. Use the refresh token to get a new access token when it expires

### Authorization Header Format

```
Authorization: Bearer {access_token}
```

## Endpoints

### Authentication

#### Register User

Creates a new user account.

- **URL**: `/auth/register`
- **Method**: `POST`
- **Auth Required**: No
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password1!"
}
```

**Success Response (201 Created)**:
```json
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresAt": "2025-03-22T16:30:00Z",
    "user": {
      "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
      "username": "johndoe",
      "email": "john@example.com",
      "status": "ACTIVE",
      "createdAt": "2025-03-21T16:30:00Z",
      "updatedAt": "2025-03-21T16:30:00Z"
    }
  }
}
```

**Error Response (400 Bad Request)**:
```json
{
  "status": "error",
  "message": "Username already exists",
  "path": "/api/v1/auth/register"
}
```

#### Login

Authenticates a user and provides tokens.

- **URL**: `/auth/login`
- **Method**: `POST`
- **Auth Required**: No
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "username": "johndoe",
  "password": "Password1!"
}
```

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "User authenticated successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresAt": "2025-03-22T16:30:00Z",
    "user": {
      "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
      "username": "johndoe",
      "email": "john@example.com",
      "status": "ACTIVE",
      "createdAt": "2025-03-21T16:30:00Z",
      "updatedAt": "2025-03-21T16:30:00Z"
    }
  }
}
```

**Error Response (401 Unauthorized)**:
```json
{
  "status": "error",
  "message": "Invalid username or password",
  "path": "/api/v1/auth/login"
}
```

#### Refresh Token

Refreshes an access token using a valid refresh token.

- **URL**: `/auth/refresh`
- **Method**: `POST`
- **Auth Required**: No
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresAt": "2025-03-22T16:30:00Z"
  }
}
```

**Error Response (401 Unauthorized)**:
```json
{
  "status": "error",
  "message": "Invalid refresh token",
  "path": "/api/v1/auth/refresh"
}
```

### User Management

#### Get Current User

Retrieves the currently authenticated user's profile.

- **URL**: `/users/me`
- **Method**: `GET`
- **Auth Required**: Yes

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "john@example.com",
    "status": "ACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T16:30:00Z"
  }
}
```

**Error Response (401 Unauthorized)**:
```json
{
  "status": "error",
  "message": "Not authenticated",
  "path": "/api/v1/users/me"
}
```

#### Get User by ID

Retrieves a user by their ID.

- **URL**: `/users/{id}`
- **Method**: `GET`
- **Auth Required**: Yes
- **URL Parameters**: `id=[UUID]`

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "john@example.com",
    "status": "ACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T16:30:00Z"
  }
}
```

**Error Response (404 Not Found)**:
```json
{
  "status": "error",
  "message": "User with ID 1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p not found",
  "path": "/api/v1/users/1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p"
}
```

#### Get All Users

Retrieves a list of all users (admin only).

- **URL**: `/users`
- **Method**: `GET`
- **Auth Required**: Yes (Admin role)

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "data": [
    {
      "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
      "username": "johndoe",
      "email": "john@example.com",
      "status": "ACTIVE",
      "createdAt": "2025-03-21T16:30:00Z",
      "updatedAt": "2025-03-21T16:30:00Z"
    },
    {
      "id": "2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q",
      "username": "janedoe",
      "email": "jane@example.com",
      "status": "ACTIVE",
      "createdAt": "2025-03-21T16:45:00Z",
      "updatedAt": "2025-03-21T16:45:00Z"
    }
  ]
}
```

**Error Response (403 Forbidden)**:
```json
{
  "status": "error",
  "message": "Access denied",
  "path": "/api/v1/users"
}
```

#### Update Email

Updates a user's email address.

- **URL**: `/users/{id}/email`
- **Method**: `PUT`
- **Auth Required**: Yes
- **URL Parameters**: `id=[UUID]`
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "email": "newemail@example.com"
}
```

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "Email updated successfully",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "newemail@example.com",
    "status": "ACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T17:00:00Z"
  }
}
```

**Error Response (400 Bad Request)**:
```json
{
  "status": "error",
  "message": "Invalid email format",
  "path": "/api/v1/users/1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p/email"
}
```

#### Change Password

Changes a user's password.

- **URL**: `/users/{id}/password`
- **Method**: `PUT`
- **Auth Required**: Yes
- **URL Parameters**: `id=[UUID]`
- **Content-Type**: `application/json`

**Request Body**:
```json
{
  "currentPassword": "Password1!",
  "newPassword": "NewPassword2!"
}
```

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "Password changed successfully",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "john@example.com",
    "status": "ACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T17:15:00Z"
  }
}
```

**Error Response (400 Bad Request)**:
```json
{
  "status": "error",
  "message": "Current password is incorrect",
  "path": "/api/v1/users/1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p/password"
}
```

#### Activate User

Activates a user account.

- **URL**: `/users/{id}/activate`
- **Method**: `POST`
- **Auth Required**: Yes (Admin role)
- **URL Parameters**: `id=[UUID]`

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "User activated successfully",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "john@example.com",
    "status": "ACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T17:30:00Z"
  }
}
```

**Error Response (404 Not Found)**:
```json
{
  "status": "error",
  "message": "User with ID 1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p not found",
  "path": "/api/v1/users/1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p/activate"
}
```

#### Deactivate User

Deactivates a user account.

- **URL**: `/users/{id}/deactivate`
- **Method**: `POST`
- **Auth Required**: Yes (Admin role)
- **URL Parameters**: `id=[UUID]`

**Success Response (200 OK)**:
```json
{
  "status": "success",
  "message": "User deactivated successfully",
  "data": {
    "id": "1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p",
    "username": "johndoe",
    "email": "john@example.com",
    "status": "INACTIVE",
    "createdAt": "2025-03-21T16:30:00Z",
    "updatedAt": "2025-03-21T17:45:00Z"
  }
}
```

**Error Response (404 Not Found)**:
```json
{
  "status": "error",
  "message": "User with ID 1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p not found",
  "path": "/api/v1/users/1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p/deactivate"
}
```

## Error Responses

### Standard Error Format

All API errors follow a standard format:

```json
{
  "status": "error",
  "message": "Description of what went wrong",
  "path": "/api/v1/endpoint/path",
  "timestamp": "2025-03-21T16:30:00Z"
}
```

### Common Error Codes

| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource does not exist |
| 422 | Unprocessable Entity - Validation failed |
| 500 | Internal Server Error - Server-side issue |

### Validation Errors

For validation errors, additional field-specific errors are provided:

```json
{
  "status": "error",
  "message": "Validation failed",
  "path": "/api/v1/auth/register",
  "timestamp": "2025-03-21T16:30:00Z",
  "errors": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Invalid email format",
    "password": "Password must contain at least 8 characters, one uppercase, one lowercase, and one digit"
  }
}
```

## Rate Limiting

The API implements rate limiting to protect against abuse:

- **Authenticated users**: 60 requests per minute
- **Anonymous users**: 30 requests per minute

When rate limited, the response will include headers with limit information:
- `X-RateLimit-Limit`: Maximum requests per window
- `X-RateLimit-Remaining`: Remaining requests in current window
- `X-RateLimit-Reset`: Time when the rate limit resets (Unix timestamp)

**Rate Limit Exceeded Response (429 Too Many Requests)**:
```json
{
  "status": "error",
  "message": "Rate limit exceeded. Try again later.",
  "path": "/api/v1/endpoint/path"
}
``` 