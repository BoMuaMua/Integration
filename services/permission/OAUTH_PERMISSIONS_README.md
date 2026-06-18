# OAuth2 客户端管理权限配置说明

## 概述
为操作 `oauth2_registered_client` 表添加了7个权限，用于管理 OAuth2 子系统客户端。

## 权限列表

| 序号 | 权限名称 | 权限标识 | 接口路径 | HTTP方法 |
|-----|---------|---------|---------|---------|
| 30 | 查询所有子系统 | `oauth:selectList` | `/oauth/selectList` | GET |
| 31 | 添加子系统 | `oauth:add` | `/oauth/add` | POST |
| 32 | 删除子系统 | `oauth:delete` | `/oauth/delete` | DELETE |
| 33 | 修改子系统 | `oauth:update` | `/oauth/update` | PUT |
| 34 | 根据id查询子系统 | `oauth:selectById` | `/oauth/selectById` | GET |
| 35 | 禁用子系统 | `oauth:disable` | `/oauth/disable` | PUT |
| 36 | 启用子系统 | `oauth:enable` | `/oauth/enable` | PUT |

## 部署步骤

### 1. 执行 SQL 脚本
在数据库中执行 `add_oauth_permissions.sql` 脚本，添加权限记录：

```bash
mysql -h 10.1.8.212 -u root -p Permissions < add_oauth_permissions.sql
```

或者直接复制 SQL 内容在数据库管理工具中执行。

### 2. 重启服务
重启以下服务使配置生效：
- permission 服务
- gateway 服务

### 3. 配置权限组
在权限管理系统中，将这些权限分配到对应的权限组（Groups），然后将权限组分配给角色（Roles）。

## 接口使用说明

### 查询所有子系统
```
GET /permission/oauth/selectList
Authorization: Bearer {token}
```

### 添加子系统
```
POST /permission/oauth/add
Authorization: Bearer {token}
Content-Type: application/json

{
  "clientId": "client-001",
  "clientName": "测试客户端",
  "clientSecret": "secret123",
  "redirectUris": "http://localhost:8080/callback",
  "requireAuthorizationConsent": "true",
  "scope": "read,write",
  "status": 1,
  "url": "http://localhost:8080"
}
```

### 删除子系统
```
DELETE /permission/oauth/delete?id=1
Authorization: Bearer {token}
```

### 修改子系统
```
PUT /permission/oauth/update
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 1,
  "clientId": "client-001",
  "clientName": "更新后的客户端名称",
  ...
}
```

### 根据id查询子系统
```
GET /permission/oauth/selectById?id=1
Authorization: Bearer {token}
```

### 禁用子系统
```
PUT /permission/oauth/disable?id=1
Authorization: Bearer {token}
```

### 启用子系统
```
PUT /permission/oauth/enable?id=1
Authorization: Bearer {token}
```

## 文件清单

### 新增文件
1. `add_oauth_permissions.sql` - 权限数据 SQL 脚本
2. `src/main/java/com/example/permission/controller/Oauth2Controller.java` - OAuth2 客户端管理控制器
3. `src/main/java/com/example/permission/service/impl/Oauth2ServiceImpl.java` - OAuth2 客户端管理服务实现

### 修改文件
无（仅新增文件）

## 技术说明

### 缓存策略
- 查询列表时使用 Redis 缓存，缓存时间 5 分钟
- 增删改操作时清除缓存，保证数据一致性

### 安全机制
- 所有接口都需要携带有效的 JWT Token
- Gateway 会验证 Token 并传递用户信息
- Permission 服务会根据用户权限进行访问控制

### 数据校验
- 添加客户端时会检查 `clientId` 是否已存在
- 状态字段：0-禁用，1-启用

## 注意事项

1. 确保在执行 SQL 脚本前，数据库连接正常
2. 权限添加后，需要在权限管理系统中分配给相应的角色
3. 客户端的 `clientId` 必须唯一
4. 禁用/启用操作会清除缓存，确保其他服务能获取最新状态
5. 所有操作都会记录日志，便于审计和排查问题
