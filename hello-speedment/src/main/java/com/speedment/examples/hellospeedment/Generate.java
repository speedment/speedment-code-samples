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
package com.speedment.examples.hellospeedment;


import com.speedment.Speedment;
import com.speedment.config.Project;
import com.speedment.internal.core.code.MainGenerator;
import com.speedment.internal.core.config.utils.GroovyParser;
import com.speedment.internal.core.platform.SpeedmentFactory;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Emil Forslund
 */
public class Generate {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        final Speedment speedment = SpeedmentFactory.newSpeedmentInstance();
        final Project p = GroovyParser.projectFromGroovy(speedment, Paths.get("src/main/groovy/", "speedment.groovy"));
        final MainGenerator instance = new MainGenerator(speedment);
        instance.accept(p);
        //System.out.println(instance);
    }
}
