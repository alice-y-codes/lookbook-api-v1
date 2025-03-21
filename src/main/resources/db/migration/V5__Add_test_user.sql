-- Create a test admin user 
-- Username: admin@example.com / Password: Password1!
-- BCrypt hash for 'Password1!' with salt
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
    gen_random_uuid(),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',  -- Bcrypt hash for 'Password1!'
    '$2a$10$hKDVYxLefVHV/vtuPhWD3O',  -- Salt portion of the hash
    'ACTIVE'
);

-- Assign the ADMIN role to the test admin user
DO $$
DECLARE
    admin_user_id UUID;
    admin_role_id UUID;
BEGIN
    -- Get the admin user ID
    SELECT id INTO admin_user_id FROM users WHERE username = 'admin';
    
    -- Get the admin role ID
    SELECT id INTO admin_role_id FROM roles WHERE name = 'ADMIN';
    
    -- Assign the admin role to the admin user
    INSERT INTO user_roles (user_id, role_id)
    VALUES (admin_user_id, admin_role_id);
END $$;

-- Create a test regular user
-- Username: user@example.com / Password: Password1!
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
    gen_random_uuid(),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'user',
    'user@example.com',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',  -- Bcrypt hash for 'Password1!'
    '$2a$10$hKDVYxLefVHV/vtuPhWD3O',  -- Salt portion of the hash
    'ACTIVE'
);

-- Assign the USER role to the test user
DO $$
DECLARE
    test_user_id UUID;
    user_role_id UUID;
BEGIN
    -- Get the test user ID
    SELECT id INTO test_user_id FROM users WHERE username = 'user';
    
    -- Get the user role ID
    SELECT id INTO user_role_id FROM roles WHERE name = 'USER';
    
    -- Assign the user role to the test user
    INSERT INTO user_roles (user_id, role_id)
    VALUES (test_user_id, user_role_id);
END $$; 