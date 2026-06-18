-- 添加 OAuth2 客户端管理相关权限
-- 这些权限用于操作 oauth2_registered_client 表

-- 30. 查询所有子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('查询所有子系统', 'oauth:selectList', 1);

-- 31. 添加子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('添加子系统', 'oauth:add', 1);

-- 32. 删除子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('删除子系统', 'oauth:delete', 1);

-- 33. 修改子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('修改子系统', 'oauth:update', 1);

-- 34. 根据id查询子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('根据id查询子系统', 'oauth:selectById', 1);

-- 35. 禁用子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('禁用子系统', 'oauth:disable', 1);

-- 36. 启用子系统
INSERT INTO Permissions (name, perms, status) 
VALUES ('启用子系统', 'oauth:enable', 1);
