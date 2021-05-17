package com.kyblog.Controller;

import com.kyblog.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kyblog.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-10 16:53
 **/
@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController implements kyblogConstant {

    @RequestMapping(path = "/articles", method = RequestMethod.GET)
    public String getArticles(Model model) {
        return "admin/articles";
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public String getTags(Model model) {
        return "admin/tags";
    }

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    @ResponseBody
    public String getTags() {
        return getJsonString(200);
    }

    @RequestMapping(value = "/kinds", method = RequestMethod.GET)
    public String getKinds(Model model) {
        return "admin/kinds";
    }

    @RequestMapping(value = "/kinds", method = RequestMethod.POST)
    @ResponseBody
    public String getKinds() {
        return getJsonString(200);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String getComments(Model model) {
        return "admin/comments";
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseBody
    public String getComments() {
        return getJsonString(200);
    }
}
