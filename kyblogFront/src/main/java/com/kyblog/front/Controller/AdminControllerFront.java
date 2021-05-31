package com.kyblog.front.Controller;

import com.kyblog.api.entity.*;
import com.kyblog.api.vo.StatisticsCount;
import com.kyblog.api.vo.VisitStatistics;
import com.kyblog.api.vo.VisitorStatistics;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.api.utils.BlogUtils.camel4underline;
import static com.kyblog.api.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-10 16:53
 **/
@Controller
@RequestMapping(value = "/admin")
public class AdminControllerFront extends FrontBaseController {
    @RequestMapping(value = "/articles/new", method = RequestMethod.GET)
    public String newPage(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("status", KIND_STATUS_ACTIVE);
        ResponseEntity<List<Kind>> kindResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/kinds/list",
                HttpMethod.POST, new HttpEntity<>(param), KIND_REF);
        List<Kind> kinds = kindResponseEntity.getBody();
        model.addAttribute("kinds", kinds);
        return "/admin/new";
    }

    @RequestMapping(value = "/articles/publish", method = RequestMethod.POST)
    @ResponseBody
    public String publish(String title, String content, String tags, String kind, String introduce, Integer status, String img) {
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("content", content);
        param.put("tags", tags);
        param.put("kind", kind);
        param.put("introduce", introduce);
        param.put("status", status);
        param.put("img", img);
        restTemplate.postForObject(ARTICLE_SERVICE_PREFIX + "/articles/publish",param,String.class);
        return getJsonString(200);
    }

    @RequestMapping(value = "/articles/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(Long id, String title, String content, String tags,
                         String kind, String introduce, Integer status, String img) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("title", title);
        param.put("content", content);
        param.put("tags", tags);
        param.put("kind", kind);
        param.put("introduce", introduce);
        param.put("status", status);
        param.put("img", img);
        restTemplate.postForObject(ARTICLE_SERVICE_PREFIX + "/articles/update",param,String.class);
        return getJsonString(200);
    }

    @RequestMapping(value = "/articles/edit", method = RequestMethod.GET)
    public String edict(@RequestParam(value = "articleId") Long articleId,Model model) {
        List<String> tagNameList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("status", KIND_STATUS_ACTIVE);
        ResponseEntity<List<Kind>> kindResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/kinds/list",
                HttpMethod.POST, new HttpEntity<>(param), KIND_REF);
        List<Kind> kinds = kindResponseEntity.getBody();
        param.put("articleId", articleId);
        Kind kind = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/kinds/kind?articleId={articleId}", Kind.class,
                param
        );
        Article article = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/articles/article/" + articleId, Article.class
        );
        assert article != null;
        article.setKind(kind);
        ResponseEntity<List<Tag>> tagsResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/tags/list?articleId=" +articleId ,
                HttpMethod.GET, new HttpEntity<>(param), TAG_REF
        );
        List<Tag> tagList = tagsResponseEntity.getBody();
        assert tagList != null;
        for (Tag tag :
                tagList) {
            tagNameList.add(tag.getName());
        }
        model.addAttribute("article", article);
        model.addAttribute("tag", String.join(";", tagNameList));
        model.addAttribute("kinds", kinds);
        return "/admin/edit";
    }

    @RequestMapping(path = "/articles", method = RequestMethod.GET)
    public String getArticles(Model model, Page page, OrderMode orderMode) {
        if (orderMode.getColumn() == null) {
            orderMode.setColumn("edict_time");
        }
        Comment commentTemplate = new Comment();
        commentTemplate.setStatus(COMMENT_ACTIVE);
        Kind kind;
        Integer rows;
        page.setPath("/articles");
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("orderMode", orderMode);
        ResponseEntity<List<Article>> articleResponseEntity =
                restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/articles/list",
                        HttpMethod.POST,
                        new HttpEntity<>(map),
                        ARTICLE_REF);
        List<Article> articleList = articleResponseEntity.getBody();
        map = new HashMap<>();
        map.put("comment", commentTemplate);
        for (Article article : articleList) {
            map.put("articleId", article.getId());
            kind = restTemplate.getForObject(
                    ARTICLE_SERVICE_PREFIX + "/kinds/kind?articleId={articleId}", Kind.class,
                    map
            );
            article.setKind(kind);
            ResponseEntity<List<Tag>> tagsResponseEntity = restTemplate.exchange(
                    ARTICLE_SERVICE_PREFIX + "/tags/list?articleId=" + article.getId(),
                    HttpMethod.GET, new HttpEntity<>(map), TAG_REF
            );
            List<Tag> tagList = tagsResponseEntity.getBody();
            article.setTags(tagList);
            commentTemplate.setArticleId(article.getId());
            rows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", map, Integer.class);
            article.setCommentCount(rows);
        }
        model.addAttribute("articles", articleList);
        return "/admin/articles";
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String getTags(Model model) {
        return "/admin/tags";
    }

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    @ResponseBody
    public String getTags(Page page, OrderMode orderMode) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        page.setPath("/");
        int rows =
                restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/tags/rows?status=" + TAG_STATUS_ACTIVE, Integer.class);
        page.setRows(rows);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        param.put("status", TAG_STATUS_ACTIVE);
        param.put("orderMode", orderMode);
        param.put("page", page);
        ResponseEntity<List<Tag>> tagsResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX + "/tags/list",
                HttpMethod.POST, new HttpEntity<>(param), TAG_REF
        );
        List<Tag> tagList = tagsResponseEntity.getBody();
        map.put("tags", tagList);
        map.put("rows", rows);
        return getJsonString(200, null, map);
    }

    @RequestMapping(value = "/tags", method = RequestMethod.PUT)
    @ResponseBody
    public String add(Tag tag) {
        restTemplate.put(ARTICLE_SERVICE_PREFIX+"/tags",tag);
        return getJsonString(200);
    }
    @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTag(@PathVariable("tagId") String tagId) {
        restTemplate.delete(ARTICLE_SERVICE_PREFIX+"/tags/"+ tagId);
        return getJsonString(200);
    }

    @RequestMapping(value = "/kinds", method = RequestMethod.GET)
    public String getKinds(Model model) {
        return "/admin/kinds";
    }

    @RequestMapping(value = "/kinds", method = RequestMethod.POST)
    @ResponseBody
    public String getKinds(OrderMode orderMode, Page page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        page.setPath("/");
        System.out.println(orderMode);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        int rows =
                restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/kinds/rows?status=" + TAG_STATUS_ACTIVE, Integer.class);
        page.setRows(rows);
        param.put("status", KIND_STATUS_ACTIVE);
        param.put("orderMode", orderMode);
        param.put("page", page);
        ResponseEntity<List<Kind>> kindResponseEntity = restTemplate.exchange(ARTICLE_SERVICE_PREFIX + "/kinds/list",
                HttpMethod.POST, new HttpEntity<>(param), KIND_REF);
        List<Kind> kindList = kindResponseEntity.getBody();
        map.put("kinds", kindList);
        map.put("rows", rows);
        return getJsonString(200, null, map);
    }

    @RequestMapping(value = "/kinds", method = RequestMethod.PUT)
    @ResponseBody
    public String add(Kind kind) {
        restTemplate.put(ARTICLE_SERVICE_PREFIX+"/kinds",kind);
        return getJsonString(200);
    }

    @RequestMapping(value = "/kinds/{kindId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteKind(@PathVariable("kindId") String kindId) {
        restTemplate.delete(ARTICLE_SERVICE_PREFIX+"/kinds/"+kindId);
        return getJsonString(200);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String getComments(Model model) {
        Comment comment = new Comment();
        Map<String, Object> param = new HashMap<>();
        param.put("comment", comment);
        comment.setStatus(COMMENT_ACTIVE);
        int activeRows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        comment.setReadStatus(COMMENT_UNREAD);
        int unreadRows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        model.addAttribute("rows", activeRows);
        model.addAttribute("unRead", unreadRows);
        return "/admin/comments";
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseBody
    public String getComments(Comment comment, OrderMode orderMode, Page page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("comment", comment);
        param.put("orderMode", orderMode);
        param.put("page", page);

        int rows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        page.setRows(rows);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        ResponseEntity<List<Comment>> commentResponseEntity =
                restTemplate.exchange(COMMENT_SERVICE_PREFIX + "/comments/list",
                        HttpMethod.POST,
                        new HttpEntity<>(param),
                        COMMENT_REF);
        List<Comment> commentList = commentResponseEntity.getBody();
        Article article;
        for (Comment c:
             commentList) {
            article =
                    restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/articles/article/" + c.getArticleId(), Article.class);
            c.setArticleTitle(article.getTitle());
        }
        map.put("comments", commentList);
        map.put("rows", rows);
        return getJsonString(200, null, map);
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashBoard(Model model) {
        // 获取各个状态下文章的数量
        Map<String, Integer> countMap = statusCount();
        Map<String, Object> param = new HashMap<>();
        Comment commentTemplate = new Comment();
        param.put("comment", commentTemplate);
        commentTemplate.setStatus(COMMENT_ACTIVE);
        Integer commentCount = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        Integer kindCount = restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/kinds/rows?status=" + TAG_STATUS_ACTIVE, Integer.class);
        Integer tagCount = restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/tags/rows?status=" + TAG_STATUS_ACTIVE, Integer.class);
        commentTemplate.setReadStatus(COMMENT_UNREAD);
        Integer unreadRows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        Map<String, StatisticsCount> statisticsCount = (Map<String, StatisticsCount>)restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/statisticsCount", Map.class);
        VisitStatistics visitStatistics = restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/visitStatistics", VisitStatistics.class);
        model.addAttribute("publishCount",countMap.get("publish"));
        model.addAttribute("draftCount",countMap.get("draft"));
        model.addAttribute("trashCount",countMap.get("trash"));
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("kindCount", kindCount);
        model.addAttribute("tagCount", tagCount);
        model.addAttribute("unRead", unreadRows);
        model.addAttribute("statistics", visitStatistics);
        model.addAttribute("statisticsCount", statisticsCount);
        System.out.println(visitStatistics.getDatesString());
        return "/admin/dashboard";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    @ResponseBody
    public String dashBoard() {
        return getJsonString(200);
    }

    public Map<String,Integer> statusCount(){
        Map<String,Integer> map = new HashMap<>();
        Integer draft = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/articles/rows?status="+ARTICLE_STATUS_DRAFT, Integer.class);
        Integer publish = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/articles/rows?status="+ARTICLE_STATUS_ACTIVE, Integer.class);
        Integer trash = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/articles/rows?status="+ARTICLE_STATUS_DELETED, Integer.class);
        map.put("draft",draft);
        map.put("publish",publish);
        map.put("trash",trash);
        return map;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String statistics(Model model) {
        Map<String, Object> param = new HashMap<>();
        Comment commentTemplate = new Comment();
        param.put("comment", commentTemplate);
        ResponseEntity<List<VisitorStatistics>> responseEntity = restTemplate.exchange(STATISTICS_SERVICE_PREFIX + "/statistics/visitorStatistics",
                HttpMethod.GET, null, VISITORSTATISTICS_REF);
        List<VisitorStatistics> visitorStatistics = responseEntity.getBody();
        visitorStatistics.forEach(temp->{
            if (temp.getStatistics().getArticleId() == -1L) {
                temp.setTarget("主页");
            } else {
                Long articleId = temp.getStatistics().getArticleId();
                Article article = restTemplate.getForObject(ARTICLE_SERVICE_PREFIX + "/articles/article/" + articleId, Article.class);
                temp.setTarget(article.getTitle());
            }
        });
//        List<VisitorStatistics> visitorStatistics = (List<VisitorStatistics>) restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/visitorStatistics", List.class);
        VisitStatistics visitStatistics = restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/visitStatistics", VisitStatistics.class);
        Integer unreadRows = restTemplate.postForObject(COMMENT_SERVICE_PREFIX + "/comments/rows", param, Integer.class);
        Map<String, StatisticsCount> statisticsCount = (Map<String, StatisticsCount>)restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/statisticsCount", Map.class);
        model.addAttribute("statistics",visitStatistics);
        // 获取所有访客信息
        model.addAttribute("visitors",visitorStatistics);
        // 获取统计数量信息
        model.addAttribute("statisticsCount",statisticsCount);
        // 获取未读评论数
        model.addAttribute("unRead",unreadRows);
        return "/admin/statistics";
    }
}
