package xyz.guqing.authorization.service;

import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.model.SysRole;
import xyz.guqing.authorization.mapper.RoleMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleService {
    @Resource
    private RoleMapper roleMapper;

    public List<SysRole> getRoleByUserId(Integer userId){
        List<SysRole> roleList = roleMapper.queryRolesByUserId(userId);

        return roleList;
    }

}
