package com.kyblog.entity;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document("comment")
public class Comment {
    @Id
    private String objectId;
    private Long id;
    private Long articleId;
    private String articleTitle;
    private String name;
    private Date time;
    private Long replyId;
    private Integer type;
    private Integer status;
    private String email;
    private Integer readStatus;

    @Override
    public String toString() {
        return "Comment{" +
                "objectId='" + objectId + '\'' +
                ", id=" + id +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", replyId=" + replyId +
                ", type=" + type +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", readStatus=" + readStatus +
                '}';
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;

    }
}
