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
package com.speedment.orm.examples.hares;

import com.speedment.orm.examples.hares.model.Carrot;
import com.speedment.orm.examples.hares.model.Hare;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * SecondLevelCacheTest - Test to check the 2nd level cache
 *
 * @author Vlad Mihalcea
 */
public class SecondLevelCacheTest
        //extends AbstractIntegrationTest {
        extends AbstractTest {

    @Override
    protected Class<?>[] entities() {
        return new Class<?>[]{
                Hare.class,
                Carrot.class
        };
    }

    @Override
    protected Properties getProperties() {
        Properties properties = super.getProperties();
        properties.put("hibernate.cache.use_second_level_cache", Boolean.TRUE.toString());
        properties.put("hibernate.cache.use_query_cache", Boolean.TRUE.toString());
        properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        return properties;
    }

    @Before
    public void init() {
        super.init();
        doInTransaction(session -> {
            Hare harry = new Hare();
            harry.setName("Harry");
            harry.setColor("Gray");
            harry.setAge(3);

            Hare henrietta = new Hare();
            henrietta.setName("Henrietta");
            henrietta.setColor("White");
            henrietta.setAge(2);

            Hare henry = new Hare();
            henry.setName("Henry");
            henry.setColor("Black");
            henry.setAge(9);

            Carrot bigOne = new Carrot();
            bigOne.setName("The big one");

            Carrot orange = new Carrot();
            orange.setName("Orange");

            Carrot smallOne = new Carrot();
            smallOne.setName("The small");

            Carrot oldRotten = new Carrot();
            oldRotten.setName("The old and rotten");

            harry.addCarrot(bigOne);
            harry.addCarrot(orange);

            henrietta.addCarrot(smallOne);

            henry.addCarrot(oldRotten);

            session.persist(harry);
            session.persist(henrietta);
            session.persist(henry);
        });
    }

    @After
    public void destroy() {
        getSessionFactory().getCache().evictAllRegions();
        super.destroy();
    }

    @Test
    public void test2ndLevelCacheWithGet() {
        doInTransaction(session -> {
            Hare harry = (Hare) session.get(Hare.class, 1L);
        });
        doInTransaction(session -> {
            LOGGER.info("Check entity is cached after load");
            Hare harry = (Hare) session.get(Hare.class, 1L);
        });
    }

    @Test
    public void test2ndLevelCacheWithQuery() {
        doInTransaction(session -> {

            Hare hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "join fetch h.carrots " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();
        });
        doInTransaction(session -> {
            LOGGER.info("Check get entity is cached after query");
            Hare hare = (Hare) session.get(Hare.class, 1L);
        });

        doInTransaction(session -> {
            LOGGER.info("Check query entity is cached after query");
            Hare hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "join fetch h.carrots " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();

            hare.setName("High-Performance Hibernate");
            session.flush();

            LOGGER.info("Check query entity query is invalidated");
            hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();
        });
    }

    @Test
    public void test2ndLevelCacheWithQueryInvalidation() {
        doInTransaction(session -> {
            Hare hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "join fetch h.carrots " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();
        });

        doInTransaction(session -> {
            LOGGER.info("Check query entity is cached after query");
            Hare hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "join fetch h.carrots " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();

            LOGGER.info("Insert a new Hare!");

            Hare hulk = new Hare();
            hulk.setName("Hulk");
            hulk.setColor("Orange");
            hulk.setAge(0);

            session.persist(hulk);
            session.flush();

            LOGGER.info("Check query entity query is invalidated");
            hare = (Hare) session.createQuery(
                    "select h " +
                            "from Hare h " +
                            "where " +
                            "   h.id = :id").setParameter("id", 1L)
                    .setCacheable(true)
                    .uniqueResult();
        });
    }
}
