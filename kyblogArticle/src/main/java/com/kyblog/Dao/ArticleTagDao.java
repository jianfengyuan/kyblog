package com.kyblog.Dao;

import com.kyblog.entity.Article;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleTagDao {
    int insertArticleTag(ArticleTag articleTag);

    int updateArticleTag(ArticleTag articleTag);

    List<ArticleTag> queryAll();

    List<ArticleTag> queryByArticleId(Long articleId);

    List<ArticleTag> queryByTagId(Long tagId);

    ArticleTag queryByTagIdAndArticleId(@Param("tagId") Long tagId,
                                        @Param("articleId") Long articleId,
                                        @Param("status") Integer status);

    int updateStatus(@Param("id")Long id,@Param("status") Integer status);
}
