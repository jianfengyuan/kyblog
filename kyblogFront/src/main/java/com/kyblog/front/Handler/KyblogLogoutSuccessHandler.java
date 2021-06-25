package com.kyblog.front.Handler;

import com.kyblog.api.utils.RedisOpsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class KyblogLogoutSuccessHandler implements LogoutSuccessHandler {
    @Value("${logout.url}")
    private String url;

    @Autowired
    RedisOpsUtils redisOpsUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        redisOpsUtils.del("LOGIN_SESSION");
        httpServletResponse.sendRedirect(url);
    }
}
