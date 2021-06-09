package com.kyblog.front.Controller;


import com.kyblog.api.entity.Article;
import com.kyblog.api.entity.Comment;
import com.kyblog.api.entity.Kind;
import com.kyblog.api.entity.Tag;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import com.kyblog.api.vo.VisitorStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-11 10:54
 **/

@Controller
public class FrontBaseController implements kyblogConstant {
    @Qualifier("restTemplateWithRibbon")
    @Autowired
    protected RestTemplate restTemplate;

    @Qualifier("redisOpsUtils")
    @Autowired
    protected RedisOpsUtils redisOpsUtils;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    protected ParameterizedTypeReference<List<Tag>> TAG_REF = new ParameterizedTypeReference<List<Tag>>() {
    };

    protected ParameterizedTypeReference<List<Article>> ARTICLE_REF = new ParameterizedTypeReference<List<Article>>() {
    };

    protected ParameterizedTypeReference<List<Comment>> COMMENT_REF = new ParameterizedTypeReference<List<Comment>>() {
    };

    protected ParameterizedTypeReference<List<Kind>> KIND_REF = new ParameterizedTypeReference<List<Kind>>() {
    };
    protected ParameterizedTypeReference<List<VisitorStatistics>> VISITORSTATISTICS_REF = new ParameterizedTypeReference<List<VisitorStatistics>>() {
    };


    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }



}
