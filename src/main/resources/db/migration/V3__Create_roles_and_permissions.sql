-- Create roles table
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Create permissions table
CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Create role_permissions join table
CREATE TABLE role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

-- Create user_roles join table
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_permissions_name ON permissions(name);

-- Insert default roles
INSERT INTO roles (id, created_at, updated_at, name, description)
VALUES 
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN', 'Administrator with full access'),
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 'Regular user with standard permissions');

-- Insert basic permissions
INSERT INTO permissions (id, created_at, updated_at, name, description)
VALUES
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER_READ', 'Can view user details'),
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER_CREATE', 'Can create users'),
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER_UPDATE', 'Can update users'),
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER_DELETE', 'Can delete users'),
    (gen_random_uuid(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER_ADMIN', 'Can perform administrative actions on users');

-- Add comments
COMMENT ON TABLE roles IS 'Defines user roles in the system';
COMMENT ON TABLE permissions IS 'Defines granular permissions in the system';
COMMENT ON TABLE role_permissions IS 'Maps roles to their granted permissions';
COMMENT ON TABLE user_roles IS 'Maps users to their assigned roles'; 