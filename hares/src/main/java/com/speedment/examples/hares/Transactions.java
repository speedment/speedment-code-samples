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


import com.company.speedment.test.db0.hares.hare.Hare;
import com.speedment.runtime.core.field.ComparableField;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author pemi
 */
public class Transactions extends BaseDemo {

    public static void main(String[] args) {

        Transactions ex = new Transactions();

        //run("Builder", ex::builderDemo);
        
        run("Predicate", ex::predicateDemo);
        run("KeyValue", ex::keyValueDemo);
        run("ShortCirtuit", ex::shortCircuitOfCount);
        run("getter", ex::getter);
        run("setter", ex::setter);
        run("comparator", ex::comparator);
        run("advanced predicated", ex::advancedPredicates);

    }
    
    /*
    

    class TransactionException extends RuntimeException { // Should extends SpeedmentException

    }

//    interface TransactionManager<T> extends Manager<T> {
//        
//    }
   */
//    interface Transaction /*extends AutoCloseable*/ {
//
//        void begin();
//
//        void commit();
//
//        void rollback();
//
////        @Override
////        public void close();
//
//        //Alt 2
//        <T, M extends Manager<T>> M manager(M mgr);
//        
//        <T, M extends Manager<T>> M managerOf(Class<T> entityClass);
//
//    }

//    private interface Callback<T, E extends Throwable> {
//
//        void accept(T t) throws E;
//    }

    /*
    
    interface TransactionHandler {

        void invoke(Consumer<Transaction> action) throws InterruptedException, ExecutionException;

        <R> R invoke(Function<Transaction, R> mapper) throws InterruptedException, ExecutionException;

        <R> R invoke(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException;

        CompletableFuture<Void> submit(Consumer<Transaction> action);

        <R> CompletableFuture<R> submit(Function<Transaction, R> mapper);

        interface Builder extends TransactionHandler {

            // Default is (tx) -> tx.begin();
            <E extends Throwable> Builder withIntializer(Consumer<Transaction> iniitializer);

            // Default is (tx) -> tx.commit();
            Builder withFinisher(Consumer<Transaction> finisher);

            // Default is (tx, ex) -> tx.rollback();
            <E extends Throwable> Builder withExeptionHandler(BiConsumer<Transaction, E> handler);

            // Default is in the same thread
            Builder withExecutor(ExecutorService executor);

            TransactionHandler build();

            @Override
            public default void invoke(Consumer<Transaction> action) throws InterruptedException, ExecutionException {
                build().invoke(action);
            }

            @Override
            public default <R> R invoke(Function<Transaction, R> mapper) throws InterruptedException, ExecutionException {
                return build().invoke(mapper);
            }

            @Override
            public default <R> R invoke(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return build().invoke(mapper, timeout, timeUnit);
            }

            @Override
            public default CompletableFuture<Void> submit(Consumer<Transaction> action) {
                return build().submit(action);
            }

            @Override
            public default <R> CompletableFuture<R> submit(Function<Transaction, R> mapper) {
                return build().submit(mapper);
            }

        }

    }

    interface TransactionComponent {
//        // Alt 1
//        extends Supplier<Transaction> {

        // Alt 2
//        CompletableFuture<Void> transaction(Consumer<Transaction> tx);
//
//        <R> CompletableFuture<R> transaction(Function<Transaction, R> mapper);
//
//        <E extends Throwable> void transactionE(Callback<Transaction, E> mapper) throws E;
        //<E extends Throwable> void transactionE(Callback<Transaction, E> mapper) throws E;
        default TransactionHandler transaction() {
            return builder().build();
        }

        TransactionHandler.Builder builder();

    };
    */
    
    
    /*

    private void builderDemo() {
        // A Builder-pattern can be used to create an entity.
        try {
            Hare harry = hares.newEmptyEntity()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist(MetadataUtil.toText(System.out::println));
            System.out.println(harry);
        } catch (SpeedmentException se) {
            se.printStackTrace();
        }

        TransactionComponent txs = null; // speedment.getTransactionSupplierComponent();

        try (Transaction tx = txs.get()) {
            Hare harry = hares.newEmptyEntity()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist();
        } catch (SpeedmentException se) {
            // tx.r   tx. not reachable here...
        }

        Transaction tx = txs.get();
        try {
            Hare harry = hares.newEmptyEntity()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist();
        } catch (SpeedmentException se) {
            tx.rollback();
        }

    }

    private void builderDemo2() {

        TransactionComponent txs = null; // speedment.getTransactionSupplierComponent();

        txs.transaction(tx -> {
            tx.manager(hares).newEmptyEntity()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist();

            return true;

            // Implicite commit executed here
        });
        // If an exception is thrown (any), the transaction is implicitely rolled back

//        try {
//            txs.transaction(tx -> {
//                tx.manager(hares).newEmptyEntity()
//                    .setName("Harry")
//                    .setColor("Gray")
//                    .setAge(3)
//                    .persist();
//
//                throw new IOException("Fogg");
//            });
//        } catch (IOException ioe) {
//
//        }
        try {
            txs.builder()
                .withIntializer(tx -> {
                    tx.begin();
                    System.out.println("BEGIN");
                })
                .withFinisher(tx -> {
                    tx.commit();
                    System.out.println("COMMIT");
                })
                .withExeptionHandler((tx, e) -> {
                    tx.commit();
                    System.out.println("ROLLBACK " + e.getMessage());
                }).invoke(tx -> {
                    tx.manager(hares).
            });
        } catch (ExecutionException | InterruptedException e) {

        }

    }
*/
    public void predicateDemo() {
        // Large quantities of data is reduced in-memory using predicates.
        List<Hare> oldHares = hares.stream()
            .filter(Hare.AGE.greaterThan(8))
            //.filter(h -> h.getAge() > 8)
            .collect(toList());

        List<Hare> oldHares2 = hares.stream()
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





    private void getter() {
        hares.stream().mapToInt(Hare.ID::getAsInt).forEachOrdered(System.out::println);
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
