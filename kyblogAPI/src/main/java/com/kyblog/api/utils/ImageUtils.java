package com.kyblog.api.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import cn.hutool.core.codec.Base64;
import java.io.IOException;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-06 13:36
 **/

@Component
public class ImageUtils implements GitHubConstant {
    @Qualifier("restTemplateNoRibbon")
//    @Autowired
    private RestTemplate restTemplate;

    public String upload(MultipartFile file, String flag)throws IOException {
        String trueFileName = file.getOriginalFilename();
        assert trueFileName != null;
        String encode = "utf-8";
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + suffix;
        String paramImgFile = Base64.encode(file.getBytes());
        //转存到GitHub
        JSONObject json = new JSONObject();
        json.put("message", CREATE_REPOS_MESSAGE);
        json.put("content", paramImgFile);
        StringEntity stringEntity = new StringEntity(json.toJSONString(),encode);
        String targetDir = "";

        if ("a".equals(flag)){
            targetDir = IMG_FILE_DEST_PATH + fileName;
        }else {
            targetDir = "other/" + fileName;
        }

        String requestUrl = String.format(CREATE_REPOS_URL, OWNER,
                REPO_NAME, targetDir);
        System.out.println(requestUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "token " + ACCESS_TOKEN);

        ResponseEntity<JSONObject> responseEntity = this.restTemplate.exchange(requestUrl, HttpMethod.PUT,
                new HttpEntity<>(json, httpHeaders), JSONObject.class);
        System.out.println(responseEntity);
//        HttpPut httpPut = new HttpPut(requestUrl);
//        /** header中通用属性 */
//        httpPut.setHeader("Accept", "application/vnd.github.v3+json");
//        httpPut.setHeader("Authorization", "token "+ACCESS_TOKEN);
//
//        httpPut.setEntity(stringEntity);
//        String content = null;// 回复的内容
//        CloseableHttpResponse httpResponse = null;// 回复对象
//        try {
//            // 响应信息
//            httpResponse = closeableHttpClient.execute(httpPut);// 执行请求
//            HttpEntity entity = httpResponse.getEntity();// 接收响应
//            content = EntityUtils.toString(entity, encode);// 将响应转为String类型
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {// 关闭资源
//            try {
//                httpResponse.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            closeableHttpClient.close(); // 关闭连接、释放资源
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(content);
//        System.out.println(stringEntity);
//
        if (JSONUtil.parseObj(responseEntity.getBody()).getObj("commit") != null) {
            System.out.println(GITPAGE_REQUEST_URL + targetDir);
            return GITPAGE_REQUEST_URL + targetDir;
        }
        return null;
    }



    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }
}
