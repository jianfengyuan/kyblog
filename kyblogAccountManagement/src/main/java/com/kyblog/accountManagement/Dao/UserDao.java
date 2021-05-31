package com.kyblog.accountManagement.Dao;

import com.kyblog.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    User selectByAccount(String account);

    int insertUser(User user);

    int updatePassword(Long uid, String password);

}
