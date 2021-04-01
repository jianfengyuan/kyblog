package com.kyblog.Dao;

import com.kyblog.entity.ArticleKind;
import com.kyblog.entity.Kind;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleKindDao {
    List<ArticleKind> queryArticleKinds(Integer kindId, Integer status);

    ArticleKind queryArticleKindByArticleId(Long articleId);

    List<ArticleKind> queryArticleKindByKindId(Integer kindId);

    int updateArticleKind(Kind kind);

    int insertArticleKind(ArticleKind articleKind);
}
