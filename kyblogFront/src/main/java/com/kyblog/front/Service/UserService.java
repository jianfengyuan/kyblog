package com.kyblog.front.Service;

import com.kyblog.api.entity.User;
import com.kyblog.front.Dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;
    public int insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.insertUser(user);
    }
}
