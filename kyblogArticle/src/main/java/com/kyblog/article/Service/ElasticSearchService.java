package com.kyblog.article.Service;

import com.kyblog.api.elasticsearch.ArticleRepository;
import com.kyblog.api.entity.Article;
import com.kyblog.api.entity.ArticleKind;
import com.kyblog.api.entity.Kind;
import com.kyblog.api.entity.Tag;
import com.kyblog.api.redisKey.ArticleKey;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.utils.kyblogConstant;
import com.kyblog.article.Dao.ArticleDao;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-06-02 00:23
 **/
@Service
public class ElasticSearchService extends BaseService implements kyblogConstant {


    public void initEs() {
        articleRepository.deleteAll();
        List<Article> articles = articleDao.queryArticles(null, ARTICLE_STATUS_ACTIVE, null, null);
        for (Article article:
                articles) {
            articleRepository.save(article);
        }
    }

    public long getEsCount() {
        // 创建搜索请求
        SearchRequest searchRequest = new SearchRequest("kyblog");
        // 创建搜索对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置查询条件
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchRequest.types("article").source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getHits().totalHits;
    }
    public List<Article> searchFromEs(String content) {
        List<Article> result = new ArrayList<>();
        // 创建搜索请求
        SearchRequest searchRequest = new SearchRequest("kyblog");
        // 创建搜索对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置查询条件
        searchSourceBuilder.query(QueryBuilders
                .multiMatchQuery(content, "title", "content", "introduce"))
                .from(0)
                .size((int) getEsCount())
                .highlighter(new HighlightBuilder().field("*")
                .requireFieldMatch(false).preTags("<span style='color:red;font-weight:500'>").postTags("</span>"));

        searchRequest.types("article").source(searchSourceBuilder);

        // 执行搜索
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            Article temp = new Article();
            temp.setId(Long.valueOf(map.get("id").toString()));
            temp.setIntroduce(map.get("introduce").toString());
//            temp.setPublishTime(new Date((Long) map.get("publishTime")));
            temp.setTitle(map.get("title").toString());
//            temp.setReadCount((Long) map.get("readCount"));
            Map<String, HighlightField> fields = hit.getHighlightFields();
            // 设置标题高亮
            if (fields.containsKey("title")) {
                temp.setTitle(fields.get("title").fragments()[0].toString());
            }
            // 设置摘要高亮
            if (fields.containsKey("introduce")) {
                temp.setIntroduce(fields.get("introduce").fragments()[0].toString());
            }
            result.add(temp);
        }
        for (Article article :
                result) {
            Article temp = articleDao.queryById(article.getId(),null);
            List<Tag> tags = tagDao.queryByArticleId(article.getId());
            ArticleKind ak = articleKindDao.queryArticleKindByArticleId(article.getId());
            Kind kind = kindDao.queryKindById(ak.getKindId());
            Long count;
            if (redisOpsUtils.hasKey(ArticleKey.getByReadCount.getPrefix() + ":" + article.getId())) {
                count = Long.valueOf(redisOpsUtils.get(ArticleKey.getByReadCount.getPrefix() + ":" + article.getId()).toString());
            } else {
                count = temp.getReadCount();
                redisOpsUtils.set(ArticleKey.getByReadCount.getPrefix() + ":" + article.getId(), count);
            }
            article.setTags(tags);
            article.setKind(kind);
            article.setReadCount(count);
            article.setStatus(ARTICLE_STATUS_ACTIVE);
            article.setEdictTime(temp.getEdictTime());
            article.setPublishTime(temp.getPublishTime());
            article.setBackground(temp.getBackground());

        }
        return result;
    }
}
