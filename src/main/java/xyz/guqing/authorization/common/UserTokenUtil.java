package xyz.guqing.authorization.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import xyz.guqing.authorization.entity.dto.UserToken;
import xyz.guqing.authorization.service.UserTokenService;

@Component
public class UserTokenUtil {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserTokenService userTokenService;

    public String generateTokenAndSetDB(UserDetails userDetails){
        final String token = jwtTokenUtil.generateToken(userDetails);
        //将token写入数据库中
        UserToken userToken = new UserToken();
        userToken.setUsername(userDetails.getUsername());
        userToken.setToken(token);
        userTokenService.save(userToken);

        return token;
    }
}
