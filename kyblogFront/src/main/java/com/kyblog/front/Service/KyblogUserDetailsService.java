package com.kyblog.front.Service;

import com.kyblog.api.entity.Role;
import com.kyblog.api.entity.User;
import com.kyblog.front.Dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KyblogUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Role> roles = userDao.findRoleByUserName(s);
        user.setRoles(roles);
        return user;
    }
}
