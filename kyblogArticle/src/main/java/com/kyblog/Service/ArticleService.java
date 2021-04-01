package com.kyblog.Service;

import com.kyblog.Dao.ArticleDao;
import com.kyblog.Dao.ArticleTagDao;
import com.kyblog.Dao.TagDao;
import com.kyblog.entity.Article;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.Tag;
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
    TagDao tagDao;

    @Autowired
    MongoTemplate mongoTemplate;

    public Article findById(Long id) {
        return articleDao.queryById(id);
    }

    public int insertArticle(Article article) {
        return articleDao.insertArticle(article);
    }

    public int publish(String title, String content, String tags, String kind, String introduce, int status) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setReadCount(0L);
        if ("".equals(introduce)) {
            // 摘要为空
            introduce = content.length() > 100 ? content.substring(0, 100) : content;
        }
        article.setIntroduce(introduce);
        article.setStatus(status);
//        if (kind != null) {
//            article.setKind(kind);
//        }
        article.setPublishTime(new Date());
        article.setEdictTime(new Date());
        article = mongoTemplate.insert(article);
        System.out.println(article);
        insertArticle(article);
//        System.out.println(article);
        List<Tag> tagsList = processTags(tags);
        for (Tag tag:
                tagsList) {
            Tag temp = tagDao.queryByName(tag.getName());
            if (temp == null) {
                tag.setArticleCount(1);
                tagDao.insertTag(tag);
                System.out.println(tag.getId());
            }else{
                tag = temp;
                tag.setArticleCount(tag.getArticleCount()+1);
                tagDao.updateReadCount(tag.getId());
            }
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tag.getId());
            articleTag.setArticleId(article.getId());
            articleTag.setStatus(1);
            articleTagDao.insertArticleTag(articleTag);
        }


        return 1;
    }

    public int updateArticle(Long articleId, String title, String content, String tags, String kind, String introduce) {
        Article article = articleDao.queryById(articleId);
        List<Tag> newTagList = processTags(tags);
        List<Tag> oldTagList = tagDao.queryByArticleId(articleId);
        Set<Long> articleTagSet = new HashSet<>();
        for (Tag tag:
                newTagList) {
            articleTagSet.add(tag.getId());
        }
        article.setTitle(title);
        article.setContent(content);
//        article.setKind(kind);
        article.setIntroduce(introduce);
        article.setEdictTime(new Date());
        articleDao.updateArticle(article);
        for (Tag tag:
                newTagList) {
            if (tagDao.queryByName(tag.getName())==null) {
                tagDao.insertTag(tag);
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tag.getId());
                articleTag.setStatus(1);
                articleTagDao.insertArticleTag(articleTag);
            }
        }
        for (Tag tag:
                oldTagList) {
            if (!articleTagSet.contains(tag.getId())) {
                ArticleTag articleTag = articleTagDao.queryByTagIdAndArticleId(tag.getId(), articleId);
                articleTagDao.updateStatus(articleTag.getId(), 0);
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
                temp.setStatus(1);
                tagList.add(temp);
            }
        }
        return tagList;
    }

    public Article findArticleById(Long id) {
        Article article = articleDao.queryById(id);
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

    public List<Article> findArticles(int offset, int limit, int orderMode) {
        return articleDao.queryArticles(null, 1 ,offset,limit, orderMode);
    }

    public int deleteArticle(Long id) {
        return articleDao.deleteById(id);
    }
}
