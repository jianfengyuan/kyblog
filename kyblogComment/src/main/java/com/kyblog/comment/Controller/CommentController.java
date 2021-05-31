package com.kyblog.comment.Controller;

import com.alibaba.fastjson.JSON;
import com.kyblog.comment.Service.CommentService;
import com.kyblog.api.entity.Comment;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.api.utils.BlogUtils.*;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-23 15:38
 **/
@Controller
@RequestMapping("/comments")
public class CommentController implements kyblogConstant {
    @Autowired
//    @Qualifier("commentService")
    private CommentService commentService;

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(Comment comment) {
//        comment.setIp(getIpAddress(request));
//        System.out.println(comment);
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

    /**
     * @Author kim_yuan
     * @Description 查询符合条件的Comment,
     * 条件查询, 传入key="comment", value=Comment.class
     * 分页查询, 传入key="page", value=Page.class
     * 排序, 传入key="orderMode", value=OrderMode.class
     * @Date 10:26 上午 10/5/21
     * @param params
     * @return java.util.List<com.kyblog.entity.Comment>
     **/
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
//    Comment comment, OrderMode orderMode,Page page
    public List<Comment> getComments(@RequestBody Map<String, Object> params) {
        return commentService.selectComment(
                JSON.parseObject(JSON.toJSONString(params.get("comment")), Comment.class),
                JSON.parseObject(JSON.toJSONString(params.get("orderMode")), OrderMode.class),
                JSON.parseObject(JSON.toJSONString(params.get("page")), Page.class));
    }

    /**
     * @Author kim_yuan
     * @Description 查询符合条件的Comment的行数,
     * 条件查询, 传入key="comment", value=Comment.class
     * @Date 10:23 上午 10/5/21
     * @param params
     * @return int
     **/
    @RequestMapping(value = "/rows", method = RequestMethod.POST)
    @ResponseBody
    public int getRows(@RequestBody Map<String, Object> params) {
        return commentService.selectRows(JSON.parseObject(JSON.toJSONString(params.get("comment")), Comment.class));
    }
}
