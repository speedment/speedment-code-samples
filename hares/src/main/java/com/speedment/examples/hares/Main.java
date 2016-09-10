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

import com.company.speedment.test.db0.hares.carrot.Carrot;
import com.company.speedment.test.db0.hares.hare.Hare;
import com.speedment.runtime.exception.SpeedmentException;

import java.util.List;
import java.util.function.Predicate;

import static com.company.speedment.test.db0.hares.hare.generated.GeneratedHare.AGE;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public class Main extends BaseDemo {

    public static void main(String[] args) {
        new Main().test();
    }

    public void test() {

//        try {
//            final Blob blob = hares.createBlob();
//            final Clob clob = hares.createClob();
//            final NClob nClob = hares.createNClob();
//            final SQLXML sqlXml = hares.createSQLXML();
//            System.out.println(blob);
//            System.out.printf("Blob: length: %d \n", blob.length());
//            System.out.println(clob);
//            System.out.printf("Clob: length: %d \n", clob.length());
//            System.out.println(nClob);
//            System.out.printf("nClob: length: %d \n", nClob.length());
//            System.out.println(sqlXml);
//            System.out.printf("sqlXml: string: %s \n", sqlXml.getString());
//        } catch (SQLException sqle) {
//
//        }

//        System.exit(0);

        hares.stream().forEachOrdered(System.out::println);

        hares.stream()
            .map(Hare::getName)
            .map(String::toUpperCase)
            .min(naturalOrder());

        System.out.println("***** Predicates");

        long oldHares = hares.stream().filter(AGE.greaterThan(8)).mapToInt(Hare::getAge).sorted().count();
        //long oldHares = hares.stream().filter(AGE.greaterThan(8)).mapToInt(h->h.getAge().get()).sorted().count();
        System.out.println(oldHares);

        Predicate<Hare> p = AGE.lessThan(100).and(Hare.COLOR.equal("Gray").and(h -> true));

        final List<Hare> hareList
            = hares.stream()
            .filter(Hare.NAME.equal("Harry"))
            .filter(p)
            .collect(toList());

        hareList.forEach(System.out::println);

        System.out.println("***** FK streams");

        final Hare hare = hareList.get(0);
        hares.findCarrots(hare).forEachOrdered(System.out::println);

        System.out.println("***** FK finders");
        Carrot carrot = carrots.stream().findAny().get();
        System.out.println(carrot.findOwner(hares));
        System.out.println(carrot.findRival(hares));

        System.out.println("***** Count");
        System.out.println(hares.stream().count());

        System.out.println(hares.stream().map(Hare::getAge).sorted().count()); // Yehhaa!

        //System.out.println(Hare.stream().mapToInt(Hare::getAge).sorted().count()); // Yehhaa!
        try {
            Hare harry = hares.newEmptyEntity()
                .setName("Harry")
                .setColor("Gray")
                .setAge(3)
                .persist(hares);
        } catch (SpeedmentException se) {
            se.printStackTrace();
        }

    }

}
