package xyz.guqing.authorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "恭喜你看到我了说明你授权成功了";
    }

    @RequestMapping("/get")
    public String get(){
        return "我是任何人都可以访问的资源";
    }

    @GetMapping("/none")
    public String denie(){
        return "我是没有权限访问的资源";
    }
}
