package com.kyblog.front.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyblog.api.entity.Role;
import com.kyblog.api.entity.User;
import com.kyblog.front.Config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class JwtPreAuthFilter extends BasicAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtPreAuthFilter.class);
    public JwtPreAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("BasicAuthenticationFilters");
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        logger.info("tokenHeader: " + tokenHeader);
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response,chain);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        String username = JwtTokenUtil.getUsername(token);
        String userRole = JwtTokenUtil.getUserRole(token);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (userRole!=null) {
            for (String role : userRole.split(",")) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        if (username != null){
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;
    }
}
