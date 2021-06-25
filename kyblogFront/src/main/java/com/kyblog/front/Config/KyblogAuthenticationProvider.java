package com.kyblog.front.Config;

import com.kyblog.api.entity.Role;
import com.kyblog.api.entity.User;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.front.Service.KyblogUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class KyblogAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    RedisOpsUtils redisOpsUtils;
    @Autowired
    KyblogUserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = new User();
        user.setUsername((String) authentication.getPrincipal());
        user.setPassword((String) authentication.getCredentials());
        if (redisOpsUtils.hasKey("LOGIN_SESSION")){
            String token = (String) redisOpsUtils.get("LOGIN_SESSION");
            String userRole = JwtTokenUtil.getUserRole(token);
            String username = JwtTokenUtil.getUsername(token);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : userRole.split(",")) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        User userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        if (passwordEncoder.matches(user.getPassword(),userDetails.getPassword())) {
            List<Role> roles = userDetails.getRoles();
            StringBuilder auths = new StringBuilder();
            for (Role role:
                 roles) {
                auths.append(role.getRoleName());
                auths.append(",");
            }
            redisOpsUtils.set("LOGIN_SESSION", JwtTokenUtil.createToken(user.getUsername(), auths.toString()),3600);
            return new UsernamePasswordAuthenticationToken(user.getUsername(), null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("密码错误");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
