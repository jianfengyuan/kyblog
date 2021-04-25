package com.kyblog.Service;

import com.kyblog.Dao.*;
import com.kyblog.entity.*;
import com.kyblog.utils.kyblogConstant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService implements kyblogConstant {
    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleTagDao articleTagDao;

    @Autowired
    private ArticleKindDao articleKindDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    KindDao kindDao;

    @Autowired
    MongoTemplate mongoTemplate;

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
        article = mongoTemplate.insert(article);
        System.out.println(article);
        insertArticle(article);
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


        return 1;
    }

    public int updateArticle(Long articleId, String title, String content, String tags, String kind, String introduce,
                             Integer status) {
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
        article.setTitle(title);
        article.setContent(content);
        article.setStatus(status);
        article.setIntroduce(introduce);
        article.setEdictTime(new Date());
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
        Article article = articleDao.queryById(id,null);
        if (article == null) {
            return null;
        }
        Query query = new Query(Criteria.where("_id").is(new ObjectId(article.getObjectId())));
        Article res = mongoTemplate.findOne(query,Article.class);
        if (res == null) {
            return null;
        }
        article.setContent(res.getContent());
        return article;
    }

    public List<Article> findArticles(Page page, OrderMode orderMode) {
        return articleDao.queryArticles(null, ARTICLE_STATUS_ACTIVE ,page, orderMode);
    }

    public int deleteArticle(Article article) {
        article.setStatus(ARTICLE_STATUS_DELETED);
        return articleDao.updateArticle(article);
    }
}
