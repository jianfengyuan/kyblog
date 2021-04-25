package com.kyblog.Dao;

import com.kyblog.entity.Article;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
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


}
