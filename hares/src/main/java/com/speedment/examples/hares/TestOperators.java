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
import com.company.speedment.test.hare.db0.hares.hare.Hare;
import com.company.speedment.test.hare.db0.hares.hare.HareField;

/**
 *
 * @author pemi
 */
public class TestOperators {
    
    public static void main(String[] args) {
        new HareApplication().start();
        
        Hare.stream().filter(HareField.NAME.contains("er")).forEach(System.out::println);
        Hare.stream().filter(HareField.NAME.contains("eR")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.endsWith("rra")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.equalIgnoreCase("PerRA")).forEach(System.out::println); // Fel
        
        Hare.stream().filter(HareField.NAME.notEqualIgnoreCase("PerRA")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.startsWith("H")).forEach(System.out::println);
        
    }
    
}
