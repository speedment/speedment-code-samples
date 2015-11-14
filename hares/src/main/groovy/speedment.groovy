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
import com.speedment.config.parameters.*

name = "hares";
packageLocation = "src/main/java";
packageName = "com.company.speedment.test";
enabled = true;
dbms {
    ipAddress = "127.0.0.1";
    name = "db0";
    port = 3306;
    typeName = "MySQL";
    username = "hare";
    enabled = true;
    schema {
        columnCompressionType = ColumnCompressionType.NONE;
        fieldStorageType = FieldStorageType.WRAPPER;
        name = "hares";
        schemaName = "hares";
        storageEngineType = StorageEngineType.ON_HEAP;
        defaultSchema = false;
        enabled = true;
        table {
            columnCompressionType = ColumnCompressionType.INHERIT;
            fieldStorageType = FieldStorageType.INHERIT;
            name = "carrot";
            storageEngineType = StorageEngineType.INHERIT;
            tableName = "carrot";
            enabled = true;
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "id";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = true;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "name";
                typeMapper = com.speedment.internal.core.config.mapper.identity.StringIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "owner";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "rival";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = true;
            }
            primaryKeyColumn {
                name = "id";
                enabled = true;
            }
            index {
                name = "PRIMARY";
                enabled = true;
                unique = true;
                indexColumn {
                    name = "id";
                    orderType = OrderType.ASC;
                    enabled = true;
                }
            }
            index {
                name = "carrot_owner_to_hare_id";
                enabled = true;
                unique = false;
                indexColumn {
                    name = "owner";
                    orderType = OrderType.ASC;
                    enabled = true;
                }
            }
            index {
                name = "carrot_rival_to_hare_id";
                enabled = true;
                unique = false;
                indexColumn {
                    name = "rival";
                    orderType = OrderType.ASC;
                    enabled = true;
                }
            }
            foreignKey {
                name = "carrot_owner_to_hare_id";
                enabled = true;
                foreignKeyColumn {
                    foreignColumnName = "id";
                    foreignTableName = "hare";
                    name = "owner";
                    enabled = true;
                }
            }
            foreignKey {
                name = "carrot_rival_to_hare_id";
                enabled = true;
                foreignKeyColumn {
                    foreignColumnName = "id";
                    foreignTableName = "hare";
                    name = "rival";
                    enabled = true;
                }
            }
        }
        table {
            columnCompressionType = ColumnCompressionType.INHERIT;
            fieldStorageType = FieldStorageType.INHERIT;
            name = "hare";
            storageEngineType = StorageEngineType.INHERIT;
            tableName = "hare";
            enabled = true;
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "id";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = true;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "name";
                typeMapper = com.speedment.internal.core.config.mapper.identity.StringIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "color";
                typeMapper = com.speedment.internal.core.config.mapper.identity.StringIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "age";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            primaryKeyColumn {
                name = "id";
                enabled = true;
            }
            index {
                name = "PRIMARY";
                enabled = true;
                unique = true;
                indexColumn {
                    name = "id";
                    orderType = OrderType.ASC;
                    enabled = true;
                }
            }
        }
        table {
            columnCompressionType = ColumnCompressionType.INHERIT;
            fieldStorageType = FieldStorageType.INHERIT;
            name = "human";
            storageEngineType = StorageEngineType.INHERIT;
            tableName = "human";
            enabled = true;
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "id";
                typeMapper = com.speedment.internal.core.config.mapper.identity.IntegerIdentityMapper.class;
                autoincrement = true;
                enabled = true;
                nullable = false;
            }
            column {
                columnCompressionType = ColumnCompressionType.INHERIT;
                fieldStorageType = FieldStorageType.INHERIT;
                name = "name";
                typeMapper = com.speedment.internal.core.config.mapper.identity.StringIdentityMapper.class;
                autoincrement = false;
                enabled = true;
                nullable = false;
            }
            primaryKeyColumn {
                name = "id";
                enabled = true;
            }
            index {
                name = "PRIMARY";
                enabled = true;
                unique = true;
                indexColumn {
                    name = "id";
                    orderType = OrderType.ASC;
                    enabled = true;
                }
            }
        }
    }
}