package com.kyblog.front.Filter;

import com.kyblog.api.config.LogAspect;
import com.kyblog.api.entity.User;
import com.kyblog.front.Config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtLoginFilter.class);
    private AuthenticationManager authenticationManager;
    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        logger.info("username: " + username + " password: " + password);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        User user = new User();
//        user.setUsername((String) authResult.getPrincipal());
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        StringBuilder auths = new StringBuilder();
        for (GrantedAuthority auth :
                authorities) {
            auths.append(auth.getAuthority());
            auths.append(",");
        }
        logger.info((String) authResult.getPrincipal());
        String token = JwtTokenUtil.createToken((String) authResult.getPrincipal(), auths.toString());
        logger.info("token: " + token);
        logger.info(JwtTokenUtil.getUserRole(token));
        SecurityContextHolder.getContext().setAuthentication(authResult);
        response.setHeader(JwtTokenUtil.TOKEN_HEADER, JwtTokenUtil.TOKEN_PREFIX + token);
        response.sendRedirect("/admin/dashboard");
    }


}
