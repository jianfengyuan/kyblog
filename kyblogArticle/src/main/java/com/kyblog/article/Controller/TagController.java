package com.kyblog.article.Controller;

import com.alibaba.fastjson.JSON;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.entity.Tag;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.api.utils.BlogUtils.camel4underline;
import static com.kyblog.api.utils.BlogUtils.getJsonString;

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

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public String addTag(@RequestBody Tag tag) {
        // 如果新增TagName已经存在且正在用, 则不能修改或新增
        Tag tempTag = tagService.selectTagByName(tag.getName(), TAG_STATUS_ACTIVE);
        if (tempTag != null) {
            return getJsonString(503);
        }
        // 新增Tag
        // 如果没有TagId 表示是新增Tag
        if (tag.getId() == null) {
            tagService.insertTag(tag.getName());
            return getJsonString(200);
        }
        // 判断是否有同名且有效的Tag
//        tempTag = tagService.selectTagByName(tag.getName(), TAG_STATUS_ACTIVE);
//        if (tempTag != null) {
//            return getJsonString(503);
//        }
//        tempTag = tagService.selectTagById(tag.getId(), TAG_STATUS_ACTIVE);
        // 否则为更新Tag
        tagService.updateTag(tag);
        return getJsonString(200);
    }

    @Deprecated
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

    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTag(@PathVariable("tagId") String tagId) {

        tagService.deleteTag(Long.parseLong(tagId));
        return getJsonString(200);
    }


//    @RequestMapping(method = RequestMethod.PUT)
//    @ResponseBody
//    @Deprecated
//    public String updateTag(@RequestBody Tag tag) {
//        Tag tempTag = tagService.selectTagById(tag.getId(), null);
////        tempTag = tagService.selectTagByName(tag.getName(), TAG_STATUS_ACTIVE);
//        if (tempTag != null) {
//            return getJsonString(503);
//        }
//        tagService.updateTag(tag);
//        return getJsonString(200);
//    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getTags(@RequestParam(value = "articleId") Long articleId) {
        return tagService.selectTagByArticleId(articleId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public List<Tag> getTags(@RequestBody Map<String, Object> params) {
        Integer status = JSON.parseObject(JSON.toJSONString(params.get("status")), Integer.class);
        OrderMode orderMode = JSON.parseObject(JSON.toJSONString(params.get("orderMode")), OrderMode.class);
        Page page = JSON.parseObject(JSON.toJSONString(params.get("page")), Page.class);
        return tagService.selectTags(status, orderMode, page);
    }

    @RequestMapping(value = "/rows", method = RequestMethod.GET)
    @ResponseBody
    public Integer getRows(@RequestParam Integer status) {
        return tagService.getRows(status);
    }

    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    @ResponseBody
    public Tag getTag(@RequestParam(value = "tagId",required = false) Long tagId) {
        return tagService.selectTagById(tagId, TAG_STATUS_ACTIVE);
    }
}
