-- Create users table
CREATE TABLE users (
    -- Base entity fields
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    
    -- User-specific fields
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Indexes for fast lookups
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);

-- Add comments for documentation
COMMENT ON TABLE users IS 'Stores user account information and credentials';
COMMENT ON COLUMN users.id IS 'Unique identifier for the user';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the user was last updated';
COMMENT ON COLUMN users.username IS 'Unique username for login and identification';
COMMENT ON COLUMN users.email IS 'User email address, must be unique';
COMMENT ON COLUMN users.password_hash IS 'BCrypt hash of the user password (includes salt)';
COMMENT ON COLUMN users.status IS 'Current status of the user (ACTIVE, INACTIVE, PENDING)'; 