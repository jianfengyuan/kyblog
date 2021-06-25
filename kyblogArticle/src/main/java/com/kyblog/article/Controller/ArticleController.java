package com.kyblog.article.Controller;

import com.alibaba.fastjson.JSON;
import com.kyblog.api.entity.*;
import com.kyblog.api.redisKey.ArticleKey;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.api.utils.BlogUtils.getJsonString;

@Controller
//@RequestMapping(path = "/articles")
public class ArticleController extends BaseController implements kyblogConstant {

    @Deprecated
    @RequestMapping(path = "/articles/new", method = RequestMethod.GET)
    public String newPage(Model model) {
        List<Kind> kinds = kindService.selectKinds(KIND_STATUS_ACTIVE, null, null);
        model.addAttribute("kinds", kinds);
        return "/admin/new";
    }

    @RequestMapping(path = "/articles/publish", method = RequestMethod.POST)
    @ResponseBody
    public String publish(@RequestBody Map<String, Object> param) {
        if (param.get("title") == null) {
            return getJsonString(503, "title 不能为空");
        }
        articleService.publish((String) param.get("title"),
                (String) param.get("content"),
                (String) param.get("tags"),
                (String) param.get("kind"),
                (String) param.get("introduce"),
                (Integer) param.get("status"));
        return getJsonString(200);
    }

    @Deprecated
    @RequestMapping(path = "/articles/edit", method = RequestMethod.GET)
    public String getArticle(@RequestParam(value = "articleId", required = false) Long id, Model model) {
        Article article = new Article();
        List<String> tagNameList = new ArrayList<>();
        List<Kind> kinds = kindService.selectKinds(KIND_STATUS_ACTIVE, null, null);
        model.addAttribute("article", null);
        System.out.println(id);
        if (id != null) {
            Map<String, Object> map = new HashMap<>();
            article = articleService.findArticleById(id);
            List<Tag> tagList = tagService.selectTagByArticleId(id);
            tagNameList = new ArrayList<>();
            ArticleKind articleKind = articleKindService.selectArticleKindByArticleId(id);
            System.out.println(articleKind);
            if (articleKind != null) {
                Kind kind = kindService.selectKind(articleKind.getKindId());
                article.setKind(kind);
            }
            for (Tag tag :
                    tagList) {
                tagNameList.add(tag.getName());
            }

            map.put("article", article);
        }
        model.addAttribute("article", article);
        model.addAttribute("tag", String.join(";", tagNameList));
        model.addAttribute("kinds", kinds);
        return "/admin/edit";
    }

    @Deprecated
    @RequestMapping(path = "/articles", method = RequestMethod.GET)
    public String getArticles(Model model, Page page, OrderMode orderMode) {
        Comment commentTemplate = new Comment();
        ArticleKind articleKind;
        page.setPath("/articles");
        Map<String, Object> map = new HashMap<>();
        List<Article> articleList = articleService.findArticles(page, null);
        commentTemplate.setStatus(COMMENT_ACTIVE);
        for (Article article : articleList) {
            articleKind = articleKindService.selectArticleKindByArticleId(article.getId());
            if (articleKind != null) {
                article.setKind(kindService.selectKind(articleKind.getKindId()));
            }
//            List<Tag> tags = tagService.selectTagByArticleId(article.getId());

            article.setTags(tagService.selectTagByArticleId(article.getId()));
            commentTemplate.setArticleId(article.getId());

            article.setCommentCount(0
//                    commentService.selectRows(commentTemplate)
            );
        }
        model.addAttribute("articles", articleList);
        return "/admin/articles";
    }

    @RequestMapping(path = "/articles/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateArticle(@RequestBody Map<String, Object> param) {
//        System.out.println(id +" " +title+" " + content+" " + tags+" " + kind+" " + introduce+" " + status);
        Long id = Long.valueOf(param.get("id").toString());
        String title = (String) param.get("title");
        String content = (String) param.get("content");
        String tags = (String) param.get("tags");
        String kind = (String) param.get("kind");
        String introduce = (String) param.get("introduce");
        Integer status = (Integer) param.get("status");
        String img = (String) param.get("img");
            articleService.updateArticle(id, title, content, tags, kind, introduce, status, img);
        return getJsonString(200);
    }

    /**
     * @param id
     * @return com.kyblog.entity.Article
     * @Author kim_yuan
     * @Description 查询某一篇Article
     * @Date 10:40 上午 10/5/21
     **/
    @RequestMapping(path = "/articles/article/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Article getArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticleById(id);
        Long readCount = articleService.findReadCount(id);
        article.setReadCount(readCount);
        return article;
    }


    /**
     * @param status
     * @return int
     * @Author kim_yuan
     * @Description 查询状态为status的Article的行数
     * @Date 10:40 上午 10/5/21
     **/
    @RequestMapping(path = "/articles/rows", method = RequestMethod.GET)
    @ResponseBody
    public int getArticleRows(@RequestParam("status") int status) {
        return articleService.findArticleRows(status);
    }

    /**
     * @param params
     * @return java.util.List<com.kyblog.entity.Article>
     * @Author kim_yuan
     * @Description 分页查询Article列表,
     * 分页查询, 传入key="page", value=Page.class
     * 排序, 传入key="orderMode", value=OrderMode.class
     * @Date 10:39 上午 10/5/21
     **/
    @RequestMapping(path = "/articles/list", method = RequestMethod.POST)
    @ResponseBody
    public List<Article> getArticleList(@RequestBody Map<String, Object> params) {
        Page page = JSON.parseObject(JSON.toJSONString(params.get("page")), Page.class);
        OrderMode orderMode = JSON.parseObject(JSON.toJSONString(params.get("orderMode")), OrderMode.class);
        List<Article> articles = articleService.findArticles(page, orderMode);
        for (Article article :
                articles) {
            article.setReadCount(articleService.findReadCount(article.getId()));
        }
        return articles;
    }

    @RequestMapping(path = "/articles/readCount", method = RequestMethod.PUT)
    public void addArticleReadCount(@RequestBody Map<String, Object> param) {
        String readCount = (String) param.get("readCount");
        String articleId = (String) param.get("articleId");
        Article article = articleService.findArticleById(Long.valueOf(articleId));
        article.setReadCount(Long.valueOf(readCount));
        articleService.updateArticle(article);
        redisOpsUtils.deleteCache(ArticleKey.getByReadCount.getPrefix() + ":" + articleId);
        redisOpsUtils.deleteCache(ArticleKey.getById.getPrefix() + ":" + articleId);
    }

    @RequestMapping(path = "/articles/readCount/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Long getArticleReadCount(@PathVariable("id") String id) {
        Long count;
        Long articleId = Long.valueOf(id);
        if (redisOpsUtils.hasKey(ArticleKey.getByReadCount.getPrefix() + ":" + id)) {
            String temp = redisOpsUtils.get(ArticleKey.getByReadCount.getPrefix() + ":" + articleId).toString();
            count = Long.valueOf(temp);
        } else {
            Article article = articleService.findArticleById(articleId);
            count = article.getReadCount();
            redisOpsUtils.set(ArticleKey.getByReadCount.getPrefix() + ":" + id, count);
        }
        return count;
    }
}
