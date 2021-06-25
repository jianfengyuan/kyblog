package com.kyblog.front.Config;

import com.kyblog.api.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Configuration
public class JwtTokenUtil {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static String secretKey = "123123";
    private static final String ROLE_CLAIMS = "role";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    public static String createToken(String username, String authorities) {
        Map<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, authorities);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION*1000))
                .compact();
    }

    public static String getUsername(String token) {
        Claims tokenBody = getTokenBody(token);
        return tokenBody.getSubject();
    }

    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
