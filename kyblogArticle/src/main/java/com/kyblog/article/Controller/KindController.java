package com.kyblog.article.Controller;

import com.alibaba.fastjson.JSON;
import com.kyblog.api.entity.ArticleKind;
import com.kyblog.api.entity.Kind;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.kyblog.api.utils.BlogUtils.camel4underline;
import static com.kyblog.api.utils.BlogUtils.getJsonString;

@Controller
@RequestMapping(path = "/kinds")
public class KindController extends BaseController implements kyblogConstant {

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public String add(@RequestBody Kind kind) {
        if (kind.getId() == null) {
//            Set<String> keys = redisOpsUtils.getKeys("KindKey");
//            for (String key :
//                    keys) {
//                redisOpsUtils.del(key);
//            }
            return kindService.insertKind(kind) == 1 ? getJsonString(200) : getJsonString(501);
        } else {
            if (kind.getName().equals("-")) {
                return getJsonString(501);
            }
            kindService.updateKind(kind);
//            Set<String> keys = redisOpsUtils.getKeys("KindKey");
//            for (String key :
//                    keys) {
//                redisOpsUtils.del(key);
//            }
            return getJsonString(200);
        }
    }

    @RequestMapping(value = "/{kindId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable("kindId") Integer kindId) {
        Kind kind = kindService.selectKind(kindId);
        if ("-".equals(kind.getName())) {
            return getJsonString(501);
        }
//        Set<String> keys = redisOpsUtils.getKeys("KindKey");
//        for (String key :
//                keys) {
//            redisOpsUtils.del(key);
//        }
        return kindService.deleteKind(kind) == 1?getJsonString(200):getJsonString(501);

    }

//    @RequestMapping(value = "/kindData", method = RequestMethod.POST)
//    @ResponseBody
    @Deprecated
    public String getKindsData(Page page, Model model, OrderMode orderMode) {
        Map<String, Object> map = new HashMap<>();
        page.setPath("/");
        System.out.println(orderMode);
        orderMode.setColumn(camel4underline(orderMode.getColumn()));
        int rows = kindService.getRows(KIND_STATUS_ACTIVE);
        page.setRows(rows);
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderMode, page);
        map.put("kinds", kindList);
        map.put("rows", rows);
        return getJsonString(200, null, map);
    }
    @Deprecated
//    @RequestMapping(value = "/kindList", method = RequestMethod.GET)
    public String getKindPage(Model model) {
        int rows = kindService.getRows(KIND_STATUS_ACTIVE);
        model.addAttribute("rows", rows);
        return "/admin/kinds";
    }

    //    @RequestMapping(value = "/kindList", method = RequestMethod.GET)
    @Deprecated
    public String getKinds(OrderMode orderMode, Page page, Model model) {
        page.setPath("/");
        page.setRows(kindService.getRows(KIND_STATUS_ACTIVE));
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderMode, page);
        System.out.println(kindList);
        model.addAttribute("kinds", kindList);
        return "/admin/kinds";
    }

//    @RequestMapping(value = "/deleteKind", method = RequestMethod.POST)
//    @ResponseBody
    @Deprecated
    public String deleteKind(Kind kind) {
        if (kind.getName().equals("-")) {
            return getJsonString(501);
        }
        kindService.deleteKind(kind);
        return getJsonString(200);
    }

//    @RequestMapping(value = "/updateKind", method = RequestMethod.POST)
//    @ResponseBody
    @Deprecated
    public String updateKind(Kind kind) {
        if (kind.getName().equals("-")) {
            return getJsonString(501);
        }
        kindService.updateKind(kind);

        return getJsonString(200);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public List<Kind> getKindList(@RequestBody Map<String, Object> params) {
        Integer status = JSON.parseObject(JSON.toJSONString(params.get("status")), Integer.class);
        OrderMode orderMode = JSON.parseObject(JSON.toJSONString(params.get("orderMode")), OrderMode.class);
        Page page = JSON.parseObject(JSON.toJSONString(params.get("page")), Page.class);
        return kindService.selectKinds(status, orderMode, page);
    }

    @RequestMapping(value = "/rows", method = RequestMethod.GET)
    @ResponseBody
    public Integer getRows(@RequestParam("status") Integer status) {
        return kindService.getRows(status);
    }

    @RequestMapping(value = "/kind", method = RequestMethod.GET)
    @ResponseBody
    public Kind getKind(@RequestParam(value = "id",required = false) Integer kindId,
                        @RequestParam(value = "articleId", required = false) Long articleId) {
        if (kindId != null) {
            return kindService.selectKind(kindId);
        }
        if (articleId != null) {
            ArticleKind articleKind= articleKindService.selectArticleKindByArticleId(articleId);
            kindId = articleKind.getKindId();
            return kindService.selectKind(kindId);
        }
        return null;
    }
}
