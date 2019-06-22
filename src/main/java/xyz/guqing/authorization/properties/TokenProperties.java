package xyz.guqing.authorization.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.security.token")
public class TokenProperties {
    private long expirationTime = 30;     //30分钟(以秒ms计)1_800_000 5天为432_000_000
    private long allowedClockSkewSeconds = 30;
    private String secret = "CodeSheepSecret";      // JWT密码
    private String tokenPrefix = "Bearer";         // Token前缀
    private String headerString = "Authorization"; // 存放Token的Header Key

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeaderString() {
        return headerString;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }

    public long getAllowedClockSkewSeconds() {
        return allowedClockSkewSeconds;
    }

    public void setAllowedClockSkewSeconds(long allowedClockSkewSeconds) {
        this.allowedClockSkewSeconds = allowedClockSkewSeconds;
    }
}
