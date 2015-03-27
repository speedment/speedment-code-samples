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

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * AbstractIntegrationTest - Abstract IntegrationTest
 *
 * @author Vlad Mihalcea
 */
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Override
    protected String hibernateDialect() {
        return "org.hibernate.dialect.PostgreSQLDialect";
    }

    @Override
    protected DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("speedment-orm-code-samples");
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("admin");
        return dataSource;
    }
}
