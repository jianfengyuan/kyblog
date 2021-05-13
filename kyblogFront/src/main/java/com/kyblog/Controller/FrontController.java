package com.kyblog.Controller;

import com.kyblog.entity.*;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-08 16:20
 **/
@Controller
public class FrontController extends BaseController implements kyblogConstant {

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model, Page page) {
        Map<String, Object> map = new HashMap<>();
        OrderMode orderModeTemplate = new OrderMode();
        Page pageTemplate = new Page();
        Comment commentTemplate = new Comment();


        map.put("status", ARTICLE_STATUS_ACTIVE);
        int articleRows = restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/articles/rows?status=1", int.class);
        page.setRows(articleRows);
        page.setPath("/");
        orderModeTemplate.setColumn("read_count");

        map.put("page", page);
        map.put("orderMode", orderModeTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<Article>> articleResponseEntity =
                restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/articles/list",
                        HttpMethod.POST,
                        new HttpEntity<>(map),
                        ARTICLE_REF);
        List<Article> articles = articleResponseEntity.getBody();
        System.out.println(articles);
        pageTemplate.setLimit(1);
        pageTemplate.setCurrent(1);
        orderModeTemplate.setColumn("time");
        List<Tag> tagList;
        Integer rows;
        List<Comment> comments;
        orderModeTemplate.setColumn("time");
        pageTemplate.setLimit(1);
        pageTemplate.setCurrent(1);
        map.put("page", pageTemplate);
        map.put("orderMode", orderModeTemplate);
        for (Article article :
                articles) {
            ResponseEntity<List<Tag>> tagResponseEntity =
                    restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/tags/list",
                            HttpMethod.POST,
                            new HttpEntity<>(map),
                            TAG_REF);
            tagList = tagResponseEntity.getBody();
            article.setTags(tagList);
            commentTemplate.setArticleId(article.getId());
            map.put("comment", commentTemplate);
            rows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", map, Integer.class);
            if (rows != null && rows > 0) {
                ResponseEntity<List<Comment>> commentResponseEntity =
                        restTemplate.exchange(COMMENT_SERVICE_PREFIX + "/comments/list",
                                HttpMethod.POST,
                                new HttpEntity<>(map),
                                COMMENT_REF);
                comments = commentResponseEntity.getBody();
                assert comments != null;
                article.setComment(comments.get(0));
            }
        }

        pageTemplate.setCurrent(1);
        pageTemplate.setLimit(10);
        orderModeTemplate.setColumn("read_count");
        map.put("page", pageTemplate);
        map.put("orderMode", orderModeTemplate);
        articleResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/articles/list", HttpMethod.POST,
                new HttpEntity<>(map), ARTICLE_REF);
        List<Article> famousArticleList = articleResponseEntity.getBody();
        orderModeTemplate.setColumn("article_count");
        map.remove("page");
        map.put("status", KIND_STATUS_ACTIVE);
        ResponseEntity<List<Kind>> kindResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/kinds/list",
                HttpMethod.POST, new HttpEntity<>(map), KIND_REF);
        List<Kind> kindList = kindResponseEntity.getBody();
        orderModeTemplate.setColumn("article_count");
        map.put("status", TAG_STATUS_ACTIVE);
        ResponseEntity<List<Tag>> tagResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/tags/list",
                HttpMethod.POST, new HttpEntity<>(map), TAG_REF);
        List<Tag> tagCloud = tagResponseEntity.getBody();
        model.addAttribute("articles", articles);
        model.addAttribute("kinds", kindList);
        model.addAttribute("tags", tagCloud);
        model.addAttribute("famousArticles", famousArticleList);
        model.addAttribute("page", page);
        return "/front/index";
    }

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public String article(@RequestParam("articleId") Long articleId, Model model) {
        Comment commentTemplate = new Comment();
        OrderMode orderModeTemplate = new OrderMode();
        Comment replyTemplate = new Comment();
        Page pageTemplate = new Page();
        Map<String, Object> map = new HashMap<>();
        // 查询 目标文章
        // TODO: 通过Redis保证 article 不为空
        Article article = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/articles/article/" + articleId, Article.class
        );
        // 查询文章对应的 Tag 和 Kind
        ResponseEntity<List<Tag>> tagsResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/tags/list?articleId=" +articleId ,
                HttpMethod.GET, new HttpEntity<>(map), TAG_REF
        );
        List<Tag> tagList = tagsResponseEntity.getBody();
        System.out.println(tagList);
        Kind kind = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/kinds/kind", Kind.class,
                new HashMap<>().put("articleId", articleId)
        );
        assert article != null;
        article.setKind(kind);
        article.setTags(tagList);

        // 查询 热门文章
        // 按 read_count 排序, 取前10篇热门文章
        pageTemplate.setCurrent(1);
        pageTemplate.setLimit(10);
        orderModeTemplate.setColumn("read_count");
        map.put("page", pageTemplate);
        map.put("orderMode", orderModeTemplate);
        ResponseEntity<List<Article>> articlesResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX+"/articles/list", HttpMethod.POST,
                new HttpEntity<>(map), ARTICLE_REF
        );
        List<Article> famousArticleList = articlesResponseEntity.getBody();
        // 查询 热门Kind
        // 按 article_kind 排序
        orderModeTemplate.setColumn("article_count");
        map.put("status", KIND_STATUS_ACTIVE);
        map.put("orderMode", orderModeTemplate);
        map.remove("page");
        ResponseEntity<List<Kind>> kindResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/kinds/list", HttpMethod.POST,
                new HttpEntity<>(map), KIND_REF
        );
        List<Kind> kindList = kindResponseEntity.getBody();

        // 查询 标签云
        // 按 article_count 排序
        orderModeTemplate.setColumn("article_count");
        ResponseEntity<List<Tag>> tagResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/tags/list",
                HttpMethod.POST, new HttpEntity<>(map), TAG_REF);
        List<Tag> tagCloud = tagResponseEntity.getBody();

        // 查询文章对应评论
        // 按 time 排序
        commentTemplate.setType(COMMENT);
        commentTemplate.setArticleId(articleId);
        commentTemplate.setStatus(COMMENT_ACTIVE);
        orderModeTemplate.setColumn("time");
        map.put("comment", commentTemplate);
        ResponseEntity<List<Comment>> commentResponseEntity = restTemplate.exchange(
                COMMENT_SERVICE_PREFIX+"/comments/list", HttpMethod.POST,
                new HttpEntity<>(map), COMMENT_REF
        );
        List<Comment> commentList = commentResponseEntity.getBody();
        // 查询 评论对应回复
        // 按 time 排序
        orderModeTemplate.setColumn("time");
        replyTemplate.setArticleId(articleId);
        replyTemplate.setType(REPLY);
        map.put("comment", replyTemplate);
        for (Comment comment :
                commentList) {
            replyTemplate.setReplyId(comment.getId());
            ResponseEntity<List<Comment>> replyResponseEntity = restTemplate.exchange(
                    COMMENT_SERVICE_PREFIX + "/comments/list", HttpMethod.POST,
                    new HttpEntity<>(map), COMMENT_REF
            );
            List<Comment> replies = replyResponseEntity.getBody();
            comment.setReplies(replies);
        }

        model.addAttribute("famousArticles",famousArticleList);
        model.addAttribute("article", article);
        model.addAttribute("tags", tagCloud);
        model.addAttribute("comments", commentList);
        model.addAttribute("kinds", kindList);
        return "/front/article";
    }
}
