package com.kyblog.api.entity;

import java.util.Date;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-26 16:54
 **/

public class Statistics {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 访问次数
     */
    private Integer requestCount;

    /**
     * 日期
     */
    private Date requestDate;

    /**
     * 访问的文章id。如果为-1表示访问主页
     */
    private Long articleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                "ip=" + ip +
                "requestCount=" + requestCount +
                "requestDate=" + requestDate +
                "articleId=" + articleId +
                '}';
    }
}
