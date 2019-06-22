package xyz.guqing.authorization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.guqing.authorization.entity.model.SysPermission;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Select("SELECT p.* FROM sys_role r LEFT JOIN sys_role_permission rp on rp.sys_role_id=r.id JOIN sys_permission p on p.id=rp.sys_permission_id WHERE r.id=#{roleId}")
    List<SysPermission> queryPermissionByRoleIds(@Param("roleId") Integer roleId);
}
