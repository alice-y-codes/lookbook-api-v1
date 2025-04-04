# Lookbook API Endpoints

## Authentication Endpoints

### Register User
- **URL**: `/api/v1/auth/register`
- **Method**: `POST`
- **Description**: Register a new user account
- **Request Body**:
```json
{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "SecurePassword123!"
}
```
- **Response** (201 Created):
```json
{
    "success": true,
    "message": "User registered successfully",
    "data": {
        "accessToken": "eyJhbGciOiJIUzM4NCJ9...",
        "refreshToken": "eyJhbGciOiJIUzM4NCJ9...",
        "user": {
            "id": "123e4567-e89b-12d3-a456-426614174000",
            "username": "johndoe",
            "email": "john@example.com",
            "status": "ACTIVE"
        }
    }
}
```

### Login
- **URL**: `/api/v1/auth/login`
- **Method**: `POST`
- **Description**: Authenticate user and get access tokens
- **Request Body**:
```json
{
    "usernameOrEmail": "johndoe",
    "password": "securePassword123"
}
```
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "accessToken": "eyJhbGciOiJIUzM4NCJ9...",
        "refreshToken": "eyJhbGciOiJIUzM4NCJ9...",
        "user": {
            "id": "123e4567-e89b-12d3-a456-426614174000",
            "username": "johndoe",
            "email": "john@example.com",
            "status": "ACTIVE"
        }
    }
}
```

### Refresh Token
- **URL**: `/api/v1/auth/refresh`
- **Method**: `POST`
- **Description**: Get new access token using refresh token
- **Request Body**:
```json
{
    "refreshToken": "eyJhbGciOiJIUzM4NCJ9..."
}
```
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Token refreshed successfully",
    "data": {
        "accessToken": "eyJhbGciOiJIUzM4NCJ9..."
    }
}
```

## User Endpoints

### Get Current User Profile
- **URL**: `/api/v1/users/me`
- **Method**: `GET`
- **Description**: Get the current authenticated user's profile
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "User profile retrieved successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "john@example.com",
        "status": "ACTIVE"
    }
}
```

### Get User by ID
- **URL**: `/api/v1/users/{id}`
- **Method**: `GET`
- **Description**: Get user profile by ID
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "User retrieved successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "john@example.com",
        "status": "ACTIVE"
    }
}
```

### Get All Users
- **URL**: `/api/v1/users`
- **Method**: `GET`
- **Description**: Get list of all users (requires admin role)
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Users retrieved successfully",
    "data": [
        {
            "id": "123e4567-e89b-12d3-a456-426614174000",
            "username": "johndoe",
            "email": "john@example.com",
            "status": "ACTIVE"
        },
        {
            "id": "987fcdeb-a123-45d6-7890-123456789012",
            "username": "janedoe",
            "email": "jane@example.com",
            "status": "ACTIVE"
        }
    ]
}
```

### Activate User
- **URL**: `/api/v1/users/{id}/activate`
- **Method**: `POST`
- **Description**: Activate a user account (requires admin role)
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "User activated successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "john@example.com",
        "status": "ACTIVE"
    }
}
```

### Deactivate User
- **URL**: `/api/v1/users/{id}/deactivate`
- **Method**: `POST`
- **Description**: Deactivate a user account (requires admin role)
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "User deactivated successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "john@example.com",
        "status": "INACTIVE"
    }
}
```

### Update User Email
- **URL**: `/api/v1/users/{id}/email`
- **Method**: `PUT`
- **Description**: Update user's email address
- **Headers**: `Authorization: Bearer <access_token>`
- **Request Body**:
```json
{
    "email": "newemail@example.com"
}
```
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Email updated successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "newemail@example.com",
        "status": "ACTIVE"
    }
}
```

### Change Password
- **URL**: `/api/v1/users/{id}/password`
- **Method**: `PUT`
- **Description**: Change user's password
- **Headers**: `Authorization: Bearer <access_token>`
- **Request Body**:
```json
{
    "currentPassword": "oldPassword123",
    "newPassword": "newPassword123"
}
```
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Password changed successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "email": "john@example.com",
        "status": "ACTIVE"
    }
}
```

## Profile Endpoints

### Get Current User's Profile
- **URL**: `/api/v1/profiles/me`
- **Method**: `GET`
- **Description**: Get the current user's detailed profile information
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Profile retrieved successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "displayName": "John Doe",
        "biography": "Software developer and photographer",
        "imageUrl": "https://example.com/profiles/johndoe.jpg"
    }
}
```

### Update Current User's Profile
- **URL**: `/api/v1/profiles/me`
- **Method**: `PUT`
- **Description**: Update the current user's profile information
- **Headers**: `Authorization: Bearer <access_token>`
- **Request Body**:
```json
{
    "displayName": "John Doe",
    "biography": "Software developer and photographer"
}
```
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Profile updated successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "displayName": "John Doe",
        "biography": "Software developer and photographer",
        "imageUrl": "https://example.com/profiles/johndoe.jpg"
    }
}
```

### Upload Profile Image
- **URL**: `/api/v1/profiles/me/image`
- **Method**: `POST`
- **Description**: Upload a new profile image for the current user
- **Headers**: 
  - `Authorization: Bearer <access_token>`
  - `Content-Type: multipart/form-data`
- **Request Body**:
  - `file`: Image file (JPEG, PNG, etc.)
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Profile image uploaded successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "displayName": "John Doe",
        "biography": "Software developer and photographer",
        "imageUrl": "https://example.com/profiles/johndoe.jpg"
    }
}
```

### Remove Profile Image
- **URL**: `/api/v1/profiles/me/image`
- **Method**: `DELETE`
- **Description**: Remove the current user's profile image
- **Headers**: `Authorization: Bearer <access_token>`
- **Response** (200 OK):
```json
{
    "success": true,
    "message": "Profile image removed successfully",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "johndoe",
        "displayName": "John Doe",
        "biography": "Software developer and photographer",
        "imageUrl": null
    }
}
```

## Error Responses
All endpoints may return the following error responses:

### 400 Bad Request
```json
{
    "success": false,
    "message": "Validation error",
    "errors": [
        {
            "field": "email",
            "message": "Invalid email format"
        }
    ]
}
```

### 401 Unauthorized
```json
{
    "success": false,
    "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
    "success": false,
    "message": "Insufficient permissions"
}
```

### 404 Not Found
```json
{
    "success": false,
    "message": "User not found"
}
```

### 500 Internal Server Error
```json
{
    "success": false,
    "message": "An unexpected error occurred"
}
``` 