import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-23 13:34
 **/

public class testArticleService {
    String ARTICLE_SERVICE_PREFIX = "http://ARTICLESERVICE";
    @Qualifier("restTemplateWithRibbon")
    @Autowired
    RestTemplate restTemplate;

}
