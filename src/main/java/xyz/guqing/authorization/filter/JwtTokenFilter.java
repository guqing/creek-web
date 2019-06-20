package xyz.guqing.authorization.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.guqing.authorization.common.JwtTokenUtil;
import xyz.guqing.authorization.common.UserTokenUtil;
import xyz.guqing.authorization.properties.MySecurityAutoConfiguration;
import xyz.guqing.authorization.properties.TokenProperties;
import xyz.guqing.authorization.service.MyUserDetailsService;
import xyz.guqing.authorization.service.UserTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserTokenUtil userTokenutil;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    MySecurityAutoConfiguration securityProperties;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        TokenProperties tokenProperties = securityProperties.getTokenProperties();

        String authHeader = request.getHeader(tokenProperties.getHeaderString());
        if (authHeader != null && authHeader.startsWith(tokenProperties.getTokenPrefix())) {
            final String authToken = authHeader.substring(tokenProperties.getTokenPrefix().length() );
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else if(jwtTokenUtil.canTokenBeRefreshed(authToken)){
                    // token过期了，需要刷新token
                    String newToken = userTokenutil.generateTokenAndSetDB(userDetails);
                    response.setHeader(tokenProperties.getHeaderString(), newToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}