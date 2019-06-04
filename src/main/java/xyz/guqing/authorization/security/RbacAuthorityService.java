package xyz.guqing.authorization.security;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import xyz.guqing.authorization.entity.MyUserDetails;
import xyz.guqing.authorization.entity.SysPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("rbacauthorityservice")
public class RbacAuthorityService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object principal = authentication.getPrincipal();
        System.out.println(JSONObject.toJSONString(principal));
        boolean hasPermission  = false;
        
        if (principal instanceof MyUserDetails) {
            MyUserDetails myUserDetails = (MyUserDetails)principal;
            String username = ((UserDetails)principal).getUsername();

            //获取资源
            // 这些 url 都是要登录后才能访问，且其他的 url 都不能访问
            List<SysPermission> permissionList = new ArrayList<>();
            SysPermission sysPermission = new SysPermission();
            sysPermission.setId(1);
            sysPermission.setName("hello");
            sysPermission.setUrl("/hello");
            permissionList.add(sysPermission);
            //获取权限url
            Set<String> urls = new HashSet<>();
            for(SysPermission permission : permissionList){
                urls.add(permission.getUrl());
            }
            System.out.println(request.getRequestURI());
            System.out.println("-->"+ JSONArray.toJSONString(urls));
            hasPermission = matcherUrl(request, urls);
    
            return hasPermission;
        }
        return hasPermission;
    }
    
    /**
     * 匹配url规则
     * @param request
     * @param urls
     */
    private boolean matcherUrl(HttpServletRequest request, Set<String> urls) {
        boolean hasPermission = false;
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        
        for (String url : urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }
}
