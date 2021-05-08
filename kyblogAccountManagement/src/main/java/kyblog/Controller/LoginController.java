package kyblog.Controller;

import com.kyblog.Service.UserService;
import com.kyblog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(HttpServletResponse response, String account, String password) {
        Map<String, Object> map = userService.login(account, password);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            response.addCookie(cookie);
            return "登录成功";
        } else {
            return "登录失败: " + map.get("errorMsg");
        }

    }

    @PostMapping("/register")
    public String register(User user) {
        Map<String, Object> map = userService.register(user);
        if (map.isEmpty()) {
            return "注册成功";
        } else {
            return "注册失败: " + map.get("errorMsg");
        }

    }
}
