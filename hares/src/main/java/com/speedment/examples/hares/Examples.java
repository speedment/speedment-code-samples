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

import com.company.speedment.orm.test.hare.HareApplication;
import com.company.speedment.orm.test.hare.db0.hares.carrot.Carrot;
import com.company.speedment.orm.test.hare.db0.hares.hare.Hare;
import com.company.speedment.orm.test.hare.db0.hares.hare.HareField;
import com.company.speedment.orm.test.hare.db0.hares.human.Human;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author pemi
 */
public class Examples {

    public static void main(String[] args) {
        new HareApplication().start();

        //run("Builder", Examples::builderDemo);
        run("Predicate", Examples::predicateDemo);
        run("KeyValue", Examples::keyValueDemo);
        run("Linked", Examples::linkedDemo);
        run("Parallel", Examples::parallelDemo);
        run("Json", Examples::jsonDemo);

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
                .filter(h -> h.getAge().filter(a -> a > 8).isPresent())
                .collect(toList());
        
        List<Hare> oldHares2 = Hare.stream()
                .filter(HareField.AGE.greaterThan(8))
                .collect(toList());

        System.out.println(oldHares);
    }

    private static void keyValueDemo() {
        // Key-value searches are optimised in the background!
        
        
        Optional<Hare> harry = Hare.stream()
                .filter(h -> h.getName().filter("Harry"::equals).isPresent())
                .findAny();

        System.out.println(harry);
    }

    private static void linkedDemo() {
        // Different tables form a traversable graph in memory.
        Optional<Carrot> carrot = Hare.stream()
                .filter(h -> h.getName().filter("Harry"::equals).isPresent())
                .flatMap(h -> h.carrots()) // Carrot is a foreign key table.
                .findAny();

        System.out.println(carrot);
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

    private static void run(String name, Runnable method) {
        System.out.println();
        System.out.println("*** " + name + " Demo ***");
        method.run();
    }

}
