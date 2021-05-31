package com.kyblog.api.vo;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-27 13:13
 **/

public class StatisticsCount {
    Integer requests;
    Integer visitor;

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    public Integer getVisitor() {
        return visitor;
    }

    public void setVisitor(Integer visitor) {
        this.visitor = visitor;
    }

    @Override
    public String toString() {
        return "StatisticsCount{" +
                "requests=" + requests +
                ", visitor=" + visitor +
                '}';
    }

}
