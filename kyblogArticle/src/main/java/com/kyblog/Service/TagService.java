package com.kyblog.Service;

import com.kyblog.Dao.ArticleDao;
import com.kyblog.Dao.ArticleTagDao;
import com.kyblog.Dao.TagDao;
import com.kyblog.entity.ArticleTag;
import com.kyblog.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagDao tagDao;

    @Autowired
    ArticleTagDao articleTagDao;

    public int insertTag(Tag tag) {
        return tagDao.insertTag(tag);
    }

    public int insertArticleTag(ArticleTag articleTag) {
        return articleTagDao.insertArticleTag(articleTag);
    }

    public Tag selectTagByName(String name) {
        return tagDao.queryByName(name);
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


}
