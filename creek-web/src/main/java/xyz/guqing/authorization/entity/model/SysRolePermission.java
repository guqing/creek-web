package xyz.guqing.authorization.entity.model;

import java.io.Serializable;

public class SysRolePermission implements Serializable {
    private Integer sysRoleId;

    private Long sysPermissionId;

    public Integer getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Integer sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Long getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(Long sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }
}