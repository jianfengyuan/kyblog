package com.kyblog.comment.Service;

import com.kyblog.api.redisKey.CommentKey;
import com.kyblog.api.utils.RedisOpsUtils;
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

import java.util.ArrayList;
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

    @Autowired
    RedisOpsUtils redisOpsUtils;

    public List<Comment> selectComment(Comment comment, OrderMode orderMode, Page page) {
        comment.setStatus(COMMENT_ACTIVE);
        if (comment.getArticleId() != null) {
            String commentKey = CommentKey.getByArticleId.getPrefix();
            if (redisOpsUtils.hasKey(commentKey + ":" + comment.getType() + ":" + comment.getArticleId())) {
                List<Comment> commentList = new ArrayList<>();
                List<Object> objects = redisOpsUtils.lGet(commentKey + ":" + comment.getType() + ":" + comment.getArticleId(), 0, -1);
                for (Object o :
                        objects) {
                    commentList.add((Comment) o);
                }
                return commentList;
            } else {
                List<Comment> commentList = commentDao.queryAll(comment, orderMode, page);
                for (Comment item :
                        commentList) {
                    Query query = new Query(Criteria.where("id").is(item.getId()));
                    Comment res = mongoTemplate.findOne(query, Comment.class);
                    if (res != null) {
                        item.setContent(res.getContent());
                        redisOpsUtils.lSet(commentKey + ":" + item.getType() + ":" + comment.getArticleId(), item);
                    }
                }
                return commentList;
            }
        } else {
            String commentKey = CommentKey.getByStatus.getPrefix()+ ":" + comment.getStatus();
            if (redisOpsUtils.hasKey(commentKey)) {
                List<Comment> commentList = new ArrayList<>();
                List<Object> objects = redisOpsUtils.lGet(commentKey, 0, -1);
                for (Object o :
                        objects) {
                    commentList.add((Comment) o);
                }
                return commentList;

            } else {
                List<Comment> commentList = commentDao.queryAll(comment, orderMode, page);
                for (Comment item :
                        commentList) {
                    Query query = new Query(Criteria.where("id").is(item.getId()));
                    Comment res = mongoTemplate.findOne(query, Comment.class);
                    if (res != null) {
                        item.setContent(res.getContent());
                        redisOpsUtils.lSet(commentKey, item);
                    }
                }
                return commentList;
            }
        }
    }

    public void insertComment(Comment comment) {
        comment.setTime(new Date());
        comment.setStatus(COMMENT_ACTIVE);
//        comment.setReadStatus(COMMENT_UNREAD);
        commentDao.insertComment(comment);
        mongoTemplate.insert(comment);
        redisOpsUtils.deleteCache("Comment*");
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
        redisOpsUtils.deleteCache("CommentKey*");
        return commentDao.updateComment(comment);
    }

    public int selectRows(Comment comment) {
//        comment.setStatus(COMMENT_ACTIVE);
        int rows = commentDao.queryRows(comment);
        return rows;
    }

    public int updateComment(Comment comment) {
        redisOpsUtils.deleteCache("Comment*");
        return commentDao.updateComment(comment);
    }
}