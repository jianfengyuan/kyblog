package com.kyblog.Controller;

import com.kyblog.Service.ArticleKindService;
import com.kyblog.Service.ArticleService;
import com.kyblog.Service.KindService;
import com.kyblog.Service.TagService;
import com.kyblog.entity.*;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.getJsonString;

@Controller
//@RequestMapping(path = "/articles")
public class ArticleController extends BaseController implements kyblogConstant {

    @RequestMapping(path = "/articles/new", method = RequestMethod.GET)
    public String newPage(Model model) {
        List<Kind> kinds = kindService.selectKinds(KIND_STATUS_ACTIVE, null, null);
        model.addAttribute("kinds", kinds);
        return "/admin/new";
    }

    @RequestMapping(path = "/articles/publish", method = RequestMethod.POST)
    @ResponseBody
    public String publish(String title, String content, String tags, String kind, String introduce, Integer status) {
        if (title == null) {
            return getJsonString(503, "title 不能为空");
        }
        articleService.publish(title, content, tags, kind, introduce, status);
        return getJsonString(200);
    }

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

    @RequestMapping(path = "/articles", method = RequestMethod.GET)
//    @ResponseBody
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
//        return "200";
        return "/admin/articles";
    }

    @RequestMapping(path = "/articles/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateArticle(Long id, String title, String content, String tags,
                                String kind, String introduce, Integer status, String background) {
//        System.out.println(id +" " +title+" " + content+" " + tags+" " + kind+" " + introduce+" " + status);
        articleService.updateArticle(id, title, content, tags, kind, introduce, status, background);
        return getJsonString(200);
    }

    /**
     * @Author kim_yuan
     * @Description 查询某一篇Article
     * @Date 10:40 上午 10/5/21
     * @param id
     * @return com.kyblog.entity.Article
     **/
    @RequestMapping(path = "/articles/article/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Article getArticle(@PathVariable("id") Long id) {
        return articleService.findArticleById(id);
    }


    /**
     * @Author kim_yuan
     * @Description 查询状态为status的Article的行数
     * @Date 10:40 上午 10/5/21
     * @param status
     * @return int
     **/
    @RequestMapping(path = "/articles/rows", method = RequestMethod.GET)
    @ResponseBody
    public int getArticleRows(@RequestParam("status") int status) {
        return articleService.findArticleRows(status);
    }

    /**
     * @Author kim_yuan
     * @Description 分页查询Article列表,
     * 分页查询, 传入key="page", value=Page.class
     * 排序, 传入key="orderMode", value=OrderMode.class
     * @Date 10:39 上午 10/5/21
     * @param params
     * @return java.util.List<com.kyblog.entity.Article>
     **/
    @RequestMapping(path = "/articles/list", method = RequestMethod.POST)
    @ResponseBody
    public List<Article> getArticleList(Map<String,Object> params) {
        Page page;
        OrderMode orderMode;
        page = (Page) params.get("page");
        orderMode = (OrderMode) params.get("orderMode");
        return articleService.findArticles(page,orderMode);
    }
}
