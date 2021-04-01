package com.kyblog.utils;

public class RedisKeyUtils {
    private static final String SPLIT = ":";
    private static final String PREFIX_TICKET = "ticket";

    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }
}
