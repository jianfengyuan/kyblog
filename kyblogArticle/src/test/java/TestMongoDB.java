import com.alibaba.fastjson.JSON;
import com.kyblog.ArticleApplication;
import com.kyblog.entity.AlphaArticle;
import com.kyblog.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ArticleApplication.class)
public class TestMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert() {
        AlphaArticle article = new AlphaArticle();

        article.setTitle("test id 1");
        article.setContent("test test!!");
        System.out.println(mongoTemplate.insert(article).getId());
    }

    @Test
    public void testQuery() {
        Query query = new Query(Criteria.where("title").is("test id 1"));
        AlphaArticle res = mongoTemplate.findOne(query, AlphaArticle.class);
        System.out.println(JSON.toJSON(res));

    }

    @Test
    public void testUpdate() {
        Query query = new Query(Criteria.where("title").is("test"));
        Update update = new Update();
        update.set("title", "testUpdate");
        update.set("content", "update!update");
        mongoTemplate.updateFirst(query, update,"alphaArticle");
        AlphaArticle res = mongoTemplate.findOne(new Query(Criteria.where("title").is("testUpdate")), AlphaArticle.class, "alphaArticle");
        System.out.println(JSON.toJSONString(res));
    }

}
