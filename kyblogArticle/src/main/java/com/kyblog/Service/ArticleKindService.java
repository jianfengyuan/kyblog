package com.kyblog.Service;

import com.kyblog.Dao.ArticleKindDao;
import com.kyblog.entity.ArticleKind;
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

}
