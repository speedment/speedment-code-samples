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

/**
 *
 * @author pemi
 */
public class ExamplesNoActiveRecordPattern extends BaseDemo {

//    public static void main(String[] args) {
//
//        ExamplesNoActiveRecordPattern ex = new ExamplesNoActiveRecordPattern();
//
//        run("Builder", ex::builderDemo);
//        run("Predicate", ex::predicateDemo);
//        run("KeyValue", ex::keyValueDemo);
//        run("Linked", ex::linkedDemo);
//        run("Parallel", ex::parallelDemo);
//        run("Json", ex::jsonDemo);
//        run("Optional", ex::optionalDemo);
//        run("ShortCirtuit", ex::shortCircuitOfCount);
//        run("getter", ex::getter);
//        run("setter", ex::setter);
//        run("comparator", ex::comparator);
//        run("advanced predicated", ex::advancedPredicates);
//        //run("Metadata", Examples::metadata);
//        run("update", ex::updateDemo);
//
//    }
//
//    private void builderDemo() {
//        // A Builder-pattern can be used to create an entity.
//        try {
//            Hare harry = hares.newEmptyEntity()
//                .setName("Harry")
//                .setColor("Gray")
//                .setAge(3)
//                .persist(MetadataUtil.toText(System.out::println));
//            System.out.println(harry);
//        } catch (SpeedmentException se) {
//            se.printStackTrace();
//        }
//
//    }
//
//    private void builderDemo2() {
//        // A Builder-pattern can be used to create an entity.
//        try {
//            Hare harry = hares.newEmptyEntity()
//                .setName("Harry")
//                .setColor("Gray")
//                .setAge(3);
//            //.persist(hares, MetadataUtil.toText(System.out::println));
//            //.persist(speedment, MetadataUtil.toText(System.out::println));
//            //
//            // Idea: introduce .map() for an entity. We could also have other functional methods
//            //
//            //.map(hares::persist);
//            System.out.println(harry);
//        } catch (SpeedmentException se) {
//            se.printStackTrace();
//        }
//
//    }
//
//    private void builderDemo3() {
//        // A Builder-pattern can be used to create an entity.
//        try {
//            Hare harry = hares.newEmptyEntity()
//                .setName("Harry")
//                .setColor("Gray")
//                .setAge(3);
//            hares.persist(harry);
//            System.out.println(harry);
//        } catch (SpeedmentException se) {
//            se.printStackTrace();
//        }
//
//    }
//
//    // Decorate an Entity with active record capabilities
//    private void builderDemo4() {
//        // A Builder-pattern can be used to create an entity.
//        try {
//            Hare harry = hares.newEmptyEntity()
//                .setName("Harry")
//                .setColor("Gray")
//                .setAge(3);
//            hares.persist(harry);
//            System.out.println(harry);
//        } catch (SpeedmentException se) {
//            se.printStackTrace();
//        }
//
//    }
//
//    public void predicateDemo() {
//        // Large quantities of data is reduced in-memory using predicates.
//        List<Hare> oldHares = hares.stream()
//            .filter(Hare.AGE.greaterThan(8))
//            //.filter(h -> h.getAge() > 8)
//            .collect(toList());
//
//        List<Hare> oldHares2 = hares.stream()
//            .filter(Hare.AGE.greaterThan(8))
//            .collect(toList());
//
//        System.out.println(oldHares);
//    }
//
//    private void keyValueDemo() {
//        // Key-value searches are optimised in the background!
//
//        Optional<Hare> harry = hares.stream()
//            .filter(Hare.NAME.equal("Harry"))
//            .findAny();
//
//        System.out.println(harry);
//    }
//
//    private void linkedDemo() {
//        // Different tables form a traversable graph in memory.
//        Optional<Carrot> carrot = hares.stream()
//            .filter(Hare.NAME.equal("Harry"))
//            .flatMap(Hare::findCarrots) // Carrot is a foreign key table.
//            .findAny();
//
//        System.out.println(carrot);
//    }
//
//    private void optionalDemo() {
//        // Just find any carrot that we can use in 
//        // this example
//        Carrot carrot = carrots.stream().findAny().get();
//
//        // column "rival" can be null so we will
//        // get an Optional for free!
//        Optional<Hare> oHare = carrot.findRival();
//
//        System.out.println(oHare);
//    }
//
//    private void shortCircuitOfCount() {
//        // Streams can be short circuited so that
//        // this will correspond to
//        // "select count(*) from hares"
////        
////        LoggerManager.getLogger(AsynchronousQueryResultImpl.class).setLevel(Level.DEBUG);
////        
//        long noHares = hares.stream()
//            .map(Hare::getAge)
//            .sorted()
//            .count();
//
//        System.out.println(noHares);
//    }
//
//    private void parallelDemo() {
//        // Find all hares that share name with a human using multiple 
//        // threads.
//        hares.stream()
//            .parallel()
//            .filter(hare -> humans.stream()
//                .filter(Human.NAME.equal(hare.getName()))
//                .findAny().isPresent()
//            ).forEach(System.out::println);
//    }
//
//    private void jsonDemo() {
//        // Export a hare to JSON format
//        String one = humans.newEmptyEntity()
//            .setName("Harry")
//            .toJson();
//
//        // List all hares in JSON format
//        String many = humans.stream()
//            .collect(CollectorUtil.toJson());
//
//        System.out.println("one  = " + one);
//        System.out.println("many = " + many);
//
//        JsonEncoder<Hare> jsonEncoder = JsonEncoder
//            .noneOf(hares)
//            .put(Hare.ID)
//            .put((Hare.NAME));
//
//        String json = hares.stream().collect(CollectorUtil.toJson(jsonEncoder));
//
//        System.out.println("json: " + json);
//
//    }
//
//    private void metadata() {
//
//        // If an SQL storage engine is used, you may set up your own
//        // listener to obtain the actual transaction metadata.
//        Consumer<MetaResult<Hare>> metaListener = meta -> {
//            meta.getSqlMetaResult().ifPresent(sql -> {
//                System.out.println(
//                    "sql = " + sql.getQuery() + "\n"
//                    + "params = " + sql.getParameters() + "\n"
//                    + "thowable = " + sql.getThrowable()
//                    .map(t -> t.getMessage())
//                    .orElse("nothing thrown :-) ")
//                );
//            });
//        };
//
//        Hare harry = hares.newEmptyEntity()
//            .setName("Harry")
//            .setColor("Gray")
//            .setAge(3)
//            .persist(metaListener);
//    }
//
//    private void metadataDebug() {
//
//        // If an SQL storage engine is used, you may set up a
//        // listener to obtain the actual transaction metadata.
//        Hare harry = hares.newEmptyEntity()
//            .setName("Harry")
//            .setColor("Gray")
//            .setAge(3)
//            .persist(MetadataUtil.toText(System.out::println));
//    }
//
//    private void updateDemo() {
//        //Hare hare =
//
//        hares.stream()
//            .filter(Hare.ID.equal(42))
//            .findAny()
//            .ifPresent(h -> h.setAge(h.getAge() + 1).update(MetadataUtil.toText(System.out::println)));
//    }
//
//    private void getter() {
//        hares.stream().map(Hare.ID::get).forEachOrdered(System.out::println);
//    }
//
//    private void setter() {
//        hares.stream().map(Hare.NAME.setTo("jjj")).forEachOrdered(System.out::println);
//    }
//
//    private void comparator() {
//        hares.stream().sorted(
//            Hare.NAME.comparator()
//            .thenComparing(Hare.COLOR.comparator())
//            .thenComparing(Hare.ID.comparator())
//        ).forEachOrdered(System.out::println);
//    }
//
//    private void advancedPredicates() {
//        System.out.println("in ->");
//        hares.stream().filter(Hare.ID.in(1, 2, 10)).forEachOrdered(System.out::println);
//        System.out.println("between ->");
//        hares.stream().filter(Hare.ID.between(3, 5).and(Hare.ID.equal(3))).forEachOrdered(System.out::println);
//    }
//
//    private static void run(String name, Runnable method) {
//        System.out.println();
//        System.out.println("*** " + name + " Demo ***");
//        method.run();
//    }
//
////    public interface HareMutable extends Hare {
////
////        default HareMutable of(Hare hare) {
////            return HareMutableImpl(hare);
////        }
////    }
//
////    public static class HareMutableImpl extends HareImpl implements HareMutable {
////
////        public HareMutableImpl(Hare hare) {
////        }
////
////        @Override
////        protected Speedment speedment() {
////            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////        }
////
////    }
//
//    // Dedicated streams
//    // streamOfImmutable()
//    // Stream<Hare> stream()
//    // Stream<HareMutable> streamOfMutable()
//    // Stream<HareActive> streamOfActive()
//    //
//    // Mapping
//    // stream().map(HareMutable::of).filter(...
//    // stream().map(Hare::mutable).filter(...
//    // stream().map(hares::mutable).filter(... (Responsibility ok)
//    // stream().map(h -> HareActive.of(speedment, h)).filter(...
//    interface FunctionalHare extends Hare, FunctionalEntity<Hare> {
//
//        @Override
//        public FunctionalHare setAge(Integer age);
//
//        @Override
//        public FunctionalHare setColor(String color);
//
//        @Override
//        public FunctionalHare setName(String name);
//
//        @Override
//        public FunctionalHare persist() throws SpeedmentException;
//        
//        
//    }
//
//    // Functional Entity
//    public interface FunctionalEntity<T> {
//
//        Optional<T> filter(Predicate<? super T> predicate);
//
//        <R> R map(Function<? super FunctionalEntity<T>, ? extends R> mapper);
//
//        T peek(Consumer<? super FunctionalEntity<T>> action);
//
//        <R> R collect(Supplier<R> supplier,
//            BiConsumer<R, ? super T> accumulator,
//            BiConsumer<R, R> combiner);
//
//        <R, A> R collect(Collector<? super T, A, R> collector);
//
//        boolean match(Predicate<? super T> predicate);
//
//    }
////
////    private void foo() {
////        Manager<FunctionalHare> hares = null;
////        FunctionalHare hare = null;
////
////        hare.filter(Hare.AGE.greaterThan(1)).ifPresent(System.out::println);
////        
////        FunctionalHare h2 = hares.newEmptyEntity()
////            .setAge(1)
////            .setColor("Red")
////            .setName("Santa")
////            .map(hares::persist);
////        
////            h2.map(fh -> hares.persist(fh));
////        
////
////    }

}
