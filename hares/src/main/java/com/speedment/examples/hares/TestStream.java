/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.examples.hares;

import com.company.speedment.orm.test.hare.HareApplication;
import com.company.speedment.orm.test.hare.db0.hares.hare.Hare;
import com.speedment.util.stream.builder.ReferenceStreamBuilder;
import com.speedment.util.stream.builder.pipeline.BasePipeline;
import com.speedment.util.stream.builder.streamterminator.StreamTerminator;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public class TestStream {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new HareApplication().start();
        
        Hare.stream().forEach(System.out::println);
        

        Stream<String> baseStream = Stream.of("A", "B");

        Stream<String> s = new ReferenceStreamBuilder<>(new BasePipeline<>(() -> baseStream), new StreamTerminator() {
        });
        s.onClose(() -> System.out.println("Really important"));
        s.onClose(() -> System.out.println("Even more important!!!"));
        s.findAny();

//        try (Stream<String> s = new ReferenceStreamBuilder<>(new BasePipeline<>(() -> baseStream), new StreamTerminator() {
//
//            @Override
//            public <T> void forEachOrdered(ReferencePipeline<T> pipeline, Consumer<? super T> action) {
//                throw new Error("FFL");
//            }
//
//        })) {
//
//            s.onClose(() -> System.out.println("Closed"));
//
//            s.forEachOrdered(System.out::println);
//        } finally {
//            System.out.println("One last thing...");
//        }
//
//        try (IntStream ints = IntStream.range(0, 10)) {
//            ints.onClose(() -> System.out.println("Closed"));
//
//            ints.forEach(i -> {
//                System.out.println(i);
//            });
//        }
    }

}
