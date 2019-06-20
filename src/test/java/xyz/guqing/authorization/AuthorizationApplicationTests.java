package xyz.guqing.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;
import xyz.guqing.authorization.service.SysPermissionService;
import xyz.guqing.authorization.service.SysRoleService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizationApplicationTests {

    @Autowired
    SysPermissionService permissionService;
    @Autowired
    SysRoleService roleService;

    @Test
    public void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }

}
