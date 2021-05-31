package com.kyblog.article.Dao;

import com.kyblog.api.entity.Comment;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    int insertComment(Comment comment);

    int updateComment(Comment comment);

    List<Comment> queryAll( @Param("comment") Comment comment, @Param("orderMode") OrderMode orderMode, @Param("page") Page page);

    int queryRows(@Param("comment") Comment comment);


}
