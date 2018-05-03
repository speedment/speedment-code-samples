package com.speedment.example.aggregator.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speedment.enterprise.aggregator.Aggregator;
import com.speedment.example.aggregator.db.salaries.Salary;
import com.speedment.example.aggregator.db.salaries.SalaryManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
@RestController
@RequestMapping("/salary/mean")
public class SalaryMeanController {

    private @Autowired SalaryManager salaries;

    @Data
    private static class Result {
        @JsonIgnore
        private Salary.Gender gender;
        private long count;
        private double salary;
    }

    @GetMapping
    Map<Salary.Gender, Result> get() {
        return salaries.stream()
            .collect(Aggregator.builder(Result::new)
                .onPresent(Salary.GENDER).key(Result::setGender)
                .on(Salary.SALARY).average(Result::setSalary)
                .count(Result::setCount)
                .build())
            .collect(toMap(
                Result::getGender,
                Function.identity()
            ));
    }
}
