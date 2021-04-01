package com.kyblog.Controller;

import com.kyblog.entity.Kind;
import com.kyblog.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.kyblog.utils.BlogUtils.getJsonString;

@Controller
@RequestMapping(path = "/kind")
public class KindController {

    @RequestMapping(value = "/")
    public String add() {
        return getJsonString(200);
    }

    @RequestMapping(value = "/kindList",method = RequestMethod.GET)
    public String getKinds() {
        return "/admin/kinds";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteKind(Kind kind) {
        return getJsonString(200);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateKind(Kind kind) {
        return getJsonString(200);
    }
}
