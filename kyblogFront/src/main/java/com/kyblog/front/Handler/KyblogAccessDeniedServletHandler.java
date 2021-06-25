package com.kyblog.front.Handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KyblogAccessDeniedServletHandler implements AccessDeniedHandler {
    private final String url;

    public KyblogAccessDeniedServletHandler(String url) {
        this.url = url;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.sendRedirect(url);
    }
}
