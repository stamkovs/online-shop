-------------------------------------------------------------------------------------------------
-- insert roles and permissions that different users can have.
-------------------------------------------------------------------------------------------------

insert into user_role (name, permission) values
('Admin', 'Manage'),
('Manager', 'Edit'),
('Customer', 'View');