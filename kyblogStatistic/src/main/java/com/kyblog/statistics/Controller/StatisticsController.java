package com.kyblog.statistics.Controller;

import com.kyblog.statistics.Service.StatisticsService;
import com.kyblog.api.entity.Statistics;
import com.kyblog.api.vo.StatisticsCount;
import com.kyblog.api.vo.VisitStatistics;
import com.kyblog.api.vo.VisitorStatistics;
import com.kyblog.statistics.Task.StatisticsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-27 15:14
 **/

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private StatisticsTask statisticsTask;


    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public void insert(@RequestBody Statistics statistics) {
        statisticsService.insert(statistics);
    }

    @RequestMapping(value = "/visitorStatistics",method = RequestMethod.GET)
    @ResponseBody
    public List<VisitorStatistics> getVisitorStatistics() {
        return statisticsService.getVisitorStatistics();

    }

    @RequestMapping(value = "/visitStatistics", method = RequestMethod.GET)
    @ResponseBody
    public VisitStatistics getVisitStatistics() {
        return statisticsService.getVisitStatistics();
    }

    @RequestMapping(value = "/statisticsCount", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, StatisticsCount> getStatisticsCount() {
        return statisticsService.getStatisticsCount();
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public void refresh() {
        statisticsTask.saveRequestCount();
        statisticsTask.saveReadCount();
    }
}
