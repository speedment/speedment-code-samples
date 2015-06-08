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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

name = "hare";

dbms {
    name = 'db0';
    schema { 
        name = 'hares';
        table {
            name = "hare";
            column {
                name = "id";
                mapping = Integer.class;
                nullable = false;
            }
            column {
                name = "name";
                mapping = String.class;
                nullable = false;
            }
            column {
                name = "color";
                mapping = String.class;
                nullable = false;
            }
            column {
                name = "age";
                mapping = Integer.class;
                nullable = false;
            }
            primaryKeyColumn {
                name = "id"
            }
            index {
                name = "MyCustomIndex"
                indexColumn {
                    name = "color"
                }
            }
            index {
                name = "MyCustomDualIndex"
                indexColumn {
                    name = "name"
                }
                indexColumn {
                    name = "color"
                }
            }
        }
        table {
            name = "carrot";
            column {
                name = "id";
                mapping = Integer.class;
                nullable = false;
            }
            column {
                name = "name";
                mapping = String.class;
            }
            column {
                name = "owner";
                mapping = Integer.class;
                nullable = false
            }
            column {
                name = "rival";
                mapping = Integer.class;
            }
            primaryKeyColumn {
                name = "id";
            }
            foreignKey {
                name = "carrot_owner_to_hare_id";
                foreignKeyColumn {
                    name = "owner";
                    foreignTableName = "hare";
                    foreignColumnName = "id";
                }
            }
            foreignKey {
                name = "carrot_rival_to_hare_id";
                foreignKeyColumn {
                    name = "rival";
                    foreignTableName = "hare";
                    foreignColumnName = "id";
                }
            }
        }
        table {
            name = "human";
            column {
                name = "id";
                mapping = Integer.class;
                nullable = false;
            }
            column {
                name = "name";
                mapping = String.class;
                nullable = false;
            }
            primaryKeyColumn {
                name = "id";
            }
        }
    }
}