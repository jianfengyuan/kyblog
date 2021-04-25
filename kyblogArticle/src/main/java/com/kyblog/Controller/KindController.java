package com.kyblog.Controller;

import com.kyblog.Service.KindService;
import com.kyblog.entity.Kind;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.entity.Tag;
import com.kyblog.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.camel4underline;
import static com.kyblog.utils.BlogUtils.getJsonString;

@Controller
@RequestMapping(path = "/kind")
public class KindController implements kyblogConstant {
    @Autowired
    private KindService kindService;
    @RequestMapping(value = "/addKind", method = RequestMethod.POST)
    @ResponseBody
    public String add(Kind kind) {

        return kindService.insertKind(kind)==1?getJsonString(200):getJsonString(501);
    }

    @RequestMapping(value = "/kindList", method = RequestMethod.GET)
    public String getKindPage(Model model) {
        int rows = kindService.getRows(KIND_STATUS_ACTIVE);
        model.addAttribute("rows", rows);
        return "/admin/kinds";
    }

    @RequestMapping(value = "/kindData", method = RequestMethod.POST)
    @ResponseBody
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

//    @RequestMapping(value = "/kindList", method = RequestMethod.GET)
    @Deprecated
    public String getKinds(OrderMode orderMode, Page page, Model model) {
        page.setPath("/");
        page.setRows(kindService.getRows(KIND_STATUS_ACTIVE));
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderMode,page);
        System.out.println(kindList);
        model.addAttribute("kinds", kindList);
        return "/admin/kinds";
    }

    @RequestMapping(value = "/deleteKind", method = RequestMethod.POST)
    @ResponseBody
    public String deleteKind(Kind kind) {
        if (kind.getName().equals("-")) {
            return getJsonString(501 );
        }
        kindService.deleteKind(kind);
        return getJsonString(200);
    }

    @RequestMapping(value = "/updateKind", method = RequestMethod.POST)
    @ResponseBody
    public String updateKind(Kind kind) {
        if (kind.getName().equals("-")) {
            return getJsonString(501 );
        }
        kindService.updateKind(kind);
        return getJsonString(200);
    }
}
