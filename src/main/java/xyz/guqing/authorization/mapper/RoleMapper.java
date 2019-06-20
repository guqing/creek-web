package xyz.guqing.authorization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.guqing.authorization.entity.model.SysRole;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("SELECT r.* from user u LEFT JOIN sys_user_role sur on sur.sys_user_id=u.id LEFT JOIN sys_role r on r.id=sur.sys_role_id WHERE u.id=1")
    public List<SysRole> queryRolesByUserId(Integer userId);
}
