package xyz.guqing.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.guqing.authorization.filter.JwtTokenFilter;
import xyz.guqing.authorization.properties.LoginProperties;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;
import xyz.guqing.authorization.security.*;
import xyz.guqing.authorization.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;  //  未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler;  //  登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;    // 无权访问返回的

    @Autowired
    AjaxLogoutSuccessHandler logoutSuccessHandler;  // 注销成功

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private MySecurityAutoConfiguration securityAutoConfiguration;

    @Bean
    public JwtTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userService ).passwordEncoder( new BCryptPasswordEncoder() );
    }

    @Override
    protected void configure( HttpSecurity httpSecurity ) throws Exception {
        LoginProperties loginProperties = securityAutoConfiguration.getLoginProperties();

        httpSecurity.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and()

                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll()

                .antMatchers("/auth/login", "/oauth/register")// 对登录注册要允许匿名访问
                .permitAll()

                .anyRequest()
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // RBAC 动态 url 认证

                .and()
                .logout().logoutUrl(loginProperties.getLogoutUrl())
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();



        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();
    }

}