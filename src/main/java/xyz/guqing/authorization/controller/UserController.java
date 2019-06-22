package xyz.guqing.authorization.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.guqing.authorization.common.ResponseUtil;
import xyz.guqing.authorization.common.UserTokenUtil;
import xyz.guqing.authorization.service.MyUserDetailsService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Resource
    private MyUserDetailsService userDetailsService;
    @Autowired
    private UserTokenUtil userTokenutil;

    @ApiOperation(value="用户登录授权", notes="根据用户名和密码获得token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Object login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordEqual = bCryptPasswordEncoder.matches(password, userDetails.getPassword());

        if(userDetails != null && isPasswordEqual) {
            String token = userTokenutil.generateTokenAndSetDB(userDetails);

            return ResponseUtil.ok(token);
        }
        return ResponseUtil.fail(403, "认证失败，用户名或密码不正确");
    }

    @ApiOperation(value="用户注册", notes="根据用户填写信息注册用户")
    @ApiImplicitParam(
            name = "user", value = "用户实体", required = true, dataType = "User"
    )
    @PostMapping("/register")
    public Object register(@RequestBody User user){
        return ResponseUtil.ok(user);
    }
}
