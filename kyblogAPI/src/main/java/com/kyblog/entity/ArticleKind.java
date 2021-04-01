package com.kyblog.entity;

public class ArticleKind {
    private Long articleId;
    private Long kindId;

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

    public Long getKindId() {
        return kindId;
    }

    public void setKindId(Long kindId) {
        this.kindId = kindId;
    }
}
