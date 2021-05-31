package com.kyblog.front.Controller;

import com.kyblog.api.entity.*;
import com.kyblog.api.redisKey.ArticleKey;
import com.kyblog.api.redisKey.StatisticsKey;
import com.kyblog.api.utils.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
public class FrontControllerFront extends FrontBaseController {

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model, Page page) {
        update(request,-1L);
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
        update(request,articleId);
        Comment commentTemplate = new Comment();
        OrderMode orderModeTemplate = new OrderMode();
        Comment replyTemplate = new Comment();
        Page pageTemplate = new Page();
        Map<String, Object> map = new HashMap<>();
        // 查询 目标文章
        // TODO: 通过Redis保证 article 不为空
        Article article;
        if (redisOpsUtils.hasKey(ArticleKey.getById.getPrefix())) {
            article = (Article) redisOpsUtils.get(ArticleKey.getById.getPrefix() + ":" + articleId);
        } else {
            article = restTemplate.getForObject(
                    ARTICLE_SERVICE_PREFIX + "/articles/article/" + articleId, Article.class
            );
        }
        assert article != null;
        if (!redisOpsUtils.hasKey(ArticleKey.getByReadCount.getPrefix() + ":" + articleId)) {
            redisOpsUtils.set(ArticleKey.getByReadCount.getPrefix() + ":" + articleId, article.getReadCount());
        }
        Long count = redisOpsUtils.incr(ArticleKey.getByReadCount.getPrefix() + ":" + articleId, 1L);
        // 查询文章对应的 Tag 和 Kind
        ResponseEntity<List<Tag>> tagsResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/tags/list?articleId=" +articleId ,
                HttpMethod.GET, new HttpEntity<>(map), TAG_REF
        );
        List<Tag> tagList = tagsResponseEntity.getBody();
        System.out.println(tagList);
        map.put("articleId", articleId);
        Kind kind = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/kinds/kind?articleId={articleId}", Kind.class,
                map
        );
//        assert article != null;
        article.setKind(kind);
        article.setTags(tagList);
        article.setReadCount(count);

        // 查询 热门文章
        // 按 read_count 排序, 取前10篇热门文章
        List<Article> famousArticleList = new ArrayList<>();
        if (redisOpsUtils.hasKey(ArticleKey.getFamousArticles.getPrefix())) {
            List<Object> objects =  redisOpsUtils.lGet(ArticleKey.getFamousArticles.getPrefix(),0,-1);
            for (Object o :
                    objects) {
                famousArticleList.add((Article) o);
            }
        } else {
            pageTemplate.setCurrent(1);
            pageTemplate.setLimit(10);
            orderModeTemplate.setColumn("read_count");
            map.put("page", pageTemplate);
            map.put("orderMode", orderModeTemplate);
            ResponseEntity<List<Article>> articlesResponseEntity = restTemplate.exchange(
                    ARTICLE_SERVICE_PREFIX+"/articles/list", HttpMethod.POST,
                    new HttpEntity<>(map), ARTICLE_REF
            );
            famousArticleList = articlesResponseEntity.getBody();
            assert famousArticleList != null;
            for (Article a:
                 famousArticleList) {
                redisOpsUtils.lSet(ArticleKey.getFamousArticles.getPrefix(), a);
            }
//            redisOpsUtils.set(ArticleKey.getFamousArticles.getPrefix(), famousArticleList);
        }

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
        map.put("orderMode", orderModeTemplate);
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

    public void update(HttpServletRequest request, Long articleId) {
        String ip = IPUtils.getIpAddress(request);
        redisOpsUtils.incr(StatisticsKey.todayVisitor.getPrefix() + ":" + ip + ":" + articleId, 1L);
    }
}