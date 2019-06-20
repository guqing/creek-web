package xyz.guqing.authorization.entity.model;

import java.io.Serializable;

public class SysUserRole implements Serializable {
    private Long sysUserId;

    private Integer sysRoleId;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Integer sysRoleId) {
        this.sysRoleId = sysRoleId;
    }
}