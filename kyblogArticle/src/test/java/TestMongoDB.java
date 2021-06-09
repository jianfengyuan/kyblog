import com.alibaba.fastjson.JSON;
import com.kyblog.article.ArticleApplication;
import com.kyblog.api.entity.AlphaArticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("local")
@ContextConfiguration(classes = ArticleApplication.class)
public class TestMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert() {
        AlphaArticle article = new AlphaArticle();
        article.setId(1);
        article.setTitle("test id 1");
        article.setContent("test test!!");
        System.out.println(article);
        article = mongoTemplate.insert(article);
        System.out.println(article);
    }

    @Test
    public void testQuery() {
        Query query = new Query(Criteria.where("id").is(1));
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
