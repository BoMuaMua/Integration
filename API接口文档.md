# Integration 项目接口文档

## 文档说明

本文档包含 Integration 项目所有微服务模块的接口信息。

**基础路径**: `https://nc-wxwjc.gcu.edu.cn/api`

**服务模块说明**:
- permission: 权限管理服务 (端口: 6602)
- sign: 签到服务 (端口: 6603)
- recruitment: 招新服务 (端口: 6604)
- check: 考核服务 (端口: 6605)

**通用响应格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

---

## 一、权限管理服务 (permission)

### 1.1 用户接口 (/user)

#### 1.1.1 获取个人信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/user/information`
- **接口备注**: 获取当前登录用户的个人信息
- **接口功能**: 返回当前登录用户的详细信息
- **请求方法**: GET
- **接口传参**: 
  - Header: `X-USER-ID` (用户ID，由网关自动添加)
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "userId": "用户ID",
    "userName": "用户名",
    "department": "部门信息",
    "role": "角色信息"
  }
}
```

#### 1.1.2 更新个人信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/user/update`
- **接口备注**: 更新当前登录用户的个人信息
- **接口功能**: 允许用户更新自己的个人信息
- **请求方法**: PUT
- **接口传参**: 
  - Header: `X-USER-ID` (用户ID)
  - Body (JSON):
    - 根据 `PersonUpdateUserDTO` 定义的字段
- **返回体格式**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 1.1.3 获取所有个人信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/user/allInformation`
- **接口备注**: 获取指定部门的所有用户信息
- **接口功能**: 管理员查看某部门下所有用户的详细信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (Integer, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "userId": "用户ID",
      "userName": "用户名",
      "departmentId": 部门ID
    }
  ]
}
```

### 1.2 管理员接口 (/admin)

#### 1.2.1 查询用户信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/admin/informationUser`
- **接口备注**: 管理员查询用户信息
- **接口功能**: 管理员查看系统用户信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.2.2 更新用户信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/admin/updateUser`
- **接口备注**: 管理员更新用户信息
- **接口功能**: 管理员修改用户信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `AdminUpdateUserDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 1.2.3 删除用户
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/admin/deleteUser`
- **接口备注**: 管理员删除用户
- **接口功能**: 管理员删除系统用户
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `userId` (String, 必填) - 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 1.3 部门接口 (/departments)

#### 1.3.1 获取部门列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/departments/get`
- **接口备注**: 获取所有部门列表
- **接口功能**: 返回系统中所有部门信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "departmentId": 1,
      "departmentName": "部门名称",
      "description": "部门描述"
    }
  ]
}
```

#### 1.3.2 添加部门
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/departments/add`
- **接口备注**: 添加新部门
- **接口功能**: 创建新的部门
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `DepartmentsDTO` 对象
    - `departmentName`: 部门名称
    - `description`: 部门描述
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.3.3 删除部门
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/departments/delete`
- **接口备注**: 删除部门
- **接口功能**: 根据部门ID删除部门
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `departmentId` (Integer, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.3.4 修改部门
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/departments/update`
- **接口备注**: 修改部门信息
- **接口功能**: 更新部门信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `Departments` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 1.3.5 获取部门详情
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/departments/getDetail`
- **接口备注**: 获取部门详细信息
- **接口功能**: 根据部门ID获取部门详细信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (Integer, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "departmentId": 1,
    "departmentName": "部门名称",
    "description": "部门描述"
  }
}
```

### 1.4 角色接口 (/roles)

#### 1.4.1 获取角色列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/roles/get`
- **接口备注**: 获取所有角色列表
- **接口功能**: 返回系统中所有角色信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "roleId": 1,
      "roleName": "角色名称",
      "description": "角色描述"
    }
  ]
}
```

#### 1.4.2 添加角色
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/roles/add`
- **接口备注**: 添加新角色
- **接口功能**: 创建新的角色
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `RolesDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.4.3 删除角色
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/roles/delete`
- **接口备注**: 删除角色
- **接口功能**: 根据角色ID删除角色
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `roleId` (Integer, 必填) - 角色ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.4.4 修改角色
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/roles/update`
- **接口备注**: 修改角色信息
- **接口功能**: 更新角色信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `Roles` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 1.4.5 获取权限组
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/roles/getPermissionGroups`
- **接口备注**: 获取角色的权限组
- **接口功能**: 根据角色ID获取该角色关联的权限组
- **请求方法**: GET
- **接口传参**: 
  - Query: `roleId` (Integer, 必填) - 角色ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.5 权限接口 (/permissions)

#### 1.5.1 获取权限列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissions/get`
- **接口备注**: 获取所有权限列表
- **接口功能**: 返回系统中所有权限信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "permissionId": "权限ID",
      "permissionName": "权限名称",
      "path": "路径",
      "description": "权限描述"
    }
  ]
}
```

#### 1.5.2 添加权限
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissions/add`
- **接口备注**: 添加新权限
- **接口功能**: 创建新的权限
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `PermissionsDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.5.3 删除权限
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissions/delete`
- **接口备注**: 删除权限
- **接口功能**: 根据权限ID删除权限
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `permissionId` (String, 必填) - 权限ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.5.4 修改权限
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissions/update`
- **接口备注**: 修改权限信息
- **接口功能**: 更新权限信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `Permissions` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 1.5.5 根据权限组id获取权限列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissions/getByGroupId`
- **接口备注**: 获取指定权限组的权限列表
- **接口功能**: 根据权限组ID获取该组下的所有权限
- **请求方法**: GET
- **接口传参**: 
  - Query: `groupId` (String, 必填) - 权限组ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.6 权限组接口 (/permissionGroups)

#### 1.6.1 获取权限组列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissionGroups/get`
- **接口备注**: 获取所有权限组列表
- **接口功能**: 返回系统中所有权限组信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "permissionGroupsId": 1,
      "permissionGroupsName": "权限组名称",
      "description": "权限组描述"
    }
  ]
}
```

#### 1.6.2 添加权限组
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissionGroups/add`
- **接口备注**: 添加新权限组
- **接口功能**: 创建新的权限组
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `PermissionGroupsDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.6.3 删除权限组
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissionGroups/delete`
- **接口备注**: 删除权限组
- **接口功能**: 根据权限组ID删除权限组
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `permissionGroupsId` (Integer, 必填) - 权限组ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.6.4 修改权限组
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissionGroups/update`
- **接口备注**: 修改权限组信息
- **接口功能**: 更新权限组信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `PermissionGroupsDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 1.6.5 根据角色id获取权限组
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/permissionGroups/getByRoleId`
- **接口备注**: 获取角色的权限组
- **接口功能**: 根据角色ID获取该角色关联的权限组
- **请求方法**: GET
- **接口传参**: 
  - Query: `roleId` (Integer, 必填) - 角色ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.7 路由接口 (/routes)

#### 1.7.1 获取路由列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/get`
- **接口备注**: 获取所有路由列表
- **接口功能**: 返回系统中所有路由信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "routeId": 1,
      "routeName": "路由名称",
      "path": "路由路径",
      "component": "组件"
    }
  ]
}
```

#### 1.7.2 添加路由
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/add`
- **接口备注**: 添加新路由
- **接口功能**: 创建新的路由
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `RoutesDTO` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.7.3 删除路由
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/delete`
- **接口备注**: 删除路由
- **接口功能**: 根据路由ID删除路由
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `route_id` (Long, 必填) - 路由ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.7.4 修改路由
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/update`
- **接口备注**: 修改路由信息
- **接口功能**: 更新路由信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `Routes` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 1.7.5 获取路由菜单
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/getMenus`
- **接口备注**: 获取路由菜单
- **接口功能**: 根据父ID和客户端ID获取路由菜单
- **请求方法**: GET
- **接口传参**: 
  - Query: `parentId` (Long, 可选) - 父级路由ID
  - Query: `clientId` (Integer, 必填) - 客户端ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.7.6 获取个人某子系统路由菜单
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/routes/getPersonalMenus`
- **接口备注**: 获取个人的子系统路由菜单
- **接口功能**: 根据用户ID和客户端ID获取该用户的路由菜单
- **请求方法**: GET
- **接口传参**: 
  - Header: `X-USER-ID` (用户ID)
  - Query: `clientId` (Integer, 必填) - 客户端ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.8 活动接口 (/activity)

#### 1.8.1 获取活动列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/get`
- **接口备注**: 获取所有活动列表
- **接口功能**: 返回系统中所有活动信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.8.2 添加活动
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/add`
- **接口备注**: 添加新活动
- **接口功能**: 创建新的活动
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Participants` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.8.3 删除活动
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/delete`
- **接口备注**: 删除活动
- **接口功能**: 根据活动ID删除活动
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `activityId` (Integer, 必填) - 活动ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.8.4 更新活动
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/update`
- **接口备注**: 更新活动信息
- **接口功能**: 修改活动信息
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): `Participants` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 1.8.5 添加人员
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/addPeople`
- **接口备注**: 向活动添加人员
- **接口功能**: 为活动添加参与人员
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `AddPeople` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.8.6 删除人员
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/deletePeople`
- **接口备注**: 从活动中删除人员
- **接口功能**: 从活动中移除参与人员
- **请求方法**: DELETE
- **接口传参**: 
  - Body (JSON): `AddPeople` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.8.7 获取活动人员列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/activity/getPeople`
- **接口备注**: 获取活动参与人员列表
- **接口功能**: 根据活动ID获取参与人员列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityId` (Integer, 必填) - 活动ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.9 指标接口 (/indicators)

#### 1.9.1 获取指标列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/get`
- **接口备注**: 获取所有指标列表
- **接口功能**: 返回系统中所有指标信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.9.2 添加指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/add`
- **接口备注**: 添加新指标
- **接口功能**: 创建新的指标
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `IndicatorsMessage` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

#### 1.9.3 更新指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/update`
- **接口备注**: 更新指标信息
- **接口功能**: 修改指标信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `IndicatorsMessage` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 1.9.4 删除指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/delete`
- **接口备注**: 删除指标
- **接口功能**: 删除指标
- **请求方法**: DELETE
- **接口传参**: 
  - Body (JSON): `deleteIndicators` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 1.9.5 根据活动ID获取指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/getIndicatorsByActivityId`
- **接口备注**: 获取活动的指标
- **接口功能**: 根据活动ID获取该活动的指标
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityId` (Integer, 必填) - 活动ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.9.6 获取用户指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/getUserIndicators`
- **接口备注**: 获取用户指标
- **接口功能**: 获取用户的指标信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `year` (Integer, 可选) - 学年
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 1.9.7 根据活动ID获取用户指标
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/indicators/getUserIndicatorsByActivityId`
- **接口备注**: 获取用户在活动中的指标
- **接口功能**: 根据活动ID获取用户在该活动的指标
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityId` (Integer, 必填) - 活动ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

### 1.10 学年接口 (/year)

#### 1.10.1 获取学年列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/year/get`
- **接口备注**: 获取所有学年列表
- **接口功能**: 返回系统中所有学年信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    "2023学年",
    "2024学年",
    "2025学年"
  ]
}
```

#### 1.10.2 添加学年
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/year/add`
- **接口备注**: 添加新学年
- **接口功能**: 添加当前学年到系统
- **请求方法**: POST
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": null
}
```

### 1.11 安全接口 (/logout)

#### 1.11.1 注销
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/permission/logout`
- **接口备注**: 用户注销登录
- **接口功能**: 清除用户登录状态和Redis缓存
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "注销成功!",
  "data": null
}
```

---

## 二、签到服务 (sign)

### 2.1 签到接口 (/signin)

#### 2.1.1 创建签到
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/createSignin`
- **接口备注**: 创建新的签到活动
- **接口功能**: 管理员创建新的签到活动
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `SigninMessage` 对象
    - `activityName`: 活动名称
    - `startTime`: 开始时间
    - `endTime`: 结束时间
    - 其他签到活动配置
- **返回体格式**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": null
}
```

#### 2.1.2 获取所有签到信息列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/signinList`
- **接口备注**: 获取所有签到活动列表
- **接口功能**: 返回所有签到活动信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 2.1.3 获取签到用户列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/signinUserList`
- **接口备注**: 获取签到活动的用户列表
- **接口功能**: 获取某个签到活动的用户签到列表
- **请求方法**: GET
- **接口传参**: 
  - Header: `X-USER-ID` (用户ID)
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 2.1.4 停止签到
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/stopSignin`
- **接口备注**: 停止签到活动
- **接口功能**: 停止某个活动的签到
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `ActivityName` 对象
    - `activityName`: 活动名称
- **返回体格式**:
```json
{
  "code": 200,
  "message": "停止成功",
  "data": null
}
```

#### 2.1.5 获取签到详细信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/signinDetail`
- **接口备注**: 获取签到活动详细信息
- **接口功能**: 根据活动名称获取签到详细信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityName` (String, 必填) - 活动名称
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {}
}
```

#### 2.1.6 用户签到
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/signin`
- **接口备注**: 用户进行签到
- **接口功能**: 用户提交签到信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `UserSignIn` 对象
  - Header: `X-USER-ID` (用户ID)
- **返回体格式**:
```json
{
  "code": 200,
  "message": "签到成功",
  "data": null
}
```

#### 2.1.7 更改用户签到状态(改为已签到)
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/changeUserSignInStatus1`
- **接口备注**: 更改用户签到状态为已签到
- **接口功能**: 管理员修改用户的签到状态
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `SignInChange` 对象
    - `activityName`: 活动名称
    - `userId`: 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 2.1.8 更改用户签到状态(改为未签到)
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/changeUserSignInStatus0`
- **接口备注**: 更改用户签到状态为未签到
- **接口功能**: 管理员修改用户的签到状态
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `SignInChange` 对象
    - `activityName`: 活动名称
    - `userId`: 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

#### 2.1.9 删除签到活动
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/delAct`
- **接口备注**: 删除签到活动
- **接口功能**: 删除某个签到活动
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Activity` 对象
    - `activityName`: 活动名称
- **返回体格式**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 2.1.10 更新签到活动
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/uptAct`
- **接口备注**: 更新签到活动
- **接口功能**: 更新签到活动信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `upActivity` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 2.1.11 获取签到二维码字符串
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/getStringForQRCode`
- **接口备注**: 获取签到二维码内容
- **接口功能**: 生成签到活动二维码字符串
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityName` (String, 必填) - 活动名称
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": "二维码字符串"
}
```

#### 2.1.12 获取签到状态
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/getSignInStatus`
- **接口备注**: 获取用户签到状态
- **接口功能**: 查询用户在某个活动的签到状态
- **请求方法**: GET
- **接口传参**: 
  - Query: `activityName` (String, 必填) - 活动名称
  - Header: `X-USER-ID` (用户ID)
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "status": "签到状态"
  }
}
```

#### 2.1.13 签到统计
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/signin/statistics`
- **接口备注**: 签到数据统计
- **接口功能**: 导出签到统计数据
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**: Excel文件下载

### 2.2 部门接口 (/departments)

#### 2.2.1 获取部门列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/departments/get`
- **接口备注**: 获取所有部门列表
- **接口功能**: 返回系统中所有部门信息
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

#### 2.2.2 获取部门详情
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/departments/getDetail`
- **接口备注**: 获取部门详细信息
- **接口功能**: 根据部门ID获取部门详细信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (Integer, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {}
}
```

### 2.3 路由接口 (/routes)

#### 2.3.1 获取菜单
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/sign/routes/getMenus`
- **接口备注**: 获取用户菜单
- **接口功能**: 获取当前用户的路由菜单
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": []
}
```

---

## 三、招新服务 (recruitment)

### 3.1 用户接口 (/user)

#### 3.1.1 获取所有用户信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/info/all`
- **接口备注**: 获取所有报名用户信息（分页）
- **接口功能**: 分页查询所有报名用户信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `page` (Integer, 默认1) - 页码
  - Query: `pageSize` (Integer, 默认10) - 每页数量
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "list": []
  }
}
```

#### 3.1.2 上传照片
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/putPhoto`
- **接口备注**: 上传用户照片
- **接口功能**: 用户上传个人照片到OSS
- **请求方法**: POST
- **接口传参**: 
  - Form-Data: 
    - `file`: 图片文件 (jpg, jpeg, png, gif, bmp)
    - `id`: 用户ID (12位)
- **返回体格式**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": null
}
```

#### 3.1.3 获取部门统计数据
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/departmentData`
- **接口备注**: 获取部门报名统计
- **接口功能**: 获取各部门报名人数统计
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.1.4 按部门获取用户信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/info/department`
- **接口备注**: 获取指定部门的报名用户
- **接口功能**: 根据部门ID获取该部门的报名用户信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (String, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.1.5 按个人搜索
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/info/person`
- **接口备注**: 搜索个人报名信息
- **接口功能**: 根据关键词搜索用户报名信息
- **请求方法**: GET
- **接口传参**: 
  - Query: `key` (String, 必填) - 搜索关键词
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.1.6 用户注册
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/register`
- **接口备注**: 用户报名注册
- **接口功能**: 新用户提交报名信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): 包含用户报名信息的JSON对象
    - `id`: 用户ID
    - 其他报名信息字段
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 3.1.7 更新图片信息
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/updateImage`
- **接口备注**: 更新用户图片信息
- **接口功能**: 更新用户的图片URL
- **请求方法**: PUT
- **接口传参**: 
  - Body (JSON): 包含id和url的JSON对象
    - `id`: 用户ID
    - `url`: 图片URL
- **返回体格式**:
```json
{
  "code": 200,
  "message": "用户信息已更新",
  "data": {}
}
```

#### 3.1.8 根据IP获取用户
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/ip`
- **接口备注**: 根据IP查询用户
- **接口功能**: 根据IP地址获取对应的用户信息
- **请求方法**: GET
- **接口传参**: 
  - Header: `x-forwarded-for` - 用户IP
- **返回体格式**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {}
}
```

#### 3.1.9 删除用户
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/delete`
- **接口备注**: 删除报名用户
- **接口功能**: 删除用户及其相关数据
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `id` (String, 必填) - 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 3.1.10 下载Excel
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/download/excel`
- **接口备注**: 导出用户数据为Excel
- **接口功能**: 下载所有报名用户数据的Excel文件
- **请求方法**: GET
- **接口传参**: 无
- **返回体格式**: Excel文件下载

#### 3.1.11 获取已通过成员Excel
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/excel/throughMembers`
- **接口备注**: 导出已通过成员数据
- **接口功能**: 下载指定部门已通过成员的Excel文件
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (String, 必填) - 部门ID
- **返回体格式**: Excel文件下载

#### 3.1.12 记录评价
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/remember/comment`
- **接口备注**: 记录面试评价
- **接口功能**: 为面试者添加评价
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Comment` 对象
    - `id`: 用户ID
    - `comment`: 评价内容
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 3.1.13 获取评价
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/comment`
- **接口备注**: 获取用户评价
- **接口功能**: 根据用户ID获取面试评价
- **请求方法**: GET
- **接口传参**: 
  - Query: `id` (String, 必填) - 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

#### 3.1.14 上传Excel
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/upload`
- **接口备注**: 批量导入用户数据
- **接口功能**: 通过Excel文件批量导入用户信息
- **请求方法**: POST
- **接口传参**: 
  - Form-Data: `file` - Excel文件
- **返回体格式**: 返回 "success" 或错误信息

### 3.2 部门接口 (/department)

#### 3.2.1 获取通过人员
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/pass`
- **接口备注**: 获取部门已通过人员
- **接口功能**: 获取指定部门已通过的报名人员
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (String, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.2.2 获取未通过人员
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/noPass`
- **接口备注**: 获取部门未通过人员
- **接口功能**: 获取指定部门未通过的报名人员
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (String, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.2.3 获取待通过人员
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/willPass`
- **接口备注**: 获取部门待审核人员
- **接口功能**: 获取指定部门待审核的报名人员
- **请求方法**: GET
- **接口传参**: 
  - Query: `departmentId` (String, 必填) - 部门ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 3.2.4 进入下一部门
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/recruitment/user/nextDepartment`
- **接口备注**: 用户进入下一部门面试
- **接口功能**: 将用户流转至下一部门
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): 包含id的Map对象
    - `id`: 用户ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 四、考核服务 (check)

### 4.1 管理员接口 (/api/administrator)

#### 4.1.1 获取主任务列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/get/allMain`
- **接口备注**: 分页获取主任务列表
- **接口功能**: 管理员分页查询所有主任务
- **请求方法**: GET
- **接口传参**: 
  - Query: `page` (Integer, 必填) - 页码
  - Query: `pageSize` (Integer, 必填) - 每页数量
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "list": []
  }
}
```

#### 4.1.2 根据名称查询主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/get/mainTaskByName`
- **接口备注**: 按名称搜索主任务
- **接口功能**: 根据主任务名称查询主任务
- **请求方法**: GET
- **接口传参**: 
  - Query: `mainTaskName` (String, 必填) - 主任务名称
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.1.3 获取子任务列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/get/breakdown`
- **接口备注**: 获取主任务下的子任务
- **接口功能**: 根据主任务ID获取子任务列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `mainTaskId` (Integer, 必填) - 主任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.1.4 添加主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/add/mainTask`
- **接口备注**: 创建主任务
- **接口功能**: 管理员创建新的主任务
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `MainTask` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.5 修改主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/update/mainTask`
- **接口备注**: 更新主任务信息
- **接口功能**: 管理员修改主任务信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `MainTask` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.6 删除主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/delete/mainTask`
- **接口备注**: 删除主任务
- **接口功能**: 管理员删除主任务
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `id` (Integer, 必填) - 主任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.7 添加子任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/add/breakdown`
- **接口备注**: 创建子任务
- **接口功能**: 为主任务添加子任务
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `TaskBreakdown` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.8 修改子任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/update/breakdown`
- **接口备注**: 更新子任务信息
- **接口功能**: 管理员修改子任务信息
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `TaskBreakdown` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.9 删除子任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/delete/breakdown`
- **接口备注**: 删除子任务
- **接口功能**: 管理员删除子任务
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `id` (Integer, 必填) - 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.10 获取反馈列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/get/feedback`
- **接口备注**: 获取任务反馈列表
- **接口功能**: 根据子任务ID获取反馈列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `breakdownId` (Integer, 必填) - 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.1.11 管理员上传附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/add/Annexs`
- **接口备注**: 管理员上传任务附件
- **接口功能**: 为子任务上传附件
- **请求方法**: POST
- **接口传参**: 
  - Form-Data:
    - `files`: 附件文件数组
    - `breakdownId`: 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.1.12 管理员查看附件列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/get/AnnexList`
- **接口备注**: 查看子任务附件列表
- **接口功能**: 获取子任务的附件列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `breakdownId` (Integer, 必填) - 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.1.13 管理员下载附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/download/Annex`
- **接口备注**: 下载任务附件
- **接口功能**: 下载子任务的附件文件
- **请求方法**: GET
- **接口传参**: 
  - Query: `objectName` (String, 必填) - 附件对象名称
- **返回体格式**: 文件流下载

#### 4.1.14 管理员删除附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/administrator/delete/Annex`
- **接口备注**: 删除任务附件
- **接口功能**: 删除子任务的附件
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `annexId` (Integer, 必填) - 附件ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 4.2 干事接口 (/api/officer)

#### 4.2.1 干事上传附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/officer/add/Annexs`
- **接口备注**: 干事上传任务附件
- **接口功能**: 干事为子任务上传附件
- **请求方法**: POST
- **接口传参**: 
  - Form-Data:
    - `files`: 附件文件数组
    - `breakdownId`: 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.2.2 干事查看附件列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/officer/get/AnnexList`
- **接口备注**: 干事查看任务附件
- **接口功能**: 干事查看子任务的附件列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `breakdownId` (Integer, 必填) - 子任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.2.3 干事下载附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/officer/download/Annex`
- **接口备注**: 干事下载任务附件
- **接口功能**: 干事下载子任务的附件
- **请求方法**: GET
- **接口传参**: 
  - Query: `objectName` (String, 必填) - 附件对象名称
- **返回体格式**: 文件流下载

#### 4.2.4 提交反馈
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/officer/add/feedback`
- **接口备注**: 干事提交任务反馈
- **接口功能**: 干事为子任务提交完成反馈
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Feedback` 对象
    - `breakdownId`: 子任务ID
    - `feedbackContent`: 反馈内容
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 4.3 教师接口 (/api/teacher)

#### 4.3.1 上传主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/add/mainTask`
- **接口备注**: 教师创建主任务
- **接口功能**: 教师创建新的主任务及子任务
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `MainPostInfo` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.2 获取主任务列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/get/mainTask`
- **接口备注**: 教师查看主任务列表
- **接口功能**: 教师分页查询自己的主任务
- **请求方法**: GET
- **接口传参**: 
  - Query: `teacherName` (String, 必填) - 教师名称
  - Query: `pageSize` (Integer, 必填) - 每页数量
  - Query: `page` (Integer, 必填) - 页码
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "list": []
  }
}
```

#### 4.3.3 删除主任务
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/delete/mainTask`
- **接口备注**: 教师删除主任务
- **接口功能**: 教师删除自己创建的主任务
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `id` (Integer, 必填) - 主任务ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.4 添加反馈
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/add/feedback`
- **接口备注**: 教师添加任务反馈
- **接口功能**: 教师对子任务添加反馈意见
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Feedback` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.5 上传需求
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/add/requirements`
- **接口备注**: 教师上传任务需求
- **接口功能**: 教师为主任务添加需求
- **请求方法**: POST
- **接口传参**: 
  - Body (JSON): `Requirement` 对象
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.6 删除需求
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/delete/requirements`
- **接口备注**: 教师删除任务需求
- **接口功能**: 教师删除主任务的需求（同时删除关联附件）
- **请求方法**: DELETE
- **接口传参**: 
  - Query: `id` (Integer, 必填) - 需求ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.7 教师上传附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/add/Annexs`
- **接口备注**: 教师上传需求附件
- **接口功能**: 教师为需求上传附件
- **请求方法**: POST
- **接口传参**: 
  - Form-Data:
    - `file`: 附件文件数组
    - `requirementId`: 需求ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

#### 4.3.8 教师查看附件列表
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/get/AnnexList`
- **接口备注**: 教师查看需求附件
- **接口功能**: 教师查看需求的附件列表
- **请求方法**: GET
- **接口传参**: 
  - Query: `requirementId` (Integer, 必填) - 需求ID
- **返回体格式**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": []
}
```

#### 4.3.9 教师下载附件
- **接口路径**: `https://nc-wxwjc.gcu.edu.cn/api/check/api/teacher/download/Annex`
- **接口备注**: 教师下载需求附件
- **接口功能**: 教师下载需求的附件文件
- **请求方法**: GET
- **接口传参**: 
  - Query: `objectName` (String, 必填) - 附件对象名称
- **返回体格式**: 文件流下载

---

## 五、通用说明

### 5.1 认证方式

所有接口需要在请求头中携带JWT Token进行认证：

```
Authorization: Bearer <token>
```

### 5.2 网关路由规则

网关会自动将请求路由到对应的微服务：
- `/permission/**` → permission服务
- `/recruitment/**` → recruitment服务
- `/sign/**` → sign服务
- `/check/**` → check服务

### 5.3 用户ID传递

网关会自动从JWT Token中提取用户ID并添加到请求头：
```
X-USER-ID: <userId>
```

### 5.4 错误响应格式

```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

常见错误码：
- 200: 操作成功
- 400: 请求参数错误
- 401: 未认证/Token过期
- 403: 无权限访问
- 404: 资源不存在
- 500: 服务器内部错误

---

## 六、接口统计

| 服务模块 | 接口数量 |
|---------|---------|
| 权限管理 (permission) | 56 |
| 签到服务 (sign) | 15 |
| 招新服务 (recruitment) | 18 |
| 考核服务 (check) | 23 |
| **总计** | **112** |

---

**文档生成时间**: 2026-05-28

**文档版本**: v1.0

**注意事项**:
1. 所有时间格式建议使用 ISO 8601 标准: `yyyy-MM-dd HH:mm:ss`
2. 文件上传接口请使用 `multipart/form-data` 格式
3. JSON请求请设置 `Content-Type: application/json`
4. 部分接口需要特定权限才能访问，请联系管理员配置
