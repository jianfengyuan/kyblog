package com.kyblog.Service;

import com.kyblog.Dao.ArticleKindDao;
import com.kyblog.Dao.KindDao;
import com.kyblog.entity.Kind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KindService {
    @Autowired
    private ArticleKindDao articleKindDao;
    @Autowired
    private KindDao kindDao;

    public List<Kind> selectKinds(Integer status, Integer orderMode) {
        return kindDao.queryKinds(status, orderMode);
    }

    public int updateKind(Integer id, String name, String introduce, Integer articleCount, Integer status) {
        Kind kind = kindDao.queryKindById(id);
        if (kind == null) {
            return 0;
        }
        if (name != null) {
            kind.setName(name);
        }
        if (introduce != null) {
            kind.setIntroduce(introduce);
        }
        if (articleCount != null) {
            kind.setArticleCount(articleCount);
        }
        if (status != null) {
            kind.setStatus(status);
        }
        return kindDao.updateKind(kind);
    }

    public int insertKind(String name, String introduce) {
        Kind kind = new Kind();
        kind.setName(name);
        if (introduce != null) {
            kind.setIntroduce(introduce);
        }
        return kindDao.insertKind(kind);
    }
}
