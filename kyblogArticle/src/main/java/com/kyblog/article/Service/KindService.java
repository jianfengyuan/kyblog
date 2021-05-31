package com.kyblog.article.Service;

import com.kyblog.article.Dao.ArticleKindDao;
import com.kyblog.article.Dao.KindDao;
import com.kyblog.api.entity.ArticleKind;
import com.kyblog.api.entity.Kind;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.redisKey.KindKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KindService implements kyblogConstant {
    @Autowired
    private KindDao kindDao;
    @Autowired
    private ArticleKindDao articleKindDao;
    @Autowired
    private RedisOpsUtils redisOpsUtils;

    public List<Kind> selectKinds(Integer status, OrderMode orderMode, Page page) {
        String kindRedisKey = KindKey.getIndex.getPrefix();
        if (orderMode != null) {
            kindRedisKey = kindRedisKey + ":" + orderMode.getColumn() + ":" + orderMode.getDir();
        }
        if (page != null) {
            kindRedisKey = kindRedisKey +":" + + page.getCurrent() +":" + page.getRows();
        }
        List<Kind> kinds = new ArrayList<>();
        if (redisOpsUtils.hasKey(kindRedisKey)) {
            List<Object> list= redisOpsUtils.lGet(kindRedisKey, 0, -1);
            assert list != null;
            for (Object o :
                    list) {
                kinds.add((Kind) o);
            }
        } else {
            kinds = kindDao.queryKinds(status, orderMode, page);
            for (Kind kind :
                    kinds) {
                redisOpsUtils.lSet(kindRedisKey, kind);
            }
        }
        return kinds;
    }

    public int updateKind(Kind kind) {
        redisOpsUtils.deleteCache("KindKey*");
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
        redisOpsUtils.deleteCache("KindKey*");
        return kindDao.updateKind(kind);
    }

    public int insertKind(Kind kind) {
        Kind k = kindDao.queryKindByName(kind.getName());
        redisOpsUtils.deleteCache("KindKey*");
        if (k == null) {
            kind.setStatus(KIND_STATUS_ACTIVE);

            return kindDao.insertKind(kind);
        }
        if (k.getStatus().equals(KIND_STATUS_DELETED)) {
            k.setStatus(KIND_STATUS_ACTIVE);
            k.setIntroduce(kind.getIntroduce());
//            redisOpsUtils.deleteCache("KindKey");
            return kindDao.updateKind(k);
        } else {
            return 0;
        }
    }

    public int getRows(Integer status) {
        return kindDao.queryRows(status);
    }

    public Kind selectKind(Integer kindId) {
        String kindRedisKey = KindKey.getById.getPrefix();
        if (redisOpsUtils.hasKey(kindRedisKey + ":" + kindId)) {
            return (Kind) redisOpsUtils.get(kindRedisKey+":" + kindId);
        }
        else {
                Kind kind = kindDao.queryKindById(kindId);
                redisOpsUtils.set(kindRedisKey +":"+ kindId, kind);
                return kind;
            }
        }

    @Deprecated
    public Kind selectKind(String name) {
        return kindDao.queryKindByName(name);
    }

}
