package com.kyblog.api.vo;

import com.kyblog.api.entity.Statistics;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-27 13:15
 **/

public class VisitorStatistics {
    String target;
    Statistics statistics;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "VisitorStatistics{" +
                "target='" + target + '\'' +
                ", statistics=" + statistics +
                '}';
    }
}
