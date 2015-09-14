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

import com.company.speedment.test.hare.db0.hares.carrot.Carrot;
import com.company.speedment.test.hare.db0.hares.hare.Hare;
import static com.company.speedment.test.hare.db0.hares.hare.Hare.AGE;
import com.speedment.exception.SpeedmentException;
import java.util.List;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;

public class Main extends BaseDemo {

    public static void main(String[] args) {
        new Main().test();
    }

    public void test() {

        hares.stream().forEachOrdered(System.out::println);

        System.out.println("***** Predicates");

        // long oldHares = hares.stream().filter(AGE.greaterThan(8)).mapToInt(Hare::getAge).sorted().count();
        long oldHares = hares.stream().filter(AGE.greaterThan(8)).mapToInt(h->h.getAge().get()).sorted().count();
        System.out.println(oldHares);


        Predicate<Hare> p = Hare.AGE.lessThan(100).and(Hare.COLOR.equal("Gray").and(h -> true));

        final List<Hare> hareList
                = hares.stream()
                .filter(Hare.NAME.equal("Harry"))
                .filter(p)
                .collect(toList());

        hareList.forEach(System.out::println);

        System.out.println("***** FK streams");

        final Hare hare = hareList.get(0);
        hare.findCarrots().forEachOrdered(System.out::println);

        System.out.println("***** FK finders");
        Carrot carrot = carrots.stream().findAny().get();
        System.out.println(carrot.findOwner());
        System.out.println(carrot.findRival());

        System.out.println("***** Count");
        System.out.println(hares.stream().count());

        System.out.println(hares.stream().map(Hare::getAge).sorted().count()); // Yehhaa!

        //System.out.println(Hare.stream().mapToInt(Hare::getAge).sorted().count()); // Yehhaa!
        try {
            Hare harry = hares.newInstance()
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
        } catch (SpeedmentException se) {
            se.printStackTrace();
        }

    }

}
