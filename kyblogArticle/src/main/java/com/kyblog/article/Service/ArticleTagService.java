package com.kyblog.article.Service;

import com.kyblog.article.Dao.ArticleTagDao;
import com.kyblog.api.entity.ArticleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagDao articleTagDao;

    public List<ArticleTag> selectByArticleId(Long articleId) {
        return articleTagDao.queryByArticleId(articleId);
    }

    public List<ArticleTag> selectByTagId(Long tagId) {
        return articleTagDao.queryByTagId(tagId);
    }
}
