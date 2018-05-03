package com.speedment.example.aggregator.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speedment.enterprise.aggregator.Aggregator;
import com.speedment.example.aggregator.db.salaries.Salary;
import com.speedment.example.aggregator.db.salaries.SalaryManager;
import com.speedment.runtime.compute.ToInt;
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
@RequestMapping("/salary/correlation")
public class SalaryCorrelationController {

    /**
     * The number of seconds in a day. 24 * 60 * 60 = 86400.
     */
    private final static int SECONDS_IN_A_DAY = 86_400;

    /**
     * Custom deserializer that takes the epoch time of when the employee got
     * the current salary - the date they were first hired and divides it with
     * {@code SECONDS_IN_A_DAY} to get the number of days they have been
     * employed.
     */
    private final static ToInt<Salary> DAYS_EMPLOYED =
        Salary.FROM_DATE
            .minus(Salary.HIRE_DATE)
            .divide(SECONDS_IN_A_DAY)
            .asInt();

    private @Autowired SalaryManager salaries;

    @Data
    private static class Result {
        @JsonIgnore
        private Salary.Gender gender;
        private long count;
        private double salaryMean;
        private double daysEmployedMean;
        private double salaryVariance;
        private double daysEmployedVariance;

        @JsonIgnore
        private double covariance;
        private double correlation;
    }

    @GetMapping
    Map<Salary.Gender, Result> get() {
        return salaries.stream()
            .collect(Aggregator.builder(Result::new)
                .onPresent(Salary.GENDER).key(Result::setGender)
                .count(Result::setCount)
                .on(Salary.SALARY).average(Result::setSalaryMean)
                .on(DAYS_EMPLOYED).average(Result::setDaysEmployedMean)
                .on(Salary.SALARY).variance(Result::setSalaryVariance)
                .on(DAYS_EMPLOYED).variance(Result::setDaysEmployedVariance)
                .on(Salary.SALARY).covariance(DAYS_EMPLOYED, Result::setCovariance)
                .build())
            .peek(result -> {
                if (result.salaryVariance == 0
                ||  result.daysEmployedVariance == 0) {
                    result.setCorrelation(Double.NaN);
                } else {
                    result.setCorrelation(
                        result.covariance / (
                            Math.sqrt(result.salaryVariance) *
                                Math.sqrt(result.daysEmployedVariance)
                        )
                    );
                }
            })
            .collect(toMap(
                Result::getGender,
                Function.identity()
            ));
    }
}
