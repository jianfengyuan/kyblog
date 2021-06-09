package com.kyblog.statistics.Task;

import com.kyblog.api.entity.*;
import com.kyblog.api.redisKey.ArticleKey;
import com.kyblog.api.redisKey.StatisticsKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-28 09:55
 **/
@Service
public class StatisticsTask implements kyblogConstant {

//    @Qualifier("myRedisTemplate")
    @Autowired
    RedisOpsUtils redisOpsUtils;

    @Qualifier("restTemplateWithRibbon")
    @Autowired
    RestTemplate restTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void saveRequestCount() {
        Set<String> keys = redisOpsUtils.getKeys(StatisticsKey.todayVisitor.getPrefix()+"*");
//        System.out.println(keys);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-24);
        for (String key : keys) {
            String[] strings = key.split("visitor:");
            String str = strings[1];
            String[] split = str.split(":");
            String ip = split[0];
            String id = split[1];
            Integer count = (Integer) redisOpsUtils.get(key);
            Statistics statistics = new Statistics();
            statistics.setIp(ip);
            statistics.setRequestCount(count);
            statistics.setRequestDate(calendar.getTime());
            statistics.setArticleId(Long.valueOf(id));
            restTemplate.put(STATISTICS_SERVICE_PREFIX+"/statistics",statistics);
            redisOpsUtils.deleteCache(StatisticsKey.todayVisitor.getPrefix()+"*");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void saveReadCount() {
        Set<String> keys = redisOpsUtils.getKeys(ArticleKey.getByReadCount.getPrefix()+"*");
        Map<String, Object> param = new HashMap<>();
        for (String key:
             keys) {
            String[] strings = key.split("read:");
            String id = strings[1];
            String count = redisOpsUtils.get(ArticleKey.getByReadCount.getPrefix() + ":" + id).toString();
            param.put("articleId", id);
            param.put("readCount", count);
            restTemplate.put(ARTICLE_SERVICE_PREFIX+"/articles/readCount",param);
        }
        redisOpsUtils.deleteCache(ArticleKey.getByReadCount.getPrefix() + "*");
        redisOpsUtils.deleteCache(ArticleKey.getFamousArticles.getPrefix() + "*");
        redisOpsUtils.deleteCache(ArticleKey.getById.getPrefix()+"*");
    }
}
