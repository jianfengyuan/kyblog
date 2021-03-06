package com.kyblog.api.entity;

public class Kind {
    private Integer id;
    private String name;
    private Integer articleCount;
    private String introduce;
    private Integer status;

    @Override
    public String toString() {
        return "Kind{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", articleCount=" + articleCount +
                ", introduce='" + introduce + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
