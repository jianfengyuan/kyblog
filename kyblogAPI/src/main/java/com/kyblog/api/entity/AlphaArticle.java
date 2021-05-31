package com.kyblog.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Document(collection = "alphaArticle")
public class AlphaArticle implements Serializable {
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    String ObjectId;
    String title;
    String content;

    @Override
    public String toString() {
        return "AlphaArticle{" +
                "id=" + ObjectId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String ObjectId) {
        this.ObjectId = ObjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
