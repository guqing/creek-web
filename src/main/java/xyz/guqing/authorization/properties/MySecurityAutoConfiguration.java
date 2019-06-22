package xyz.guqing.authorization.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TokenProperties.class, LoginProperties.class, SystemProperties.class})
public class MySecurityAutoConfiguration {

    private final TokenProperties tokenProperties;
    private final LoginProperties loginProperties;
    private final SystemProperties systemProperties;

    public MySecurityAutoConfiguration(TokenProperties tokenProperties, LoginProperties loginProperties, SystemProperties systemProperties) {
        this.tokenProperties = tokenProperties;
        this.loginProperties = loginProperties;
        this.systemProperties = systemProperties;
    }

    public TokenProperties getTokenProperties() {
        return tokenProperties;
    }

    public LoginProperties getLoginProperties() {
        return loginProperties;
    }

    public SystemProperties getSystemProperties() {
        return systemProperties;
    }
}
