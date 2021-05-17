import com.kyblog.ArticleApplication;
import com.kyblog.Dao.ArticleKindDao;
import com.kyblog.entity.Comment;
import com.kyblog.entity.Kind;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import com.kyblog.utils.kyblogConstant;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-20 15:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ArticleApplication.class)
@ActiveProfiles(value = "article-service")
public class TestMapper implements kyblogConstant {
    @Qualifier("restTemplateWithRibbon")
    @Autowired
    RestTemplate restTemplate;

    ParameterizedTypeReference<List<Comment>> COMMENT_REF = new ParameterizedTypeReference<List<Comment>>() {
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
}
