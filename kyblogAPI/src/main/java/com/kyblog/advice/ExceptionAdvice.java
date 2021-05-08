package com.kyblog.advice;

import com.kyblog.utils.BlogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-05 10:32
 **/
@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element :
                e.getStackTrace()) {
            logger.error(element.toString());
        }

        /* 判断请求方式, 如果是异步请求, 则返回Json格式错误信息
        *  如果是页面请求, 则返回错误页面 */
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            /* 这是一个异步请求*/
            response.setContentType("application/json;charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(BlogUtils.getJsonString(500, "服务器异常"));
        } else {
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}
