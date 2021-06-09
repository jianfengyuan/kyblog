package com.kyblog.article.Controller;

import com.kyblog.api.elasticsearch.ArticleRepository;
import com.kyblog.article.Service.*;
import com.kyblog.api.utils.ImageUtils;
import com.kyblog.api.utils.RedisOpsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    protected ArticleTagService articleTagService;

    @Autowired
    protected ElasticSearchService elasticSearchService;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected RedisOpsUtils redisOpsUtils;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected KindService kindService;

    @Autowired
    protected TagService tagService;

    @Autowired
    protected ArticleKindService articleKindService;

    protected ImageUtils imageUtils;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;


    /**
     * 在每个子类方法调用之前先调用，设置request,response,session这三个对象
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
    }
}
