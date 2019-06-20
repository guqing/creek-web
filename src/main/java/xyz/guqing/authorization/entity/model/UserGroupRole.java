package xyz.guqing.authorization.entity.model;

import java.io.Serializable;

public class UserGroupRole implements Serializable {
    private Integer userGroupId;

    private Integer roleId;

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}