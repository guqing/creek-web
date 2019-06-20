package xyz.guqing.authorization.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.guqing.authorization.common.JwtTokenUtil;
import xyz.guqing.authorization.common.ResponseUtil;
import xyz.guqing.authorization.entity.dto.MyUserDetails;
import xyz.guqing.authorization.entity.dto.UserToken;
import xyz.guqing.authorization.service.UserTokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserTokenService userTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //登录成功，生成token
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);

        //将token写入数据库中
        UserToken userToken = new UserToken();
        userToken.setUsername(userDetails.getUsername());
        userToken.setToken(token);
        userTokenService.save(userToken);

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseUtil.ok(token)));
    }
}

