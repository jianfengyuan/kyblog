package com.kyblog.statistics.Service;

import com.kyblog.statistics.Dao.StatisticsDao;
import com.kyblog.api.entity.Statistics;
import com.kyblog.api.redisKey.StatisticsKey;
import com.kyblog.statistics.Dao.StatisticsDao;
import com.kyblog.api.utils.IPUtils;
import com.kyblog.api.utils.RedisOpsUtils;
import com.kyblog.api.vo.StatisticsCount;
import com.kyblog.api.vo.VisitStatistics;
import com.kyblog.api.vo.VisitorStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-27 12:38
 **/
@Service
public class StatisticsService {
    @Autowired
    private StatisticsDao statisticsDao;

//    private RedisOpsUtils redisOpsUtils;

    public Integer insert(Statistics statistics) {
        return statisticsDao.insertStatistics(statistics);
    }

    public List<VisitorStatistics> getVisitorStatistics() {
        // 获取所有的访问信息

        List<Statistics> statistics = statisticsDao.queryAll(new Statistics());
        List<VisitorStatistics> visitorStatistics = new ArrayList<>();
        statistics.forEach(temp -> {
            VisitorStatistics vs = new VisitorStatistics();
//            if (temp.getArticleId() == -1) {
            vs.setStatistics(temp);
//                vs.setTarget("主页");
            visitorStatistics.add(vs);
//            } else {
////                String title = articleMapper.getTitle(temp.getArticleId());
//                if (!ObjectUtils.isEmpty("articleTitle")) {
//                    vs.setStatistics(temp);
//                    vs.setTarget("articleTitle");
//                    visitorStatistics.add(vs);
//                } else {
//                    vs.setStatistics(temp);
//                    vs.setTarget("其他");
//                    visitorStatistics.add(vs);
//                }
//            }
        });
        return visitorStatistics;
    }

    public VisitStatistics getVisitStatistics() {
        VisitStatistics statistics = new VisitStatistics();
        SimpleDateFormat dateFm = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        // 设置日期
        List<Date> recentDates = statisticsDao.getRecentDates();
        Collections.reverse(recentDates);
        String[] dates = new String[10];
        for (int i = 0; i < recentDates.size(); i++) {
            dates[i] = dateFm.format(recentDates.get(i));
//            calendar.add(Calendar.DATE,-1);

        }
        statistics.setDates(dates);
        // 设置
        List<Integer> recentRequests = statisticsDao.getRecentRequests();
        Collections.reverse(recentRequests);
        Integer[] requests = new Integer[10];
        for (int i = 0; i < recentRequests.size(); i++) {
            requests[i] = recentRequests.get(i);
        }
        statistics.setRequests(requests);
        // 设置访客量
        List<Integer> recentVisitors = statisticsDao.getRecentVisitors();
        Collections.reverse(recentVisitors);
        Integer[] visitors = new Integer[10];
        for (int i = 0; i < recentVisitors.size(); i++) {
            visitors[i] = recentVisitors.get(i);
        }
        statistics.setVisitors(visitors);
        return statistics;
    }

    public Map<String, StatisticsCount> getStatisticsCount() {
        Map<String, StatisticsCount> map = new HashMap<>();
        StatisticsCount statisticsCount = new StatisticsCount();
        statisticsCount.setRequests(statisticsDao.getRequestCount(1));
        statisticsCount.setVisitor(statisticsDao.getVisitorCount(1));
        map.put("yesterday", statisticsCount);
        statisticsCount = new StatisticsCount();
        statisticsCount.setRequests(statisticsDao.getRequestCount(7));
        statisticsCount.setVisitor(statisticsDao.getVisitorCount(7));
        map.put("week", statisticsCount);
        statisticsCount = new StatisticsCount();
        statisticsCount.setRequests(statisticsDao.getRequestCount(30));
        statisticsCount.setVisitor(statisticsDao.getVisitorCount(30));
        map.put("month",statisticsCount);
        statisticsCount = new StatisticsCount();
        statisticsCount.setRequests(statisticsDao.getRequestCount(365));
        statisticsCount.setVisitor(statisticsDao.getVisitorCount(365));
        map.put("year", statisticsCount);
        statisticsCount = new StatisticsCount();
        statisticsCount.setRequests(statisticsDao.getRequestCount(-1));
        statisticsCount.setVisitor(statisticsDao.getVisitorCount(-1));
        map.put("total", statisticsCount);
        return map;
    }
}
