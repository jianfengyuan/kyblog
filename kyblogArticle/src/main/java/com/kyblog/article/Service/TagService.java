package com.kyblog.article.Service;

import com.kyblog.article.Dao.ArticleTagDao;
import com.kyblog.article.Dao.TagDao;
import com.kyblog.api.entity.ArticleTag;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.entity.Tag;
import com.kyblog.api.redisKey.TagKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService implements kyblogConstant {
    @Autowired
    TagDao tagDao;

    @Autowired
    ArticleTagDao articleTagDao;

//    @Qualifier("myRedisTemplate")
    @Autowired
    RedisOpsUtils redisOpsUtils;

    public int insertTag(String name) {
//        Tag tag = tagDao.queryByName(name,TAG_STATUS_ACTIVE);
        redisOpsUtils.deleteCache("TagKey*");
//        if (tag != null) {
//            tag.setStatus(TAG_STATUS_ACTIVE);
//            return tagDao.updateTag(tag);
//        } else {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setStatus(TAG_STATUS_ACTIVE);
        return tagDao.insertTag(tag);
//        }
    }

    public int getRows(Integer status) {
        return tagDao.queryRows(status);
    }

    public List<Tag> selectTags(Integer status, OrderMode orderMode, Page page) {
        String redisTagKey = TagKey.getIndex.getPrefix();
        if (orderMode != null) {
            redisTagKey = redisTagKey + ":" + orderMode.getColumn() + ":" + orderMode.getDir();
        }
        if (page != null) {
            redisTagKey = redisTagKey + ":" + page.getCurrent() + ":" + page.getRows();
        }
        List<Tag> tags = new ArrayList<>();
        if (redisOpsUtils.hasKey(redisTagKey)) {
            List<Object> objects = redisOpsUtils.lGet(redisTagKey, 0, -1);
            for (Object o :
                    objects) {
                tags.add((Tag) o);
            }
            return tags;
        } else {
            tags = tagDao.queryTags(status,orderMode,page);
            for (Tag tag :
                    tags) {
                redisOpsUtils.lSet(redisTagKey, tag);
            }
            return tags;
        }
    }

    public int insertArticleTag(ArticleTag articleTag) {
//        redisOpsUtils.deleteCache("TagKey");
        return articleTagDao.insertArticleTag(articleTag);
    }

    public Tag selectTagByName(String name, Integer status) {
        String redisTagKey = TagKey.getByName.getPrefix();
        if (redisOpsUtils.hasKey(redisTagKey + ":" + name)) {
            return (Tag) redisOpsUtils.get(redisTagKey + ":" + name);
        } else {
            Tag tag = tagDao.queryByName(name, status);
            redisOpsUtils.set(redisTagKey + ":" + name, tag);
            return tag;
        }
    }

    public Tag selectTagById(Long id,Integer status) {
        String redisTagKey = TagKey.getById.getPrefix();
        if (redisOpsUtils.hasKey(redisTagKey + ":" + id)) {
            return (Tag) redisOpsUtils.get(redisTagKey + ":" + id);
        } else {
            Tag tag = tagDao.queryById(id, status);
            redisOpsUtils.set(redisTagKey + ":" + id, tag);
            return tag;
        }
    }

    public List<Tag> selectTagByArticleId(Long articleId) {
        List<Tag> tagList = new ArrayList<>();
        List<ArticleTag> articleTagList = articleTagDao.queryByArticleId(articleId);
        Tag tag;
        for (ArticleTag articleTag: articleTagList) {
            tag = tagDao.queryById(articleTag.getTagId(),TAG_STATUS_ACTIVE);
            tagList.add(tag);
        }
        return tagList;
    }

    public int deleteTag(Long tagId) {
        Tag tag = tagDao.queryById(tagId,TAG_STATUS_ACTIVE);
        tag.setStatus(TAG_STATUS_DELETED);
        tag.setArticleCount(0);
        List<ArticleTag> articleTagList = articleTagDao.queryByTagId(tag.getId());
        for (ArticleTag at:
             articleTagList) {
            if (at.getStatus() == ARTICLE_TAG_STATUS_ACTIVE) {
                at.setStatus(ARTICLE_TAG_STATUS_DELETED);
                articleTagDao.updateArticleTag(at);
            }
        }
        redisOpsUtils.deleteCache("TagKey*");
        return tagDao.updateTag(tag);
    }

    public int updateTag(Tag tag) {
        redisOpsUtils.deleteCache("TagKey*");
        return tagDao.updateTag(tag);
    }
}
