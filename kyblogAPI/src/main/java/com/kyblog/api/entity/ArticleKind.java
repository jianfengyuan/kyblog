package com.kyblog.api.entity;

public class ArticleKind {
    private Long id;
    private Long articleId;
    private Integer kindId;
    private Integer status;

    @Override
    public String toString() {
        return "ArticleKind{" +
                "articleId=" + articleId +
                ", kindId=" + kindId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
