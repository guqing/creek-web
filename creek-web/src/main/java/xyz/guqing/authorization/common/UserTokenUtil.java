package xyz.guqing.authorization.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserTokenUtil {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String generateTokenAndSetDB(UserDetails userDetails) {
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }
}
