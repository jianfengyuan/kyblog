package com.kyblog.Controller;

import com.kyblog.Dao.CommentDao;
import com.kyblog.Service.ArticleService;
import com.kyblog.Service.CommentService;
import com.kyblog.entity.Comment;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.utils.kyblogConstant;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.*;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-23 15:38
 **/
@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController implements kyblogConstant {

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(Comment comment) {
        comment.setIp(getIpAddress(request));
        System.out.println(comment);
        commentService.insertComment(comment);
        return getJsonString(200);
    }

    @RequestMapping(value = "/commentData", method = RequestMethod.POST)
    @ResponseBody
    public String getCommentData(Comment comment, Page page, OrderMode orderMode) {
        Map<String, Object> map = new HashMap<>();
        int rows = commentService.selectRows(comment);
        page.setRows(rows);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        List<Comment> commentList = commentService.selectComment(comment, orderMode, page);
        for (Comment item :
                commentList) {
            item.setArticleTitle(articleService.findArticleById(item.getArticleId()).getTitle());
        }
        map.put("comments", commentList);
        map.put("rows", rows);
//        System.out.println(commentList);
        return getJsonString(200, null, map);
    }

    @RequestMapping(value = "/commentsList", method = RequestMethod.GET)
    public String getCommentsPage(Model model) {
        Comment comment = new Comment();
        comment.setStatus(COMMENT_ACTIVE);
        int activeRows = commentService.selectRows(comment);
        comment.setReadStatus(COMMENT_UNREAD);
        int unreadRows = commentService.selectRows(comment);
        model.addAttribute("rows", activeRows);
        model.addAttribute("unRead", unreadRows);
        return "/admin/comments";
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    @ResponseBody
    public String deleteComment(Comment comment) {
        commentService.deleteComment(comment);
        return getJsonString(200);
    }

    @RequestMapping(value = "updateComment", method = RequestMethod.POST)
    @ResponseBody
    public String updateComment(Comment comment) {
        System.out.println(comment);
        commentService.updateComment(comment);
        return getJsonString(200);
    }
}
