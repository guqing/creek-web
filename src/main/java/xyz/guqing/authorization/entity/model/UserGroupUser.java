package xyz.guqing.authorization.entity.model;

import java.io.Serializable;

public class UserGroupUser implements Serializable {
    private Integer userGroupId;

    private Integer userId;

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}