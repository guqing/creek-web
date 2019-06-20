package xyz.guqing.authorization.service;

import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.model.SysPermission;
import xyz.guqing.authorization.mapper.PermissionMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysPermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    public List<SysPermission> queryPermissionByRoleId(){
        
    }
}
