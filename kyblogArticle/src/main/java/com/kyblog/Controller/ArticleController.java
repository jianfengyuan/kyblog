package com.kyblog.Controller;

import com.kyblog.Service.ArticleService;
import com.kyblog.Service.TagService;
import com.kyblog.entity.Article;
import com.kyblog.entity.Page;
import com.kyblog.entity.Tag;
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
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;

//    @RequestMapping(path = "/articles/edict", method = RequestMethod.GET)
    public String helloworld() {

        return "/alpha/article";
    }

    @RequestMapping(path = "/articles/publish", method = RequestMethod.POST)
    @ResponseBody
    public String publish(String title, String content, String tags, String kind, String introduce) {
        if (title == null) {
            return getJsonString(503, "title 不能为空");
        }

        articleService.publish(title, content, tags, kind, introduce, 1);
        return getJsonString(200);
    }

    @RequestMapping(path = "/articles/edit", method = RequestMethod.GET)
    public String getArticle(@RequestParam(value = "articleId", required = false) Long id, Model model) {
        Article article = new Article();
        List<String> tagNameList = new ArrayList<>();
        model.addAttribute("article", null);
        System.out.println(id);
        if (id != null) {
            Map<String, Object> map = new HashMap<>();
            article = articleService.findArticleById(id);
            List<Tag> tagList = tagService.selectTagByArticleId(id);
            tagNameList = new ArrayList<>();
            for (Tag tag :
                    tagList) {
                tagNameList.add(tag.getName());
            }

            map.put("article", article);
        }
        model.addAttribute("article", article);
        model.addAttribute("tag", String.join(";", tagNameList));
        return "/admin/edit";
    }

    @RequestMapping(path = "/articles", method = RequestMethod.GET)
//    @ResponseBody
    public String getArticles(Model model, Page page) {
        page.setPath("/articles");
        Map<String, Object> map = new HashMap<>();
        List<Article> articleList = articleService.findArticles(page.getOffset(), page.getLimit(), 2);
        for (Article article: articleList) {
            article.setTags(tagService.selectTagByArticleId(article.getId()));
            article.setCommentCount(0);
        }
        model.addAttribute("articles", articleList);
//        return "200";
        return "/admin/articles";
    }
}
