package com.kyblog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kyblog.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-23 15:38
 **/
@Controller
public class CommentController {

    @RequestMapping(value = "/getComments",method = RequestMethod.POST)
    @ResponseBody
    public String getCommentData(Integer articleId) {
        return getJsonString(200);
    }
}
