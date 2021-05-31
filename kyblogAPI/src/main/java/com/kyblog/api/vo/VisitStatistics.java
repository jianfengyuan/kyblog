package com.kyblog.api.vo;

import java.util.Arrays;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-27 13:12
 **/

public class VisitStatistics {
    // 从昨天开始近10天的日期
    private String[] dates;
    // 从昨天开始近10天的访问量
    private Integer[] requests;
    // 从昨天开始近10天的访客
    private Integer[] visitors;

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }

    public Integer[] getRequests() {
        return requests;
    }

    public void setRequests(Integer[] requests) {
        this.requests = requests;
    }

    public Integer[] getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer[] visitors) {
        this.visitors = visitors;
    }

    public String getDatesString(){
        String result = "[";
        for (int i = 0; i < dates.length; i++) {
            result += '"'+dates[i]+'"'+",";
        }
        return result+"]";
    }

    public String getRequestsString(){
        return Arrays.toString(requests);
    }

    public String getVisitorsString(){
        return Arrays.toString(visitors);
    }

    @Override
    public String toString() {
        return "visitStatistics{" +
                "dates=" + Arrays.toString(dates) +
                ", requests=" + Arrays.toString(requests) +
                ", visitors=" + Arrays.toString(visitors) +
                '}';
    }
}
