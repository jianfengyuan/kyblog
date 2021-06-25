package com.kyblog.front.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyblog.api.entity.User;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.front.Config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    RedisOpsUtils redisOpsUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);

//        User user1 = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);
        logger.info("authToken: " + authToken);
//        if (redisOpsUtils.hasKey("Login_User_" + user.getUsername())){
        if (authToken!= null){
//            String token = (String) redisOpsUtils.get("Login_User_" + user.getUsername());
            String userRole = JwtTokenUtil.getUserRole(authToken);
            String username = JwtTokenUtil.getUsername(authToken);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : userRole.split(",")) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
