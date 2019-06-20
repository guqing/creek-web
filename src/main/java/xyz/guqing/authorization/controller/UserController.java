package xyz.guqing.authorization.controller;

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

    @PostMapping("/register")
    public Object register(@RequestBody User user){
        return ResponseUtil.ok(user);
    }
}
