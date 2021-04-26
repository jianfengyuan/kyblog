package com.kyblog.Dao;

import com.kyblog.entity.Comment;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    int insertComment(Comment comment);

    int updateComment(Comment comment);

    List<Comment> queryAll(Comment comment, OrderMode orderMode, Page page);

    int queryRows(Comment comment);


}
