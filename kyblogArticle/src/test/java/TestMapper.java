import com.kyblog.ArticleApplication;
import com.kyblog.Dao.ArticleKindDao;
import com.kyblog.Dao.CommentDao;
import com.kyblog.entity.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-04-20 15:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ArticleApplication.class)
public class TestMapper {
    @Autowired
    ArticleKindDao articleKindDao;
    @Autowired
    CommentDao commentDao;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    public void testArticleKindMapper() {
        System.out.println(articleKindDao.queryArticleKindByArticleIdAndKindID(
                12L, 2, 0));
    }

    @Test
    public void testCommentMapper() {
        Comment comment = new Comment();
//        comment.setId(1L);
        comment.setStatus(1);
        comment.setReadStatus(0);
        System.out.println(commentDao.queryAll(comment, null, null));
    }

    @Test
    public void testInsertComment() {
        Comment comment = new Comment();
        comment.setArticleTitle("test");
        comment.setArticleId(11L);
        comment.setName("kkk");
        comment.setIp("127.1.1.1");
        comment.setEmail("1234@qq.com");
        comment.setTime(new Date());
        comment.setType(1);
        comment.setStatus(1);
        comment.setReadStatus(0);

        for (int i = 0; i < 20; i++) {
            comment.setContent("test"+i);
            comment = mongoTemplate.insert(comment);
            commentDao.insertComment(comment);
            comment.setId(null);
            comment.setObjectId(null);
        }
    }
}
