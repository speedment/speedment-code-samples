package com.speedment.example.spring.aggregate.controller;

import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.util.StatisticsUtil;
import com.speedment.enterprise.plugins.json.JsonComponent;
import com.speedment.example.spring.aggregate.db.Salary;
import com.speedment.example.spring.aggregate.db.SalaryManager;
import com.speedment.example.spring.aggregate.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static com.speedment.enterprise.plugins.json.JsonCollectors.*;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
@AllArgsConstructor
@RestController
@EnableScheduling
@RequestMapping("/speedment")
public class SpeedmentController {

    private final SalaryManager salaries;
    private final DataStoreComponent datastore;
    private final JsonComponent json;

    @PostConstruct
    void loadInitialState() {
        datastore.load();
        StatisticsUtil.prettyPrint(
            datastore.getStatistics()
        ).forEachOrdered(System.out::println);
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    void reloadDatastore() {
        datastore.reload();
    }

    @GetMapping
    String getEmployeeSalaries(@RequestParam String from,
                               @RequestParam String to) {
        return salaries.stream()
            .filter(Salary.FROM_DATE.lessThan(Utils.toEpochSecond(to)))
            .filter(Salary.TO_DATE.greaterOrEqual(Utils.toEpochSecond(from)))
            .collect(
                json.collector(Salary.class)
                    .put("count", count())
                    .put("from", min(Salary.FROM_DATE, Utils::fromEpochSecond))
                    .put("to",   max(Salary.TO_DATE,   Utils::fromEpochSecond))
                    .put("average", average(Salary.SALARY, Utils::toCurrency))
                    .build()
        );
    }
}
