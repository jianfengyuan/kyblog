import com.kyblog.api.entity.Comment;
import com.kyblog.api.entity.Kind;
import com.kyblog.api.entity.OrderMode;
import com.kyblog.api.entity.Page;
import com.kyblog.article.ArticleApplication;
import com.kyblog.api.entity.*;
import com.kyblog.api.redisKey.StatisticsKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import com.kyblog.api.vo.StatisticsCount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-20 15:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ArticleApplication.class)
//@ActiveProfiles(value = "article-service")
public class TestMapper implements kyblogConstant {
    @Qualifier("restTemplateWithRibbon")
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisOpsUtils redisOpsUtils;

    ParameterizedTypeReference<List<Comment>> COMMENT_REF = new ParameterizedTypeReference<List<Comment>>() {
    };
    String ARTICLE_SERVICE_PREFIX = "http://ARTICLESERVICE";
    String STATISTICS_SERVICE_PREFIX = "http://STATISTICSERVICE";

    protected ParameterizedTypeReference<List<Kind>> KIND_REF = new ParameterizedTypeReference<List<Kind>>() {
    };
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void TestCommentApplication() {
        OrderMode orderModeTemplate = new OrderMode();
        Page pageTemplate = new Page();
        String COMMENT_SERVICE_PREFIX = "http://COMMENTSERVICE";
        Comment commentTemplate = new Comment();
        Map<String, Object> map = new HashMap<>();
        orderModeTemplate.setColumn("time");
        commentTemplate.setType(COMMENT);
        commentTemplate.setArticleId(11L);
        commentTemplate.setStatus(COMMENT_ACTIVE);
        map.put("comment", commentTemplate);
        map.put("orderMode", orderModeTemplate);
        System.out.println(map);
        ResponseEntity<List<Comment>> commentResponseEntity = restTemplate.exchange(
                COMMENT_SERVICE_PREFIX+"/comments/list", HttpMethod.POST,
                new HttpEntity<>(map), COMMENT_REF
        );
        List<Comment> commentList = commentResponseEntity.getBody();
        System.out.println("CommentList: "+ commentList);
    }

    @Test
    public void testKindApplication() {
        String ARTICLE_SERVICE_PREFIX = "http://ARTICLESERVICE";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("articleId", 11);
//        map.put("id", null);
        Kind kind = restTemplate.getForObject(
                ARTICLE_SERVICE_PREFIX + "/kinds/kind?articleId={articleId}", Kind.class, map

        );
        System.out.println(kind);
    }

    @Test
    public void testGetKindList() {
        Map<String, Object> param = new HashMap<>();
        OrderMode orderMode = new OrderMode();
        Page page = new Page();
        orderMode.setColumn("article_count");
        param.put("status", KIND_STATUS_ACTIVE);
        param.put("orderMode", orderMode);

        ResponseEntity<List<Kind>> commentResponseEntity = restTemplate.exchange(
                ARTICLE_SERVICE_PREFIX+"/kinds/list", HttpMethod.POST,
                new HttpEntity<>(param), KIND_REF
        );
        List<Kind> kinds = commentResponseEntity.getBody();
        System.out.println(kinds);

    }

    @Test
    public void testRedisList() {
        List<String> strs = new ArrayList<>();
        strs.add("kim");
        strs.add("anna");
        redisOpsUtils.lSet("test", new String[]{"kim", "anna"});
//        System.out.println(redisOpsUtils.lGet("test", 0, -1));
    }

    @Test
    public void testTask() {
        Set<String> keys = redisOpsUtils.getKeys(StatisticsKey.todayVisitor.getPrefix()+"*");
        System.out.println(keys);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        for (String key : keys) {
            String[] strings = key.split("visitor:");
            String str = strings[1];
            String[] split = str.split(":");
            String ip = split[0];
            String id = split[1];
            Integer count = (Integer) redisOpsUtils.get(key);
//            Integer count = redisService.get(StatisticsKey.todayVisitor, str, Integer.class);
            Statistics statistics = new Statistics();
            statistics.setIp(ip);
            statistics.setRequestCount(count);
            statistics.setRequestDate(calendar.getTime());
            statistics.setArticleId(Long.valueOf(id));
//            restTemplate.put(STATISTICS_SERVICE_PREFIX+"/statistics",statistics);
//            Statistics statistics = new Statistics(ip,count,calendar.getTime(),Integer.valueOf(id));
//            statisticsMapper.insert(statistics);
//            redisService.del(StatisticsKey.todayVisitor, str);
//            System.out.println(restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/visitStatistics", VisitStatistics.class));
        }
//        System.out.println(restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/visitorStatistics", List.class));
        Map<String, StatisticsCount> res = (Map<String, StatisticsCount>)restTemplate.getForObject(STATISTICS_SERVICE_PREFIX + "/statistics/statisticsCount", Map.class);
        System.out.println(res.get("yesterday"));
        System.out.println(res.get("total"));
    }

}
