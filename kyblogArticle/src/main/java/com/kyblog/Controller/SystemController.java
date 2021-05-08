package com.kyblog.Controller;

import com.kyblog.entity.*;
import com.kyblog.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-28 23:02
 **/

@Controller
public class SystemController extends BaseController implements kyblogConstant {
    @RequestMapping(value = {"/","/index"},method = RequestMethod.GET)
    public String index(Model model, Page page) {
        OrderMode orderModeTemplate = new OrderMode();
        Page pageTemplate = new Page();
        Comment commentTemplate = new Comment();
        int articleRows = articleService.findArticleRows(ARTICLE_STATUS_ACTIVE);
        page.setRows(articleRows);
        page.setPath("/");
        orderModeTemplate.setColumn("read_count");
        List<Article> articles = articleService.findArticles(page,orderModeTemplate);
        pageTemplate.setLimit(1);
        pageTemplate.setCurrent(1);
        orderModeTemplate.setColumn("time");
        for (Article article :
                articles) {
            article.setTags(tagService.selectTagByArticleId(article.getId()));
            commentTemplate.setArticleId(article.getId());
            if (commentService.selectRows(commentTemplate) > 0) {
                article.setComment(commentService.selectComment(commentTemplate,
                        orderModeTemplate,pageTemplate).get(0));
            }
        }
        pageTemplate.setCurrent(1);
        pageTemplate.setLimit(10);
        orderModeTemplate.setColumn("read_count");
        List<Article> famousArticleList = articleService.findArticles(pageTemplate,orderModeTemplate);
        orderModeTemplate.setColumn("article_count");
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderModeTemplate, null);
        orderModeTemplate.setColumn("article_count");
        List<Tag> tagCloud = tagService.selectTags(TAG_STATUS_ACTIVE, orderModeTemplate, null);
        model.addAttribute("articles", articles);
        model.addAttribute("kinds", kindList);
        model.addAttribute("tags", tagCloud);
        model.addAttribute("famousArticles",famousArticleList);
        model.addAttribute("page", page);
        return "/front/index";
    }

    @RequestMapping(value="/article", method = RequestMethod.GET)
    public String article(@RequestParam(value = "articleId", required = true) Long id,
                          Model model) {
        Comment commentTemplate = new Comment();
        OrderMode orderModeTemplate = new OrderMode();
        Comment replyTemplate = new Comment();
        Page pageTemplate = new Page();
        Article article = articleService.findArticleById(id);
        List<Tag> tagList = tagService.selectTagByArticleId(id);
        ArticleKind articleKind = articleKindService.selectArticleKindByArticleId(id);
        Kind kind = kindService.selectKind(articleKind.getKindId());
        article.setKind(kind);
        article.setTags(tagList);
        pageTemplate.setCurrent(1);
        pageTemplate.setLimit(10);
        orderModeTemplate.setColumn("read_count");
        List<Article> famousArticleList = articleService.findArticles(pageTemplate,orderModeTemplate);
        commentTemplate.setType(COMMENT);
        commentTemplate.setArticleId(id);
        commentTemplate.setStatus(COMMENT_ACTIVE);

        orderModeTemplate.setColumn("article_count");
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderModeTemplate, null);
        orderModeTemplate.setColumn("time");
        replyTemplate.setArticleId(id);
        replyTemplate.setType(REPLY);
        List<Comment> commentList = commentService.selectComment(commentTemplate, orderModeTemplate, null);
        orderModeTemplate.setColumn("article_count");
        List<Tag> tagCloud = tagService.selectTags(TAG_STATUS_ACTIVE, orderModeTemplate, null);
        orderModeTemplate.setColumn("time");
        for (Comment comment :
                commentList) {
            replyTemplate.setReplyId(comment.getId());
            List<Comment> replies = commentService.selectComment(replyTemplate, orderModeTemplate, null);
            comment.setReplies(replies);
        }
        model.addAttribute("famousArticles",famousArticleList);
        model.addAttribute("article", article);
        model.addAttribute("tags", tagCloud);
        model.addAttribute("comments", commentList);
        model.addAttribute("kinds", kindList);
//        System.out.println(model);
        return "/front/article";
    }
}
