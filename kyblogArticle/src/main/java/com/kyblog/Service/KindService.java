package com.kyblog.Service;

import com.kyblog.Dao.ArticleKindDao;
import com.kyblog.Dao.KindDao;
import com.kyblog.entity.ArticleKind;
import com.kyblog.entity.Kind;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KindService implements kyblogConstant {
    @Autowired
    private KindDao kindDao;
    @Autowired
    private ArticleKindDao articleKindDao;

    public List<Kind> selectKinds(Integer status, OrderMode orderMode, Page page) {
        return kindDao.queryKinds(status, orderMode, page);
    }

    public int updateKind(Kind kind) {
        return kindDao.updateKind(kind);
    }

    /**
     * 删除类别
     * 类别被删除后, 对应统计的文章数量清零
     * 文章原本所设置的类别变成默认"-"
    * */
    public int deleteKind(Kind kind) {
        kind.setStatus(KIND_STATUS_DELETED);
        kind.setArticleCount(0);
        Kind defaultKind = kindDao.queryKindByName("-");
        List<ArticleKind> articleKinds =
                articleKindDao.queryArticleKindByKindId(kind.getId(), ARTICLE_KIND_STATUS_ACTIVE);
        ArticleKind defaultAk;
        for (ArticleKind ak:
             articleKinds) {
            ak.setStatus(ARTICLE_KIND_STATUS_DELETED);
            articleKindDao.updateArticleKind(ak);
            defaultAk = articleKindDao.queryArticleKindByArticleIdAndKindID(ak.getArticleId(),
                    defaultKind.getId(), null);
            if (defaultAk != null) {
                defaultAk.setStatus(ARTICLE_KIND_STATUS_ACTIVE);
                articleKindDao.updateArticleKind(defaultAk);
            } else {
                ArticleKind newAk = new ArticleKind();
                newAk.setStatus(ARTICLE_KIND_STATUS_ACTIVE);
                newAk.setKindId(defaultKind.getId());
                newAk.setArticleId(ak.getArticleId());
                articleKindDao.insertArticleKind(newAk);
            }
            defaultKind.setArticleCount(defaultKind.getArticleCount()+1);
            kindDao.updateKind(defaultKind);
        }
        return kindDao.updateKind(kind);
    }

    public int insertKind(Kind kind) {
        Kind k = kindDao.queryKindByName(kind.getName());
        if (k == null) {
            kind.setStatus(KIND_STATUS_ACTIVE);
            return kindDao.insertKind(kind);
        }
        if (k.getStatus().equals(KIND_STATUS_DELETED)) {
            k.setStatus(KIND_STATUS_ACTIVE);
            k.setIntroduce(kind.getIntroduce());
            return kindDao.updateKind(k);
        } else {
            return 0;
        }
    }

    public int getRows(Integer status) {
        return kindDao.queryRows(status);
    }

    public Kind selectKind(Integer kindId) {
        return kindDao.queryKindById(kindId);
    }
    public Kind selectKind(String name) {
        return kindDao.queryKindByName(name);
    }
}
