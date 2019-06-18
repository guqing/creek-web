package xyz.guqing.authorization.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.guqing.authorization.common.Const;
import xyz.guqing.authorization.common.JwtTokenUtil;
import xyz.guqing.authorization.entity.UserToken;
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
    private UserTokenService userTokenService;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader( Const.HEADER_STRING );
        if (authHeader != null && authHeader.startsWith( Const.TOKEN_PREFIX )) {
            final String authToken = authHeader.substring( Const.TOKEN_PREFIX.length() );
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
                    String newToken = generateTokenAndSetDB(userDetails);
                    response.setHeader(Const.HEADER_STRING, newToken);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private String generateTokenAndSetDB(UserDetails userDetails){
        final String token = jwtTokenUtil.generateToken(userDetails);
        //将token写入数据库中
        UserToken userToken = new UserToken();
        userToken.setUsername(userDetails.getUsername());
        userToken.setToken(token);
        userTokenService.save(userToken);
        return token;
    }
}