package com.kyblog.Controller;

import com.kyblog.Dao.CommentDao;
import com.kyblog.Service.ArticleService;
import com.kyblog.Service.CommentService;
import com.kyblog.entity.Comment;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-23 15:38
 **/
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/getComments",method = RequestMethod.POST)
    @ResponseBody
    public String getCommentData(Comment comment, Page page, OrderMode orderMode) {
        Map<String, Object> map = new HashMap<>();
        List<Comment> commentList = commentService.selectComment(comment,orderMode, page);
        for (Comment item :
                commentList) {
            item.setArticleTitle(articleService.findArticleById(item.getArticleId()).getTitle());
        }
        map.put("comments", commentList);
        return getJsonString(200,null,map);
    }
}
