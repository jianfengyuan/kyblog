package com.kyblog.comment.Service;

import com.kyblog.comment.Dao.CommentDao;
import com.kyblog.api.entity.Comment;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.utils.kyblogConstant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-25 15:28
 **/
//@Service(
@Service
public class CommentService implements kyblogConstant {
    @Autowired
//    @Qualifier("commentDao")
    private CommentDao commentDao;

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Comment> selectComment(Comment comment, OrderMode orderMode, Page page) {
        comment.setStatus(COMMENT_ACTIVE);
        List<Comment> commentList = commentDao.queryAll(comment, orderMode, page);
        for (Comment item:
                commentList) {
            Query query = new Query(Criteria.where("_id").is(new ObjectId(item.getObjectId())));
            Comment res = mongoTemplate.findOne(query,Comment.class);
            if (res != null) {
                item.setContent(res.getContent());
            }
        }
        return commentList;
    }

    public int insertComment(Comment comment) {
        comment.setTime(new Date());
        comment.setStatus(COMMENT_ACTIVE);
//        comment.setReadStatus(COMMENT_UNREAD);
        comment = mongoTemplate.insert(comment);
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

    public int selectRows(Comment comment) {
        comment.setStatus(COMMENT_ACTIVE);
        return commentDao.queryRows(comment);
    }

    public int updateComment(Comment comment) {
        return commentDao.updateComment(comment);
    }
}