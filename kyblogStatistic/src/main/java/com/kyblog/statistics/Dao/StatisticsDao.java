package com.kyblog.statistics.Dao;

import com.kyblog.api.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface StatisticsDao {
    int insertStatistics(Statistics statistics);

    List<Statistics> queryAll(@Param("statistics") Statistics statistics);

    Integer getRequestCount(@Param("type") Integer type);

    Integer getVisitorCount(@Param("type") Integer type);

    List<Integer> getRecentRequests();

    List<Integer> getRecentVisitors();

    List<Date> getRecentDates();

}
