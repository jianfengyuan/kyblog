package com.kyblog.entity;

public class ArticleKind {
    private Long articleId;
    private Integer kindId;

    @Override
    public String toString() {
        return "ArticleKind{" +
                "articleId=" + articleId +
                ", kindId=" + kindId +
                '}';
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
}
