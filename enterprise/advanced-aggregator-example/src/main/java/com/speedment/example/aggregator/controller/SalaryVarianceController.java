package com.speedment.example.aggregator.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.aggregator.Aggregator;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.enterprise.datastore.runtime.entitystore.Order;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache;
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
@RequestMapping("/salary/variance")
public class SalaryVarianceController {

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

    @Data
    private static class Result2 {
        @JsonIgnore
        private final long ref;
        private final long count;
        private final double salaryMean;
        private double salaryVariance;

        Result2(Result firstPass) {
            ref        = firstPass.ref;
            count      = firstPass.count;
            salaryMean = firstPass.salary;
        }
    }

    @GetMapping
    Map<Salary.Gender, Result2> get() {
        final EntityStore<Salary> store = entityStore();
        final Aggregator<Result> agg = Aggregator.builder(store, Result::new)
            .withEnumKey(Salary.GENDER)
            .withAverage(Salary.SALARY, Result::setSalary)
            .withCount(Result::setCount)
            .build();

        return agg.aggregate(entityStore().references())
            .flatMap(previous -> {
                final Salary.Gender gender =
                    store.deserializeAny(previous.ref, Salary.GENDER);

                return Aggregator.builder(store, ref -> new Result2(previous))
                    .withEnumKey(Salary.GENDER)
                    .withVariance(
                        Salary.SALARY,
                        previous.salary,
                        Result2::setSalaryVariance)
                    .build()
                    .aggregate(genderFieldCache().equal(
                        gender, Order.ASC, 0, Long.MAX_VALUE)
                    );
                }
            )
            .collect(toMap(
                result -> store.deserializeAny(result.ref, Salary.GENDER),
                Function.identity()
            ));
    }

    private EntityStore<Salary> entityStore() {
        return dataStore.currentHolder().getEntityStore(TABLE);
    }

    private FieldCache.OfEnum<Salary.Gender> genderFieldCache() {
        return dataStore.currentHolder().getFieldCache(
            Salary.GENDER.identifier()
        );
    }
}
