-- Additional test users for development and testing
-- This is a repeatable migration (R__) that will run after all versioned migrations

-- Create test users (if they don't exist)
-- All passwords are 'Password1!'
DO $$
BEGIN
    -- Test User 1
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'tester1') THEN
        INSERT INTO users (
            id, created_at, updated_at, username, email, password_hash, password_salt, status
        ) VALUES (
            gen_random_uuid(),
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            'tester1',
            'tester1@example.com',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3O',
            'ACTIVE'
        );
    END IF;
    
    -- Test User 2
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'tester2') THEN
        INSERT INTO users (
            id, created_at, updated_at, username, email, password_hash, password_salt, status
        ) VALUES (
            gen_random_uuid(),
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            'tester2',
            'tester2@example.com',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3O',
            'ACTIVE'
        );
    END IF;
    
    -- Test User 3 (inactive)
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'inactive') THEN
        INSERT INTO users (
            id, created_at, updated_at, username, email, password_hash, password_salt, status
        ) VALUES (
            gen_random_uuid(),
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            'inactive',
            'inactive@example.com',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3O',
            'INACTIVE'
        );
    END IF;
    
    -- Test User 4 (pending)
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'pending') THEN
        INSERT INTO users (
            id, created_at, updated_at, username, email, password_hash, password_salt, status
        ) VALUES (
            gen_random_uuid(),
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            'pending',
            'pending@example.com',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',
            '$2a$10$hKDVYxLefVHV/vtuPhWD3O',
            'PENDING'
        );
    END IF;
END $$;

-- Assign roles to test users
DO $$
DECLARE
    user_role_id UUID;
    admin_role_id UUID;
    tester1_id UUID;
    tester2_id UUID;
    inactive_id UUID;
    pending_id UUID;
BEGIN
    -- Get role IDs
    SELECT id INTO user_role_id FROM roles WHERE name = 'USER';
    SELECT id INTO admin_role_id FROM roles WHERE name = 'ADMIN';
    
    -- Get user IDs
    SELECT id INTO tester1_id FROM users WHERE username = 'tester1';
    SELECT id INTO tester2_id FROM users WHERE username = 'tester2';
    SELECT id INTO inactive_id FROM users WHERE username = 'inactive';
    SELECT id INTO pending_id FROM users WHERE username = 'pending';
    
    -- Assign roles
    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = tester1_id AND role_id = user_role_id) THEN
        INSERT INTO user_roles (user_id, role_id) VALUES (tester1_id, user_role_id);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = tester2_id AND role_id = user_role_id) THEN
        INSERT INTO user_roles (user_id, role_id) VALUES (tester2_id, user_role_id);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = inactive_id AND role_id = user_role_id) THEN
        INSERT INTO user_roles (user_id, role_id) VALUES (inactive_id, user_role_id);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = pending_id AND role_id = user_role_id) THEN
        INSERT INTO user_roles (user_id, role_id) VALUES (pending_id, user_role_id);
    END IF;
END $$; 