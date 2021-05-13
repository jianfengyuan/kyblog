package com.kyblog.Controller;

import com.alibaba.fastjson.JSONObject;
import com.kyblog.entity.Article;
import com.kyblog.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kyblog.utils.BlogUtils.getJsonString;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-06 13:35
 **/

@Controller
@RequestMapping("/image")
public class ImageController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("image") MultipartFile file, @RequestParam("flag") Integer flag, Long id) throws IOException {
        if (!ImageUtils.checkFileSize(file.getSize(),1,"M")) {
            return getJsonString(502);
        }
        String imgPath = imageUtils.upload(file,"o");
        Article article = new Article();
        article.setId(id);
        article.setBackground(imgPath);
//        articleService.updateArticle(article);
        Map<String, Object> map = new HashMap<>();
        map.put("img", imgPath);
        return getJsonString(200, "上传图片成功", map);
    }

    @RequestMapping(value = "/article", method = RequestMethod.POST)
    @ResponseBody
    public String markdownImg(@RequestParam("editormd-image-file") MultipartFile file) throws IOException {
        String url = imageUtils.upload(file, "o");
        Map<String, Object> map = new HashMap<>();
        map.put("img", url);
        if (url != null) {
            logger.info("图片上传成功, url: " + url);
            return getJsonString(200, "上传成功", map);
        } else {
            logger.info("上传失败");
            return getJsonString(404, "上传失败");
        }

    }
}
