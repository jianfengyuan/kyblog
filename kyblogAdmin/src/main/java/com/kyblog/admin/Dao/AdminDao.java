package com.kyblog.admin.Dao;

import com.kyblog.api.entity.Article;
import com.kyblog.api.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminDao {
    Profile findProfileByUsername(@Param("username") String username);
    Profile findProfileByUid(@Param("uid") Integer uid);
    int updateProfile(Profile profile);
}
