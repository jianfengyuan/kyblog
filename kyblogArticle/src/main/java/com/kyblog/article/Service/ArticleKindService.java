package com.kyblog.article.Service;

import com.kyblog.article.Dao.ArticleKindDao;
import com.kyblog.api.entity.ArticleKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleKindService {
    @Autowired
    private ArticleKindDao articleKindDao;

    public List<ArticleKind> selectArticleKinds(Integer kindId, Integer status) {
        return articleKindDao.queryArticleKinds(kindId, status);
    }

    public ArticleKind selectArticleKindByArticleId(Long articleId) {
        ArticleKind articleKind = articleKindDao.queryArticleKindByArticleId(articleId);
        return articleKind;
    }



}
