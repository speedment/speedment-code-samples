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
import com.company.speedment.test.hare.db0.hares.human.Human;
import com.speedment.Manager;
import com.speedment.Speedment;

/**
 *
 * @author pemi
 */
public class BaseDemo {

    protected final Speedment speedment;
    protected final Manager<Hare> hares;
    protected final Manager<Carrot> carrots;
    protected final Manager<Human> humans;

    public BaseDemo() {
        speedment = new HareApplication().build();
        hares = speedment.managerOf(Hare.class);
        carrots = speedment.managerOf(Carrot.class);
        humans = speedment.managerOf(Human.class);
    }

}
