package com.kyblog.article.Service;

import com.kyblog.api.elasticsearch.ArticleRepository;
import com.kyblog.api.entity.*;
import com.kyblog.api.redisKey.ArticleKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.article.Dao.*;
import com.kyblog.api.utils.kyblogConstant;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ArticleService implements kyblogConstant {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleTagDao articleTagDao;

    @Autowired
    ArticleKindDao articleKindDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    KindDao kindDao;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RedisOpsUtils redisOpsUtils;

    @Autowired
    ElasticSearchService elasticSearchService;

    @Deprecated
    public Article findById(Long id, Integer status) {
        return articleDao.queryById(id, status);
    }

    public int insertArticle(Article article) {
        return articleDao.insertArticle(article);
    }

    public int publish(String title, String content, String tags, String kind, String introduce, int status) {
        Article article = new Article();
        ArticleKind articleKind;
        article.setTitle(title);
        article.setContent(content);
        article.setReadCount(0L);
        if ("".equals(introduce)) {
            // 摘要为空
            introduce = content.length() > 100 ? content.substring(0, 100) : content;
        }
        article.setIntroduce(introduce);
        article.setStatus(status);

        article.setPublishTime(new Date());
        article.setEdictTime(new Date());
        insertArticle(article);
        System.out.println(article);
        article = mongoTemplate.insert(article);


        if (kind != null) {
            articleKind = new ArticleKind();
            Kind k = kindDao.queryKindByName(kind);
            articleKind.setKindId(k.getId());
            articleKind.setArticleId(article.getId());
            articleKindDao.insertArticleKind(articleKind);
        }
        List<Tag> tagsList = processTags(tags);
        for (Tag tag:
                tagsList) {
            Tag temp = tagDao.queryByName(tag.getName(),null);
            if (temp == null) {
                tag.setArticleCount(1);
                tag.setStatus(TAG_STATUS_ACTIVE);
                tagDao.insertTag(tag);
            }else{
                tag = temp;
                tag.setStatus(TAG_STATUS_ACTIVE);
                tag.setArticleCount(tag.getArticleCount()+1);
                tagDao.updateTag(tag);
//                tagDao.updateReadCount(tag.getId());
            }
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tag.getId());
            articleTag.setArticleId(article.getId());
            articleTag.setStatus(1);
            articleTagDao.insertArticleTag(articleTag);
        }
        redisOpsUtils.deleteCache("ArticleKey*");
        redisOpsUtils.deleteCache("TagKey*");
        redisOpsUtils.deleteCache("KindKey*");

        return 1;
    }

    public int updateArticle(Article article) {
        return articleDao.updateArticle(article);
    }

    public int updateArticle(Long articleId, String title, String content,
                             String tags, String kind, String introduce,
                             Integer status, String background) {
        Article article = articleDao.queryById(articleId,null);
        List<Tag> newTagList = processTags(tags);
        List<Tag> oldTagList = tagDao.queryByArticleId(articleId);
        Set<Long> articleTagSet = new HashSet<>();
        Kind k;
        ArticleKind ak;
        for (Tag tag:
                newTagList) {
            articleTagSet.add(tag.getId());
        }
        // 先把原本的文章种类查询出来
        // 先删再加新的种类, 分类有可能相同, 有可能不同
        ak = articleKindDao.queryArticleKindByArticleId(article.getId());
        if (ak != null) {
            k = kindDao.queryKindById(ak.getKindId());
            ak.setStatus(KIND_STATUS_DELETED);
            articleKindDao.updateArticleKind(ak);
            k.setArticleCount(k.getArticleCount() - 1);
            kindDao.updateKind(k);
        }
        if (kind != null) {
            k = kindDao.queryKindByName(kind);
            k.setArticleCount(k.getArticleCount()+1);
            kindDao.updateKind(k);
            System.out.println(k);
            ak = articleKindDao.queryArticleKindByArticleIdAndKindID(
                    article.getId(), k.getId(),ARTICLE_KIND_STATUS_DELETED);
            if (ak != null) {
                ak.setStatus(ARTICLE_KIND_STATUS_ACTIVE);
                articleKindDao.updateArticleKind(ak);
            } else {
                ArticleKind articleKind = new ArticleKind();
                articleKind.setStatus(ARTICLE_KIND_STATUS_ACTIVE);
                articleKind.setArticleId(article.getId());
                articleKind.setKindId(k.getId());
                articleKindDao.insertArticleKind(articleKind);
            }

        }
        if (background != null) {
            article.setBackground(background);
        }
        article.setTitle(title);
        article.setContent(content);
        article.setStatus(status);
        article.setIntroduce(introduce);
        article.setEdictTime(new Date());
        Update update = new Update().set("title", article.getTitle())
                .set("content", article.getContent())
                .set("introduce", article.getIntroduce())
                .set("status", article.getStatus())
                .set("background", article.getBackground());
        Query query = new Query(Criteria.where("id").is(article.getId()));
        UpdateResult res = mongoTemplate.updateFirst(query, update, Article.class);
        if (res.getModifiedCount() == 0) {
            throw new RuntimeException("更新文章失败");
        }
        articleDao.updateArticle(article);
        for (Tag tag:
                oldTagList) {
            ArticleTag articleTag = articleTagDao.queryByTagIdAndArticleId(tag.getId(), articleId, ARTICLE_TAG_STATUS_ACTIVE);
            tag.setArticleCount(tag.getArticleCount()-1);
            articleTag.setStatus(ARTICLE_TAG_STATUS_DELETED);
            articleTagDao.updateArticleTag(articleTag);
            tagDao.updateTag(tag);
        }

        for (Tag tag:
                newTagList) {
            Tag tempTag = tagDao.queryByName(tag.getName(),null);
            if (tempTag == null) {
                tag.setArticleCount(tag.getArticleCount()+1);
                tagDao.insertTag(tag);
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tag.getId());
                articleTag.setStatus(ARTICLE_TAG_STATUS_ACTIVE);
                articleTagDao.insertArticleTag(articleTag);
            } else {
                ArticleTag articleTag = articleTagDao.queryByTagIdAndArticleId(tempTag.getId(), articleId, null);
                if (articleTag == null) {
                    articleTag = new ArticleTag();
                    articleTag.setStatus(ARTICLE_TAG_STATUS_ACTIVE);
                    articleTag.setArticleId(articleId);
                    articleTag.setTagId(tempTag.getId());
                    articleTagDao.insertArticleTag(articleTag);
                    tempTag.setStatus(TAG_STATUS_ACTIVE);
                    tempTag.setArticleCount(tempTag.getArticleCount()+1);
                    tagDao.updateTag(tempTag);
                } else if (articleTag.getStatus().equals(ARTICLE_TAG_STATUS_DELETED)) {
                    articleTag.setStatus(ARTICLE_TAG_STATUS_ACTIVE);
                    tempTag.setArticleCount(tempTag.getArticleCount() + 1);
                    articleTagDao.updateArticleTag(articleTag);
                    tempTag.setStatus(TAG_STATUS_ACTIVE);
                    tagDao.updateTag(tempTag);
                }
            }
        }
        redisOpsUtils.deleteCache("ArticleKey*");
        redisOpsUtils.deleteCache("KindKey*");
        redisOpsUtils.deleteCache("TagKey*");
        return 1;
    }

    public List<Tag> processTags(String tags) {
        if (tags == null) {
            return null;
        }
        List<Tag> tagList = new ArrayList<>();
        String[] tagArray = tags.split(";");
        for (String tag:
             tagArray) {
            if(!"".equals(tag)) {
                Tag temp = new Tag();
                temp.setName(tag);
                temp.setStatus(TAG_STATUS_ACTIVE);
                temp.setArticleCount(0);
                tagList.add(temp);
            }
        }
        return tagList;
    }

    public Article findArticleById(Long id) {
        if (redisOpsUtils.hasKey(ArticleKey.getById.getPrefix() + ":" + id)) {
            return (Article) redisOpsUtils.get(ArticleKey.getById.getPrefix() + ":" + id);
        } else {
            Article article = articleDao.queryById(id,null);
            if (article == null) {
                return null;
            }
            Query query = new Query(Criteria.where("id").is(article.getId()));
            Article res = mongoTemplate.findOne(query,Article.class);
            if (res == null) {
                return null;
            }
            article.setContent(res.getContent());
//            redisOpsUtils.set(ArticleKey.getByReadCount.getPrefix() + ":" + article.getId(), article.getReadCount());
            redisOpsUtils.set(ArticleKey.getById.getPrefix()+":" + article.getId(), article);
            return article;
        }
    }

    public List<Article> findArticles(Page page, OrderMode orderMode) {

        List<Article> articles = articleDao.queryArticles(null, ARTICLE_STATUS_ACTIVE, page, orderMode);
        return articles;
    }

    public int deleteArticle(Article article) {
        article.setStatus(ARTICLE_STATUS_DELETED);
        return articleDao.updateArticle(article);
    }

    public int findArticleRows(Integer status) {
        return articleDao.queryRows(status);
    }

    public List<Article> findArticlesByTagId(Long tagId) {
        List<ArticleTag> articleTags = articleTagDao.queryByTagId(tagId);
        List<Article> articles = new ArrayList<>();
        for (ArticleTag at :
                articleTags) {
            Article article = findArticleById(at.getArticleId());
            articles.add(article);
        }
        return articles;
    }

    public List<Article> findArticlesByKindId(Integer kindId) {
        List<ArticleKind> articleKinds = articleKindDao.queryArticleKindByKindId(kindId, KIND_STATUS_ACTIVE);
        List<Article> articles = new ArrayList<>();
        for (ArticleKind ak :
                articleKinds) {
            Article article = findArticleById(ak.getArticleId());
            articles.add(article);
        }
        return articles;
    }

    public Long findReadCount(Long articleId) {
        if (redisOpsUtils.hasKey(ArticleKey.getByReadCount.getPrefix() + ":" + articleId)) {
            return Long.valueOf(redisOpsUtils.get(ArticleKey.getByReadCount.getPrefix() + ":" + articleId).toString());
        }
        Article article = articleDao.queryById(articleId, ARTICLE_STATUS_ACTIVE);
        return article.getReadCount();
    }
}
