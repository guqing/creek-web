package xyz.guqing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.dto.MyUserDetails;
import xyz.guqing.authorization.entity.model.SysPermission;
import xyz.guqing.authorization.entity.model.SysRole;
import xyz.guqing.authorization.entity.model.User;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails userDetails = new MyUserDetails();
        User user = userService.getUserByUsername(username);
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());

        //赋值角色和权限
        Long userId = user.getId();
        List<SysRole> roleList = roleService.getRoleByUserId(userId.intValue());
        userDetails.setSysRoles(roleList);
        List<SysPermission> sysPermissions = permissionService.queryPermissionByRoleIds(roleList);
        userDetails.setPermissions(sysPermissions);

        return userDetails;
    }

}