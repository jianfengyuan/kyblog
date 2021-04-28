package com.kyblog.Controller;

import com.kyblog.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-27 17:34
 **/

public class BaseController {

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected KindService kindService;

    @Autowired
    protected TagService tagService;

    @Autowired
    protected ArticleKindService articleKindService;


    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;


//    protected ResponseResult result;
//    protected Admin loginAdmin;
//    protected Admin user;
//    protected Front front;


    /**
     * 在每个子类方法调用之前先调用，设置request,response,session这三个对象
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
//        result = new ResponseResult();
        // 设置admin对象
//        loginAdmin = (Admin)session.getAttribute("admin");
        // 获取前台主页对象
//        front = frontService.queryById(1);
        // 获取用户对象
//        user = adminService.queryById(1);
//        user.setPassword(null);
//        user.setRecentLogin(null);
//        user.setOpenId(null);
//        user.setSalt(null);
    }
}
