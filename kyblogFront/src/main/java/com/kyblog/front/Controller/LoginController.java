package com.kyblog.front.Controller;

import com.kyblog.api.entity.User;
import com.kyblog.front.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping("/user/insert")
    @ResponseBody
    public String insert(@RequestBody User user){
//        System.out.println(user);
        userService.insertUser(user);
        return "创建成功";
    }

}
