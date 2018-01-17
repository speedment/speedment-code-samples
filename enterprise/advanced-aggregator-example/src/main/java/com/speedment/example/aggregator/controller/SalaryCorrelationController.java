package com.speedment.example.aggregator.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.aggregator.Aggregator;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.enterprise.datastore.runtime.entitystore.Order;
import com.speedment.enterprise.datastore.runtime.fieldcache.FieldCache;
import com.speedment.enterprise.datastore.runtime.function.deserialize.DeserializeDouble;
import com.speedment.example.aggregator.db.salaries.Salary;
import com.speedment.runtime.config.identifier.TableIdentifier;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static com.speedment.enterprise.datastore.runtime.aggregator.Aggregators.*;
import static java.util.stream.Collectors.toMap;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
@RestController
@RequestMapping("/salary/correlation")
public class SalaryCorrelationController {

    private final static TableIdentifier<Salary> TABLE =
        Salary.EMP_NO.identifier().asTableIdentifier();

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
    private final static DeserializeDouble<Salary> DAYS_EMPLOYED =
        multiply(
            minus(
                getAsLongOrElse(Salary.FROM_DATE, 0),
                getAsLongOrElse(Salary.HIRE_DATE, 0)
            ).asDeserializeDouble(),
            constant(1d / SECONDS_IN_A_DAY)
        );

    private @Autowired DataStoreComponent dataStore;

    @Data
    private static class Result {
        @JsonIgnore
        private final long ref;
        private long count;
        private double salary;
        private double daysEmployed;
    }

    @Data
    private static class Result2 {
        @JsonIgnore
        private final long ref;
        private final long count;
        private final double salaryMean;
        private final double daysEmployedMean;
        private double salaryVariance;
        private double daysEmployedVariance;

        @JsonIgnore
        private double covariance;
        private double correlation;

        Result2(Result firstPass) {
            ref              = firstPass.ref;
            count            = firstPass.count;
            salaryMean       = firstPass.salary;
            daysEmployedMean = firstPass.daysEmployed;
        }
    }

    @GetMapping
    Map<Salary.Gender, Result2> get() {
        final EntityStore<Salary> store = entityStore();
        final Aggregator<Result> agg = Aggregator.builder(store, Result::new)
            .withEnumKey(Salary.GENDER)
            .withCount(Result::setCount)
            .withAverage(Salary.SALARY, Result::setSalary)
            .withAverage(DAYS_EMPLOYED, Result::setDaysEmployed)
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
                    .withVariance(
                        DAYS_EMPLOYED,
                        previous.daysEmployed,
                        Result2::setDaysEmployedVariance)
                    .withAverage(covariance(
                            previous.salary,
                            previous.daysEmployed
                        ), Result2::setCovariance)
                    .build()
                    .aggregate(genderFieldCache().equal(
                        gender, Order.ASC, 0, Long.MAX_VALUE)
                    );
                }
            )

            .map(result -> {
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
                return result;
            })

            .collect(toMap(
                result -> store.deserializeAny(result.ref, Salary.GENDER),
                Function.identity()
            ));
    }

    private DeserializeDouble<Salary> covariance(
            double salaryMean,
            double daysEmployedMean) {
        return multiply(
            minus(
                getAsDouble(Salary.SALARY),
                constant(salaryMean)
            ),
            minus(
                DAYS_EMPLOYED,
                constant(daysEmployedMean)
            )
        );
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
