-- Get role IDs
DO $$
DECLARE
    admin_role_id UUID;
    user_role_id UUID;
    user_read_permission_id UUID;
    user_create_permission_id UUID;
    user_update_permission_id UUID;
    user_delete_permission_id UUID;
    user_admin_permission_id UUID;
BEGIN
    -- Get role IDs
    SELECT id INTO admin_role_id FROM roles WHERE name = 'ADMIN';
    SELECT id INTO user_role_id FROM roles WHERE name = 'USER';
    
    -- Get permission IDs
    SELECT id INTO user_read_permission_id FROM permissions WHERE name = 'USER_READ';
    SELECT id INTO user_create_permission_id FROM permissions WHERE name = 'USER_CREATE';
    SELECT id INTO user_update_permission_id FROM permissions WHERE name = 'USER_UPDATE';
    SELECT id INTO user_delete_permission_id FROM permissions WHERE name = 'USER_DELETE';
    SELECT id INTO user_admin_permission_id FROM permissions WHERE name = 'USER_ADMIN';
    
    -- Assign all permissions to ADMIN role
    INSERT INTO role_permissions (role_id, permission_id)
    VALUES
        (admin_role_id, user_read_permission_id),
        (admin_role_id, user_create_permission_id),
        (admin_role_id, user_update_permission_id),
        (admin_role_id, user_delete_permission_id),
        (admin_role_id, user_admin_permission_id);
        
    -- Assign limited permissions to USER role
    INSERT INTO role_permissions (role_id, permission_id)
    VALUES
        (user_role_id, user_read_permission_id);
END $$; 