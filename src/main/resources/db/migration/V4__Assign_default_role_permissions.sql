-- Get role IDs and assign permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
AND p.name IN ('USER_READ', 'USER_CREATE', 'USER_UPDATE', 'USER_DELETE', 'USER_ADMIN');

-- Assign USER_READ permission to USER role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'USER'
AND p.name = 'USER_READ'; 