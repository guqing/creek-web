package xyz.guqing.authorization.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TokenProperties.class, LoginProperties.class})
public class MySecurityAutoConfiguration {

    private final TokenProperties tokenProperties;
    private final LoginProperties loginProperties;

    public MySecurityAutoConfiguration(TokenProperties tokenProperties, LoginProperties loginProperties) {
        this.tokenProperties = tokenProperties;
        this.loginProperties = loginProperties;
    }

    public TokenProperties getTokenProperties() {
        return tokenProperties;
    }

    public LoginProperties getLoginProperties() {
        return loginProperties;
    }
}
