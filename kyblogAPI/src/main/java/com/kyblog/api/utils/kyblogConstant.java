package com.kyblog.api.utils;

public interface kyblogConstant {

    // ARTICLE STATUS
    Integer ARTICLE_STATUS_DELETED = 0;
    Integer ARTICLE_STATUS_ACTIVE = 1;
    Integer ARTICLE_STATUS_DRAFT = 2;

    // KIND STATUS
    Integer KIND_STATUS_DELETED = 0;
    Integer KIND_STATUS_ACTIVE = 1;
    // TAG STATUS
    Integer TAG_STATUS_DELETED = 0;
    Integer TAG_STATUS_ACTIVE = 1;

    // ARTICLE KIND STATUS
    Integer ARTICLE_KIND_STATUS_DELETED = 0;
    Integer ARTICLE_KIND_STATUS_ACTIVE = 1;

    //    ARTICLE TAG STATUS
    Integer ARTICLE_TAG_STATUS_DELETED = 0;
    Integer ARTICLE_TAG_STATUS_ACTIVE = 1;

    // COMMENT TYPE
    Integer COMMENT = 0;
    Integer REPLY = 1;

    // COMMENT STATUS
    Integer COMMENT_DELETED = 0;
    Integer COMMENT_ACTIVE = 1;

    // COMMENT READ STATUS
    Integer COMMENT_UNREAD = 0;
    Integer COMMENT_READ = 1;

    //    如果配置了server.servlet.context-path, 服务名后记得带上context-path
    String ARTICLE_SERVICE_PREFIX = "/articleservice";
    String COMMENT_SERVICE_PREFIX = "/commentservice";
    String STATISTICS_SERVICE_PREFIX = "/statisticservice";
    String ADMIN_SERVICE_PREFIX = "/adminservice";
    String GATEWAY_PREFIX = "http://GATEWAY";
}
