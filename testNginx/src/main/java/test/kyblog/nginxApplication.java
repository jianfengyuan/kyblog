package test.kyblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kyblog.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-26 16:25
 **/
@SpringBootApplication
@Controller
public class nginxApplication {
    public static void main(String[] args) {
        SpringApplication.run(nginxApplication.class, args);
    }

//    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/getData")
    @ResponseBody
    public String getData() {
        return getJsonString(200,"从 springBoot 传输信息");
    }
}
