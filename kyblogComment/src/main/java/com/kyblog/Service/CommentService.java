package com.kyblog.Service;

import com.kyblog.Dao.CommentDao;
import com.kyblog.entity.Comment;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-25 15:28
 **/
@Service
public class CommentService implements kyblogConstant {
    @Autowired
    private CommentDao commentDao;

    public List<Comment> selectComment(Comment comment, OrderMode orderMode, Page page) {
        return commentDao.queryAll(comment, orderMode, page);
    }

    public int insertComment(Comment comment) {
        return commentDao.insertComment(comment);
    }


    public int deleteComment(Comment comment) {
        if (comment.getType().equals(COMMENT)) {
            Comment replyTemplate = new Comment();
            replyTemplate.setReplyId(comment.getId());
            replyTemplate.setArticleId(comment.getArticleId());
            List<Comment> replyList = commentDao.queryAll(replyTemplate, null, null);
            for (Comment reply:
                 replyList) {
                reply.setStatus(COMMENT_DELETED);
                commentDao.updateComment(reply);
            }
        }
        comment.setStatus(COMMENT_DELETED);
        return commentDao.updateComment(comment);
    }

    public int selectRows() {
        return 
    }
}
