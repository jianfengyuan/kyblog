package com.kyblog.Controller;

import com.alibaba.fastjson.JSON;
import com.kyblog.Service.TagService;
import com.kyblog.entity.Kind;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.entity.Tag;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.camel4underline;
import static com.kyblog.utils.BlogUtils.getJsonString;

@Controller
@RequestMapping("/tags")
public class TagController extends BaseController implements kyblogConstant {
    @Deprecated
    @RequestMapping(value = "/tagList", method = RequestMethod.GET)
    public String getTagPage(Model model) {
        int rows = tagService.getRows(null);
        model.addAttribute("rows", rows);
        return "/admin/tags";
    }

    @RequestMapping(value = "/addTag")
    @ResponseBody
    public String addTag(String name) {
        tagService.insertTag(name);
        return getJsonString(200);
    }

    @RequestMapping(value = "/tagData", method = RequestMethod.POST)
    @ResponseBody
    public String getTags(Model model, Page page, OrderMode orderMode) {
        Map<String, Object> map = new HashMap<>();
        page.setPath("/");
        int rows = tagService.getRows(KIND_STATUS_ACTIVE);
        page.setRows(rows);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        List<Tag> tagList = tagService.selectTags(TAG_STATUS_ACTIVE, orderMode, page);
        map.put("tags", tagList);
        map.put("rows", rows);
        return getJsonString(200, null, map);
    }

    @RequestMapping(value = "/deleteTag", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTag(Tag tag) {
        tagService.deleteTag(tag.getId());
        return getJsonString(200);
    }

    @RequestMapping(value = "/updateTag", method = RequestMethod.POST)
    @ResponseBody
    public String updateTag(Tag tag) {
        Tag tempTag;
        tempTag = tagService.selectTagByName(tag.getName(), TAG_STATUS_ACTIVE);
        if (tempTag != null) {
            return getJsonString(503);
        }
        tagService.updateTag(tag);
        return getJsonString(200);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getTags(@RequestParam(value = "articleId") Long articleId) {
        return tagService.selectTagByArticleId(articleId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public List<Tag> getTags(Map<String,Object> params) {
        Integer status = JSON.parseObject(JSON.toJSONString(params.get("status")), Integer.class);
        OrderMode orderMode = JSON.parseObject(JSON.toJSONString(params.get("orderMode")), OrderMode.class);
        Page page = JSON.parseObject(JSON.toJSONString(params.get("page")), Page.class);
        return tagService.selectTags(status, orderMode, page);
    }
}
