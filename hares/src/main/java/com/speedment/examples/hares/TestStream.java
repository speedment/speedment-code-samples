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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.examples.hares;


import com.speedment.runtime.core.internal.stream.builder.ReferenceStreamBuilder;
import com.speedment.runtime.core.internal.stream.builder.pipeline.PipelineImpl;
import com.speedment.runtime.core.internal.stream.builder.streamterminator.StreamTerminator;
import com.speedment.runtime.core.stream.StreamDecorator;

import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public class TestStream extends BaseDemo {

    public static void main(String[] args) {

    }

    public void test() {

        hares.stream().forEach(System.out::println);

        Stream<String> baseStream = Stream.of("A", "B");

        Stream<String> s = new ReferenceStreamBuilder<>(new PipelineImpl<>(() -> baseStream), new StreamTerminator() {

            @Override
            public StreamDecorator getStreamDecorator() {
                return StreamDecorator.IDENTITY;
            }

        });
        s.onClose(() -> System.out.println("Really important"))
         .onClose(() -> System.out.println("Even more important!!!"))
         .findAny();

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
