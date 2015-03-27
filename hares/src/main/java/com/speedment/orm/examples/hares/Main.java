package com.speedment.orm.examples.hares;

import com.company.speedment.orm.test.hare.HareApplication;
import com.company.speedment.orm.test.hare.db0.hares.hare.HareManager;

public class Main {

    public static void main(String[] args) {
        new HareApplication().start();
        HareManager.get().stream().forEachOrdered(System.out::println);
    }

}
