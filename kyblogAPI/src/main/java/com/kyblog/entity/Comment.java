package com.kyblog.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "comment")
public class Comment {
    @Id

    private String objectId;

    private Long id;
    private Long articleId;
    private String articleTitle;
    private String name;
    private String content;
    private Date time;
    private Long replyId;
    private String ip;
    private Integer type;
    private Integer status;
    private String email;
    private Integer readStatus;
    private List<Comment> replies;

    @Override
    public String toString() {
        return "Comment{" +
                "objectId='" + objectId + '\'' +
                ", id=" + id +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", replyId=" + replyId +
                ", ip='" + ip + '\'' +
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }
}
