package xyz.guqing.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizationApplicationTests {

    @Autowired
    MySecurityAutoConfiguration securityProperties;

    @Test
    public void contextLoads() {
        System.out.println(securityProperties.getTokenProperties().getSecret());
    }

}
