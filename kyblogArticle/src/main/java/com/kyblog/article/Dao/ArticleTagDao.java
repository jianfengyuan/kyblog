package com.kyblog.article.Dao;

import com.kyblog.api.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleTagDao {
    int insertArticleTag(ArticleTag articleTag);

    int updateArticleTag(ArticleTag articleTag);

    @Deprecated
    List<ArticleTag> queryAll();

    List<ArticleTag> queryByArticleId(Long articleId);

    List<ArticleTag> queryByTagId(Long tagId);

    ArticleTag queryByTagIdAndArticleId(@Param("tagId") Long tagId,
                                        @Param("articleId") Long articleId,
                                        @Param("status") Integer status);

    int updateStatus(@Param("id")Long id,@Param("status") Integer status);
}
