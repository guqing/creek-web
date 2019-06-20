package xyz.guqing.authorization.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import xyz.guqing.authorization.entity.dto.MyUserDetails;
import xyz.guqing.authorization.entity.model.SysPermission;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("rbacauthorityservice")
public class RbacAuthorityService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object principal = authentication.getPrincipal();

        boolean hasPermission  = false;
        
        if (principal instanceof MyUserDetails) {
            MyUserDetails myUserDetails = (MyUserDetails)principal;
            String username = ((UserDetails)principal).getUsername();

            //获取资源
            // 这些 url 都是要登录后才能访问，且其他的 url 都不能访问
            List<SysPermission> permissionList = new ArrayList<>();
            SysPermission sysPermission = new SysPermission();
            sysPermission.setId(1L);
            sysPermission.setName("hello");
            sysPermission.setUrl("/hello");
            permissionList.add(sysPermission);
            //获取权限url
            Set<String> urls = new HashSet<>();
            for(SysPermission permission : permissionList){
                urls.add(permission.getUrl());
            }
            hasPermission = matcherUrl(request, urls);
    
            return hasPermission;
        }else if("anonymousUser".equalsIgnoreCase(principal.toString())){
            //公共资源
            Set<String> urls = new HashSet<>();
            urls.add("/get");
            hasPermission = matcherUrl(request, urls);
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
