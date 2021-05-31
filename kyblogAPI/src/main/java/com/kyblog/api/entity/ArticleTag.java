package com.kyblog.api.entity;

public class ArticleTag {
    private Long id;
    private Long articleId;
    private Long tagId;
    private Integer status;

    @Override
    public String toString() {
        return "ArticleTag{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", tagId=" + tagId +
                ", status=" + status +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
