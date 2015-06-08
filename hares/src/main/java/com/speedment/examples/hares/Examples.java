/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.examples.hares;

import com.company.speedment.test.hare.HareApplication;
import com.company.speedment.test.hare.db0.hares.carrot.Carrot;
import com.company.speedment.test.hare.db0.hares.hare.Hare;
import com.company.speedment.test.hare.db0.hares.hare.HareField;
import com.company.speedment.test.hare.db0.hares.hare.HareManager;
import com.company.speedment.test.hare.db0.hares.human.Human;
import com.speedment.core.config.model.Dbms;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author pemi
 */
public class Examples {

    public static void main(String[] args) {

        System.out.println(System.getProperties());

        System.setProperty("speedment.project.hare.db0.password", "MySecretPassword");

        new HareApplication().configureDbmsPassword("MyReallySecretPassword").start();

        HareManager.get().getTable().ancestor(Dbms.class).ifPresent(dbms -> {
            final Optional<String> pw = dbms.getPassword();
            System.out.println(pw);
        });

        //run("Builder", Examples::builderDemo);
        run("Predicate", Examples::predicateDemo);
        run("KeyValue", Examples::keyValueDemo);
        run("Linked", Examples::linkedDemo);
        run("Parallel", Examples::parallelDemo);
        run("Json", Examples::jsonDemo);
        run("Optional", Examples::optionalDemo);
        run("ShortCirtuit", Examples::shortCircuitOfCount);
        //run("Metadata", Examples::metadata);

    }

    private static void builderDemo() {
        // A Builder-pattern can be used to create an entity.
        Optional<Hare> harry = Hare.builder()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist();

        System.out.println(harry);
    }

    private static void predicateDemo() {
        // Large quantities of data is reduced in-memory using predicates.
        List<Hare> oldHares = Hare.stream()
                .filter(h -> h.getAge() > 8)
                .collect(toList());

        List<Hare> oldHares2 = Hare.stream()
                .filter(HareField.AGE.greaterThan(8))
                .collect(toList());

        System.out.println(oldHares);
    }

    private static void keyValueDemo() {
        // Key-value searches are optimised in the background!

        Optional<Hare> harry = Hare.stream()
                .filter(HareField.NAME.equal("Harry"))
                .findAny();

        System.out.println(harry);
    }

    private static void linkedDemo() {
        // Different tables form a traversable graph in memory.
        Optional<Carrot> carrot = Hare.stream()
                .filter(HareField.NAME.equal("Harry"))
                .flatMap(h -> h.carrots()) // Carrot is a foreign key table.
                .findAny();

        System.out.println(carrot);
    }

    private static void optionalDemo() {
        // Just find any carrot that we can use in 
        // this example
        Carrot carrot = Carrot.stream().findAny().get();

        // column "rival" can be null so we will
        // get an Optional for free!
        Optional<Hare> oHare = carrot.findRival();

        System.out.println(oHare);
    }

    private static void shortCircuitOfCount() {
        // Streams can be short circuited so that
        // this will correspond to
        // "select count(*) from hares"
        long noHares = Hare.stream()
                .map(Hare::getAge)
                .sorted()
                .count();

        System.out.println(noHares);
    }

    private static void parallelDemo() {
        // Find all hares that share name with a human using multiple 
        // threads.
        Hare.stream()
                .parallel()
                .filter(h -> Human.stream()
                        .filter(n -> h.getName().equals(n.getName()))
                        .findAny().isPresent()
                ).forEach(System.out::println);
    }

    private static void jsonDemo() {
        // Convert to json
        Hare.stream()
                .map(Hare::toJson)
                .forEach(System.out::println);
    }

    private static void metadata() {
// If an SQL storage engine is used, you may
// want to obtain the actual transaction metadata.
        Optional<Hare> harry = Hare.builder()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist(meta -> {
                    meta.getSqlMetaResult().ifPresent(sql -> {
                        System.out.println("sql = " + sql.getQuery());
                        System.out.println("params = " + sql.getParameters());
                        System.out.println("thowable = " + sql.getThrowable()
                                .map(t -> t.getMessage())
                                .orElse("nothing thrown :-) ")
                        );
                    });
                });
    }

    private static void run(String name, Runnable method) {
        System.out.println();
        System.out.println("*** " + name + " Demo ***");
        method.run();
    }

}
