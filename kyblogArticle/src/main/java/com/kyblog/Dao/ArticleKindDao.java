package com.kyblog.Dao;

import com.kyblog.entity.ArticleKind;
import com.kyblog.entity.Kind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleKindDao {
    List<ArticleKind> queryArticleKinds(Integer kindId, Integer status);

    ArticleKind queryArticleKindByArticleId(Long articleId);

    ArticleKind queryArticleKindByArticleIdAndKindID(@Param("articleId") Long articleId,
                                                     @Param("kindId")Integer kindId, @Param("status")Integer status);

    List<ArticleKind> queryArticleKindByKindId(@Param("kindId")Integer kindId, @Param("status")Integer status);

    int updateArticleKind(ArticleKind articleKind);

    int insertArticleKind(ArticleKind articleKind);
}
