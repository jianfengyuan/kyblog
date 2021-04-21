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
public class ArticleController implements kyblogConstant {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleKindService articleKindService;
    @Autowired
    private KindService kindService;

    @RequestMapping(path = "/articles/new", method = RequestMethod.GET)
    public String newPage(Model model) {
        List<Kind> kinds = kindService.selectKinds(KIND_STATUS_ACTIVE, 0,null,null);
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
        List<Kind> kinds = kindService.selectKinds(KIND_STATUS_ACTIVE, 0,null,null);
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
    public String getArticles(Model model, Page page) {
        ArticleKind articleKind;
        page.setPath("/articles");
        Map<String, Object> map = new HashMap<>();
        List<Article> articleList = articleService.findArticles(page.getOffset(), page.getLimit(), 2);
        for (Article article: articleList) {
            articleKind = articleKindService.selectArticleKindByArticleId(article.getId());
            if (articleKind != null) {
                article.setKind(kindService.selectKind(articleKind.getKindId()));
            }
//            List<Tag> tags = tagService.selectTagByArticleId(article.getId());

            article.setTags(tagService.selectTagByArticleId(article.getId()));

            article.setCommentCount(0);
        }
        model.addAttribute("articles", articleList);
//        return "200";
        return "/admin/articles";
    }

    @RequestMapping(path = "/articles/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateArticle(Long id, String title, String content, String tags, String kind, String introduce, Integer status) {
//        System.out.println(id +" " +title+" " + content+" " + tags+" " + kind+" " + introduce+" " + status);
        articleService.updateArticle(id, title, content, tags, kind, introduce, status);
        return getJsonString(200);
    }
}
