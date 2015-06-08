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
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) {

        new HareApplication().start();
        HareManager.get().stream().forEachOrdered(System.out::println);

        System.out.println("***** Predicates");

        Predicate<Hare> p = HareField.AGE.lessThan(100).and(HareField.COLOR.equal("Gray").and(h -> true));

        final List<Hare> hares
                = Hare.stream()
                .filter(HareField.NAME.equal("Harry"))
                .filter(p)
                .collect(toList());

        hares.forEach(System.out::println);

        System.out.println("***** FK streams");

        final Hare hare = hares.get(0);
        hare.carrots().forEachOrdered(System.out::println);

        System.out.println("***** FK finders");
        Carrot carrot = Carrot.stream().findAny().get();
        System.out.println(carrot.findOwner());
        System.out.println(carrot.findRival());

        System.out.println("***** Count");
        System.out.println(Hare.stream().count());

        System.out.println(Hare.stream().map(Hare::getAge).sorted().count()); // Yehhaa!

        //System.out.println(Hare.stream().mapToInt(Hare::getAge).sorted().count()); // Yehhaa!
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
                                .orElse("nothing thrown"));
                    });
                });

    }

}
