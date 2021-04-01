package com.kyblog.Service;

import com.kyblog.Dao.ArticleTagDao;
import com.kyblog.entity.ArticleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagDao articleTagDao;

    List<ArticleTag> selectByArticleId(Long articleId) {
        return articleTagDao.queryByArticleId(articleId);
    }
}
