package com.kyblog.Controller;

import com.kyblog.Service.KindService;
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
@RequestMapping(path = "/kind")
public class KindController implements kyblogConstant {
    @Autowired
    private KindService kindService;
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(String name, String introduce) {
        kindService.insertKind(name, introduce);
        return getJsonString(200);
    }

    @RequestMapping(value = "/kindList",method = RequestMethod.GET)
    public String getKinds(Integer orderMode, Page page, Model model) {
        page.setPath("/");
        page.setRows(kindService.selectRows(KIND_STATUS_ACTIVE));
        List<Kind> kindList = kindService.selectKinds(KIND_STATUS_ACTIVE, orderMode, page.getOffset(), page.getLimit());
        System.out.println(kindList);
        model.addAttribute("kinds", kindList);
        return "/admin/kinds";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteKind(Kind kind) {
        kindService.updateKind(kind.getId(), null, null, null, KIND_STATUS_DELETED);
        return getJsonString(200);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateKind(Kind kind) {
        kindService.updateKind(kind.getId(), kind.getName(), kind.getIntroduce(),null, kind.getStatus());
        return getJsonString(200);
    }
}
