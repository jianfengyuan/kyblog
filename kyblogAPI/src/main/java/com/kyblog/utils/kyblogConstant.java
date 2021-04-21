package com.kyblog.utils;

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

    // TAG ORDER
    Integer TAG_ORDER_BY_ID = 0;
    Integer TAG_ORDER_BY_NAME = 1;
    Integer TAG_ORDER_BY_COUNT = 2;
    // KIND ORDER
    Integer KIND_ORDER_BY_ID = 0;
    Integer KIND_ORDER_BY_NAME = 1;
    Integer KIND_ORDER_BY_COUNT = 2;
    // ARTICLE ORDER
    Integer ARTICLE_ORDER_BY_ID = 0;
    Integer ARTICLE_ORDER_BY_NAME = 1;
    Integer ARTICLE_ORDER_BY_READ_COUNT = 2;
    Integer ARTICLE_ORDER_BY_TIME = 3;

    // ORDER MODE
    Integer ASC_ORDER = 0;
    Integer DESC_ORDER = 1;
}
