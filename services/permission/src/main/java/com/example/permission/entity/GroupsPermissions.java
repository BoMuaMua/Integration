package com.example.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Groups_Permissions")
public class GroupsPermissions {
    private Integer groupId;
    private Integer permissionId;

    public GroupsPermissions(Integer groupId, Integer permissionId) {
        this.groupId = groupId;
        this.permissionId = permissionId;
    }
}
