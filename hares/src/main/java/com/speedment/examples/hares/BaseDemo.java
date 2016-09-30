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

import com.company.speedment.test.HaresApplicationBuilder;
import com.company.speedment.test.db0.hares.carrot.CarrotManager;
import com.company.speedment.test.db0.hares.hare.HareManager;
import com.company.speedment.test.db0.hares.human.HumanManager;
import com.speedment.plugins.json.JsonBundle;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.REMOVE;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.STREAM;
import static com.speedment.runtime.core.ApplicationBuilder.LogType.UPDATE;
import com.speedment.runtime.core.Speedment;

/**
 *
 * @author pemi
 */
public class BaseDemo {

    protected final Speedment speedment;
    protected final HareManager hares;
    protected final CarrotManager carrots;
    protected final HumanManager humans;

    public BaseDemo() {
        speedment = new HaresApplicationBuilder()
            .withPassword("hare".toCharArray())
            .withSchema("hares")
            .withLoggingOf(STREAM)
            .withLoggingOf(UPDATE)
            .withLoggingOf(REMOVE)
            //.withLoggingOf(APPLICATION_BUILDER)
            .withBundle(JsonBundle.class)
            .build();
        hares = speedment.getOrThrow(HareManager.class);
        carrots = speedment.getOrThrow(CarrotManager.class);
        humans = speedment.getOrThrow(HumanManager.class);

    }

}
