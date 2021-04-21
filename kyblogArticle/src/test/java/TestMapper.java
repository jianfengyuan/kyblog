import com.kyblog.ArticleApplication;
import com.kyblog.Dao.ArticleKindDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    @Test
    public void testArticleKindMapper() {
        System.out.println(articleKindDao.queryArticleKindByArticleIdAndKindID(
                12L, 2, 0));
    }
}
