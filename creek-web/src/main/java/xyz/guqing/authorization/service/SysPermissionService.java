package xyz.guqing.authorization.service;

import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.model.SysPermission;
import xyz.guqing.authorization.entity.model.SysRole;
import xyz.guqing.authorization.mapper.PermissionMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysPermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    public List<SysPermission> queryPermissionByRoleIds(List<SysRole> roleList) {
        List<SysPermission> permissionList = new ArrayList<>();

        for(SysRole role : roleList) {
            List<SysPermission> sysPermissions = permissionMapper.queryPermissionByRoleIds(role.getId());
            permissionList.addAll(sysPermissions);
        }

        return permissionList;
    }
}
