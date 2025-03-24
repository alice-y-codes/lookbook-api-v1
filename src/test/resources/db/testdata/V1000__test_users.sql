-- Test user data
-- Admin user
INSERT INTO users (
    id, 
    created_at, 
    updated_at, 
    username, 
    email, 
    password_hash, 
    password_salt, 
    status
)
VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',  -- Fixed UUID for testing
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',  -- Hash for 'Password123!'
    '$2a$10$hKDVYxLefVHV/vtuPhWD3O',  -- Salt
    'ACTIVE'
);

-- Assign ADMIN role to admin user
DO $$
DECLARE
    admin_role_id UUID;
BEGIN
    SELECT id INTO admin_role_id FROM roles WHERE name = 'ADMIN';
    INSERT INTO user_roles (user_id, role_id)
    VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', admin_role_id);
END $$;

-- Regular test user
INSERT INTO users (
    id, 
    created_at, 
    updated_at, 
    username, 
    email, 
    password_hash, 
    password_salt, 
    status
)
VALUES (
    'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12',  -- Another fixed UUID for testing
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'testuser',
    'test@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',  -- Hash for 'Password123!'
    '$2a$10$hKDVYxLefVHV/vtuPhWD3O',  -- Salt
    'ACTIVE'
);

-- Assign USER role to test user
DO $$
DECLARE
    user_role_id UUID;
BEGIN
    SELECT id INTO user_role_id FROM roles WHERE name = 'USER';
    INSERT INTO user_roles (user_id, role_id)
    VALUES ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', user_role_id);
END $$;

-- Pending test user
INSERT INTO users (
    id, 
    created_at, 
    updated_at, 
    username, 
    email, 
    password_hash, 
    password_salt, 
    status
)
VALUES (
    'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13',  -- Another fixed UUID for testing
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'testuser2',
    'test2@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',  -- Hash for 'Password123!'
    '$2a$10$hKDVYxLefVHV/vtuPhWD3O',  -- Salt
    'PENDING'
); 