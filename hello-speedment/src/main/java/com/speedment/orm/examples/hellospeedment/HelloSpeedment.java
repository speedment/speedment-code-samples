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
package com.speedment.orm.examples.hellospeedment;


import com.company.speedment.orm.test.hello.db0.hellospeedment.image.impl.ImageManagerImpl;
import com.company.speedment.orm.test.hello.db0.hellospeedment.transition.impl.TransitionManagerImpl;
import com.company.speedment.orm.test.hello.db0.hellospeedment.user.impl.UserManagerImpl;
import com.company.speedment.orm.test.hello.db0.hellospeedment.visit.impl.VisitManagerImpl;
import com.speedment.orm.runtime.SpeedmentApplicationLifecycle;

/**
 *
 * @author pemi
 */
public class HelloSpeedment extends SpeedmentApplicationLifecycle<HelloSpeedment> {

    public HelloSpeedment() {
        setConfigDirectoryName("src/main/groovy/");
        setConfigFileName("speedment.groovy");
    }

    @Override
    protected void onInit() {
        loadAndSetProject();
        put(new ImageManagerImpl());
        put(new TransitionManagerImpl());
        put(new UserManagerImpl());
        put(new VisitManagerImpl());
        super.onInit();
    }
}
