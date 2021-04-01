package com.kyblog.Controller;

import com.kyblog.Service.UserService;
import com.kyblog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{account}")
    public User getUserByAccount(@PathVariable("account") String account) {
        return userService.findByAccount(account);
    }
}
