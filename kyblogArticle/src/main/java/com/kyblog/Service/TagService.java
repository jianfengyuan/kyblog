package com.kyblog.Service;

import com.kyblog.Dao.ArticleDao;
import com.kyblog.Dao.ArticleTagDao;
import com.kyblog.Dao.TagDao;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.Tag;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService implements kyblogConstant {
    @Autowired
    TagDao tagDao;

    @Autowired
    ArticleTagDao articleTagDao;

    public int insertTag(String name) {
        Tag tag = tagDao.queryByName(name,null);
        if (tag != null) {
            tag.setStatus(TAG_STATUS_ACTIVE);
            return tagDao.updateTag(tag);
        } else {
            tag = new Tag();
            tag.setName(name);
            tag.setStatus(TAG_STATUS_ACTIVE);
            return tagDao.insertTag(tag);
        }
    }

    public int getRows(Integer status) {
        return tagDao.queryRows(status);
    }

    public List<Tag> selectTags(Integer mode, Integer status,
                                Integer offset, Integer limit, Integer desc) {
        return tagDao.queryTags(mode, status, offset, limit, desc);
    }

    public int insertArticleTag(ArticleTag articleTag) {
        return articleTagDao.insertArticleTag(articleTag);
    }

    public Tag selectTagByName(String name, Integer status) {
        return tagDao.queryByName(name, status);
    }

    public Tag selectTagById(Long id) {
        return tagDao.queryById(id);
    }

    public List<Tag> selectTagByArticleId(Long articleId) {
        List<Tag> tagList = new ArrayList<>();
        List<ArticleTag> articleTagList = articleTagDao.queryByArticleId(articleId);
        Tag tag;
        for (ArticleTag articleTag: articleTagList) {
            tag = tagDao.queryById(articleTag.getTagId());
            tagList.add(tag);
        }
        return tagList;
    }

    public int deleteTag(Long tagId) {
        Tag tag = tagDao.queryById(tagId);
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
        return tagDao.updateTag(tag);
    }

    public int updateTag(Tag tag) {
        return tagDao.updateTag(tag);
    }
}
