-- Create refresh_tokens table
CREATE TABLE refresh_tokens (
    -- Base entity fields
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    
    -- Token-specific fields
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Foreign key constraint
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Indexes for fast lookups
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);

-- Add comments for documentation
COMMENT ON TABLE refresh_tokens IS 'Stores JWT refresh tokens for users';
COMMENT ON COLUMN refresh_tokens.id IS 'Unique identifier for the refresh token';
COMMENT ON COLUMN refresh_tokens.created_at IS 'Timestamp when the token was created';
COMMENT ON COLUMN refresh_tokens.updated_at IS 'Timestamp when the token was last updated';
COMMENT ON COLUMN refresh_tokens.token IS 'The actual refresh token value';
COMMENT ON COLUMN refresh_tokens.user_id IS 'ID of the user that owns this token';
COMMENT ON COLUMN refresh_tokens.expires_at IS 'Timestamp when this token expires';
COMMENT ON COLUMN refresh_tokens.revoked IS 'Whether this token has been revoked'; 