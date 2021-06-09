package com.kyblog.article.Controller;

import com.kyblog.api.entity.Article;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-06-02 14:44
 **/
@Controller
@RequestMapping("/search")
public class SearchController extends BaseController implements kyblogConstant {
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void initEs() {
        elasticSearchService.initEs();
    }

    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> search(@RequestParam("content") String content) {
        if (elasticSearchService.getEsCount() == 0) {
            elasticSearchService.initEs();
        }
        List<Article> articles = elasticSearchService.searchFromEs(content);
        return articles;
    }

    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> searchTag(@RequestParam("tagId") Long tagId) {
        return articleService.findArticlesByTagId(tagId);
    }

    @RequestMapping(value = "/kind", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> searchKind(@RequestParam("kindId") Integer kindId) {
        return articleService.findArticlesByKindId(kindId);
    }
}
