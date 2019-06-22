package xyz.guqing.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.guqing.authorization.common.JwtTokenUtil;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;
import xyz.guqing.authorization.service.SysPermissionService;
import xyz.guqing.authorization.service.SysRoleService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizationApplicationTests {

    @Autowired
    JwtTokenUtil tokenUtil;

    @Test
    public void contextLoads() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndXFpbmciLCJjcmVhdGVkIjoxNTYxMDQ5OTEwOTU2LCJleHAiOjE1NjEwNDk5NDB9._ImL8Z4eO4SNE9KHP8BislLteIyjIUtx24zs0zeN3NdbtEauzHF-FQg3xai85Xoy2Su673xQRJQ_v_QZqHMUmA";
        boolean canRefreshed = tokenUtil.canTokenBeRefreshed(token);

        System.out.println(canRefreshed);

    }

}
