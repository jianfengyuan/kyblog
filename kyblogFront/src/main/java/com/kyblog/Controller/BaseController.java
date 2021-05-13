package com.kyblog.Controller;

import com.kyblog.entity.Article;
import com.kyblog.entity.Comment;
import com.kyblog.entity.Kind;
import com.kyblog.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-11 10:54
 **/

@Controller
public class BaseController {
    @Qualifier("restTemplateWithRibbon")
    @Autowired
    protected RestTemplate restTemplate;
    //    如果配置了server.servlet.context-path, 服务名后记得带上context-path
    protected String ARTICLE_SERVICE_PREFIX = "http://ARTICLESERVICE";
    protected String COMMENT_SERVICE_PREFIX = "http://COMMENTSERVICE";


    protected ParameterizedTypeReference<List<Tag>> TAG_REF = new ParameterizedTypeReference<List<Tag>>() {
    };

    protected ParameterizedTypeReference<List<Article>> ARTICLE_REF = new ParameterizedTypeReference<List<Article>>() {
    };

    protected ParameterizedTypeReference<List<Comment>> COMMENT_REF = new ParameterizedTypeReference<List<Comment>>() {
    };

    protected ParameterizedTypeReference<List<Kind>> KIND_REF = new ParameterizedTypeReference<List<Kind>>() {
    };

}
