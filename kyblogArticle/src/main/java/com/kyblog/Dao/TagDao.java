package com.kyblog.Dao;

import com.kyblog.entity.Article;
import com.kyblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDao {
    List<Tag> queryTags(@Param("mode") Integer mode, @Param("status") Integer status,
                        @Param("offset") Integer offset, @Param("limit") Integer limit,
                        @Param("desc") Integer desc);

    int insertTag(Tag tag);

    int updateTag(Tag tag);

    int updateReadCount(Long id);

    int updateStatusById(Long id, Integer status);

    int updateStatusByName(String name, Integer status);

    Tag queryByName(@Param("name") String name, @Param("status") Integer status);

    List<Tag> queryByArticleId(Long articleId);

    Tag queryById(Long id);

    int queryRows(@Param("status") Integer status);
}
