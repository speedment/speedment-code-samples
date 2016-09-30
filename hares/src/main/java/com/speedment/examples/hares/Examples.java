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

import com.company.speedment.test.db0.hares.hare.HareImpl;
import com.speedment.plugins.json.JsonComponent;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.field.trait.HasComparableOperators;

import com.company.speedment.test.HaresApplication;
import com.company.speedment.test.HaresApplicationBuilder;
import com.company.speedment.test.db0.hares.carrot.Carrot;
import com.company.speedment.test.db0.hares.hare.Hare;
import com.company.speedment.test.db0.hares.human.Human;
import com.speedment.plugins.json.JsonEncoder;


import static com.speedment.runtime.core.ApplicationBuilder.LogType.PERSIST;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.REMOVE;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.STREAM;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.UPDATE;
import com.speedment.runtime.core.manager.Manager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static java.util.stream.Collectors.toList;


/**
 *
 * @author pemi
 */


public class Examples extends BaseDemo {

    public static void main(String[] args) {

        Examples ex = new Examples();

        run("Builder", ex::builderDemo);
        run("Predicate", ex::predicateDemo);
        run("KeyValue", ex::keyValueDemo);
        run("Finder", ex::backwardFinderDemo);
        run("BackwardFinder", ex::finderDemo);
        run("Parallel", ex::parallelDemo);
        run("Json", ex::jsonDemo);
        run("Optional", ex::optionalDemo);
        run("ShortCirtuit", ex::shortCircuitOfCount);
        run("getter", ex::getter);
        run("setter", ex::setter);
        run("comparator", ex::comparator);
        run("advanced predicated", ex::advancedPredicates);
        run("update", ex::updateDemo);
        run("delete", ex::deleteDemo);
        run("logging", ex::logging);

    }

    private void builderDemo() {
        // A Builder-pattern can be used to create an entity.
        try {

            Hare newHare = new HareImpl();
            newHare.setName("Harry");
            newHare.setColor("Gray");
            newHare.setAge(3);

            Hare persistedHare = hares.persist(newHare);

            System.out.println(persistedHare);
        } catch (SpeedmentException se) {
            se.printStackTrace();
        }

    }

    public void predicateDemo() {
        // Large quantities of data is reduced in-memory using predicates.
        List<Hare> oldHares = hares.stream()
            .filter(Hare.AGE.greaterThan(8))
            .collect(toList());

        System.out.println(oldHares);
    }

    private void keyValueDemo() {
        // Key-value searches are optimised in the background!

        Optional<Hare> harry = hares.stream()
            .filter(Hare.NAME.equal("Harry"))
            .findAny();

        System.out.println(harry);
    }

    private void finderDemo() {
        Optional<Hare> owner = carrots.stream()
            .map(Carrot.OWNER.finder(hares))
            .findAny();

        if (owner.isPresent()) {
            System.out.println(owner.get());
        } else {
            System.out.println("no find");
        }

    }

    private void backwardFinderDemo() {
        // Different tables form a traversable graph in memory.
        Optional<Carrot> carrot = hares.stream()
            .filter(Hare.NAME.equal("Harry"))
            .flatMap(Carrot.OWNER.backwardFinder(carrots)) // Carrot is a foreign key table.
            .findAny();

        if (carrot.isPresent()) {
            System.out.println(carrot.get());
        } else {
            System.out.println("no find");
        }

    }

    private void optionalDemo() {
        // Just find any carrot that we can use in 
        // this example
        Carrot carrot = carrots.stream().findAny().get();

        // column "rival" can be null so we will
        // get an Optional for free!
        Optional<Hare> oHare = carrots.findRival(carrot);

        Hare oHare2 = Carrot.RIVAL.finder(hares).apply(carrot); //??

        //System.out.println(oHare);
    }

    private void shortCircuitOfCount() {
        // Streams can be short circuited so that
        // this will correspond to
        // "select count(*) from hares"
//        
//        LoggerManager.getLogger(AsynchronousQueryResultImpl.class).setLevel(Level.DEBUG);
//        
        long noHares = hares.stream()
            .map(Hare::getAge)
            .sorted()
            .count();

        System.out.println(noHares);
    }

    private void logging() {
        // Streams can be short circuited so that
        // this will correspond to
        // "select count(*) from hares"
        HaresApplication loggingApp = new HaresApplicationBuilder()
            .withPassword("root")
            .withLoggingOf(STREAM)
            .withLoggingOf(PERSIST)
            .withLoggingOf(UPDATE)
            .withLoggingOf(REMOVE)
            .build();

        Manager<Hare> hares = loggingApp.managerOf(Hare.class);

        long noHares = hares.stream()
            .map(Hare::getAge)
            .sorted()
            .count();

        System.out.println("Number of Hares is " + noHares + ", nothing logged since we are shortcutting AbstractDbmsOperationHandler");

        hares.stream()
            .parallel()
            .filter(hare -> humans.stream()
                .filter(Human.NAME.equal(hare.getName()))
                .findAny().isPresent()
            ).forEach(System.out::println);
    }

    private void parallelDemo() {
        // Find all hares that share name with a human using multiple 
        // threads.
        hares.stream()
            .parallel()
            .filter(hare -> humans.stream()
                .filter(Human.NAME.equal(hare.getName()))
                .findAny().isPresent()
            ).forEach(System.out::println);
    }

    private void jsonDemo() {
        final JsonComponent jsonComponent = haresApplication.getOrThrow(JsonComponent.class);

        final JsonEncoder<Hare> jsonEncoder = jsonComponent
            .noneOf(hares)
            .putInt(Hare.ID)
            .put(Hare.NAME);

        final String one = jsonEncoder.apply(
            new HareImpl()
            .setId(42)
            .setName("Harry")
        );

        // List all hares in JSON format
        String many = hares.stream()
            .collect(jsonEncoder.collector());

        System.out.println("one  = " + one);
        System.out.println("many = " + many);

        String json = hares.stream().collect(jsonEncoder.collector());

        System.out.println("json: " + json);

    }

    private void updateDemo() {
        //Hare hare =

        hares.stream()
            .filter(Hare.ID.equal(42))
            .findAny()
            .ifPresent(
                h -> hares.update(h.setAge(h.getAge() + 1))
            );

        hares.stream()
            .filter(Hare.ID.equal(42))
            .findAny()
            .map(h -> Hare.AGE.setTo(h.getAge() + 1).apply(h))
            .ifPresent(hares.updater());

        hares.stream()
            .filter(Hare.ID.equal(42))
            .findAny()
            .map(h -> Hare.AGE.setTo(h.getAge() + 1).apply(h))
            .map(hares.updater());

    }

    private void deleteDemo() {

        hares.stream()
            .sorted(Hare.ID.comparator().reversed())
            .limit(1)
            .forEach(hares.remover());

    }

    @SuppressWarnings("unchecked")
    public Hare getItemByID(int id) {
        final HasComparableOperators<Hare, Integer> pkField = hares.primaryKeyFields()
            .filter(HasComparableOperators.class::isInstance)
            .map(o -> (HasComparableOperators<Hare, Integer>) o)
            .findFirst().get();

        return hares.stream()
            .filter(pkField.equal(id))
            .findAny()
            .orElseThrow(NoSuchElementException::new);

    }

    private void getter() {

        hares.stream()
            .mapToInt(Hare.ID.getter())
            .forEachOrdered(System.out::println);

    }

    private void setter() {
        hares.stream().map(Hare.NAME.setTo("jjj")).forEachOrdered(System.out::println);
    }

    private void comparator() {
        hares.stream().sorted(
            Hare.NAME.comparator()
            .thenComparing(Hare.COLOR.comparator())
            .thenComparing(Hare.ID.comparator())
        ).forEachOrdered(System.out::println);
    }

    private void advancedPredicates() {
        System.out.println("in ->");
        hares.stream().filter(Hare.ID.in(1, 2, 10)).forEachOrdered(System.out::println);
        System.out.println("between ->");
        hares.stream().filter(Hare.ID.between(3, 5).and(Hare.ID.equal(3))).forEachOrdered(System.out::println);
    }

    private static void run(String name, Runnable method) {
        System.out.println();
        System.out.println("*** " + name + " Demo ***");
        method.run();
    }

}
