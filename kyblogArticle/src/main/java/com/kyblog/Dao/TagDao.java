package com.kyblog.Dao;

import com.kyblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDao {
    List<Tag> queryAll();

    int insertTag(Tag tag);

    int updateReadCount(Long id);

    int updateStatusById(Long id, Integer status);

    int updateStatusByName(String name, Integer status);

    Tag queryByName(String name);

    List<Tag> queryByArticleId(Long articleId);

    Tag queryById(Long id);
}
