package com.kyblog.Dao;

import com.kyblog.entity.Article;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleTagDao {
    int insertArticleTag(ArticleTag articleTag);

    List<ArticleTag> queryAll();

    List<ArticleTag> queryByArticleId(Long articleId);

    List<ArticleTag> queryByTagId(Long tagId);

    ArticleTag queryByTagIdAndArticleId(Long tagId, Long articleId);

    int updateStatus(Long id,Integer status);
}
