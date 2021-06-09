package com.kyblog.article.Service;

import com.kyblog.api.elasticsearch.ArticleRepository;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.article.Dao.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-06-08 11:52
 **/

public class BaseService {
    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleTagDao articleTagDao;

    @Autowired
    ArticleKindDao articleKindDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    KindDao kindDao;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RedisOpsUtils redisOpsUtils;

    @Autowired
    ElasticSearchService elasticSearchService;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    ArticleRepository articleRepository;
}
