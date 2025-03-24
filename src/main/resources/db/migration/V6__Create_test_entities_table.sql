-- Create test_entities table for use in repository integration tests
CREATE TABLE test_entities (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
); 