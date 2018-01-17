package com.speedment.example.aggregator.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.aggregator.Aggregator;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.example.aggregator.db.salaries.Salary;
import com.speedment.runtime.config.identifier.TableIdentifier;
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

    private final static TableIdentifier<Salary> TABLE =
        Salary.EMP_NO.identifier().asTableIdentifier();

    private @Autowired DataStoreComponent dataStore;

    @Data
    private static class Result {
        @JsonIgnore
        private final long ref;
        private long count;
        private double salary;
    }

    @GetMapping
    Map<Salary.Gender, Result> get() {
        final EntityStore<Salary> store = entityStore();
        final Aggregator<Result> agg = Aggregator.builder(store, Result::new)
            .withEnumKey(Salary.GENDER)
            .withAverage(Salary.SALARY, Result::setSalary)
            .withCount(Result::setCount)
            .build();

        return agg.aggregate(entityStore().references())
            .collect(toMap(
                result -> store.deserializeAny(result.ref, Salary.GENDER),
                Function.identity()
            ));
    }

    private EntityStore<Salary> entityStore() {
        return dataStore.currentHolder().getEntityStore(TABLE);
    }
}
