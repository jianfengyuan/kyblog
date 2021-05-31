package com.kyblog.article.Dao;

import com.kyblog.api.entity.Article;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleDao {
    int insertArticle(Article article);

    int deleteById(Long id);

    Article queryById(@Param("id") Long id, @Param("status") Integer status);

    int updateArticle(Article article);

    List<Article> queryByTagId(Long tagId);

    List<Article> queryArticles(List<Long> ids, @Param("status") int status, @Param("page") Page page, @Param("orderMode") OrderMode orderMode);

    int queryRows(@Param("status")int Status);
}
