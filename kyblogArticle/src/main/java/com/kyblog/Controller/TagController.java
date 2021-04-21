package com.kyblog.Controller;

import com.kyblog.Service.TagService;
import com.kyblog.entity.Kind;
import com.kyblog.entity.Page;
import com.kyblog.entity.Tag;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.kyblog.utils.BlogUtils.getJsonString;
@Controller
@RequestMapping("/tags")
public class TagController implements kyblogConstant {
    @Autowired
    TagService tagService;

    @RequestMapping(value = "/addTag")
    @ResponseBody
    public String addTag(String name) {
        tagService.insertTag(name);
        return getJsonString(200);
    }

    @RequestMapping(value = "/tagList",method = RequestMethod.GET)
    public String getTags(Model model, Page page) {
        page.setPath("/");
        page.setRows(tagService.getRows(null));
//        page.setLimit();
        List<Tag> tagList = tagService.selectTags(TAG_ORDER_BY_ID, TAG_STATUS_ACTIVE,
                page.getOffset(), page.getLimit(), ASC_ORDER);
        model.addAttribute("tags", tagList);
        return "/admin/tags";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTag(Tag tag) {
        return getJsonString(200);
    }

    @RequestMapping(value = "/updateTag", method = RequestMethod.POST)
    @ResponseBody
    public String updateTag(Tag tag) {
        Tag tempTag;
        tempTag = tagService.selectTagByName(tag.getName());
        if (tempTag != null) {
            return getJsonString(503);
        }
        tagService.updateTag(tag);
        return getJsonString(200);
    }
}
