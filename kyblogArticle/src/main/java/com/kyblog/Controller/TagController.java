package com.kyblog.Controller;

import com.kyblog.entity.Kind;
import com.kyblog.entity.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.kyblog.utils.BlogUtils.getJsonString;

@RequestMapping("/tag")
public class TagController {
    @RequestMapping(value = "/add")
    public String add() {
        return getJsonString(200);
    }

    @RequestMapping(value = "/tagList",method = RequestMethod.GET)
    public String getTags() {
        return "/admin/tags";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteKind(Tag tag) {
        return getJsonString(200);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateKind(Tag tag) {
        return getJsonString(200);
    }
}
