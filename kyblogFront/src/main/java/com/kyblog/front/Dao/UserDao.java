package com.kyblog.front.Dao;

import com.kyblog.api.entity.Role;
import com.kyblog.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Mapper
@Repository
public interface UserDao {
    User findUserByUsername(String username);

    int insertUser(User user);

    List<Role> findRoleByUserName(String username);
}
