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


import com.company.speedment.orm.test.hello.db0.hellospeedment.Image;
import com.company.speedment.orm.test.hello.db0.hellospeedment.ImageManager;
import java.time.LocalDateTime;

/**
 *
 * @author Emil Forslund
 */
public class Main {

    public static void main(String[] args) {
        new HelloSpeedment().start();
        
        Image img = ImageManager.get().builder()
            .setAuthor(3)
            .setTitle("An image")
            .setDescription("A really nice image.")
            .setPublished(LocalDateTime.now())
            .setSrc("http://www.example.com/img.jpg");
        
        ImageManager.get().persist(img);
        
        long bilderAvFem = ImageManager.get().stream().filter(i -> i.getAuthor() == 5).count();
        System.out.println(bilderAvFem);     
    }
}