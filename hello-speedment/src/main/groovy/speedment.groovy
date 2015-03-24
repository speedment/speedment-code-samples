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
import java.time.LocalDateTime;
import java.sql.Time;
import java.sql.Timestamp;

name = "hello";

dbms {
    name = 'db0';
    schema { 
        name = 'hellospeedment';
        table {
            name = "image";
            column {
                name = "id";
                mapping = Integer.class;
            }
            column {
                name = "title";
                mapping = String.class;
            }
            column {
                name = "description";
                mapping = String.class;
            }
            column {
                name = "author";
                mapping = Integer.class;
            }
            column {
                name = "published";
                //mapping = LocalDateTime.class;
                //mapping = Time.class;
                mapping = Timestamp.class;
            }
            column {
                name = "src";
                mapping = String.class;
            }
            primaryKeyColumn {
                name = "id"
            }
        }
        table {
            name = "transition";
            column {
                name = "from";
                mapping = Integer.class;
            }
            column {
                name = "to";
                mapping = Integer.class;
            }
            primaryKeyColumn {
                name = "from"
            }
            primaryKeyColumn {
                name = "to"
            }
        }
        table {
            name = "user";
            column {
                name = "id";
                mapping = Integer.class;
            }
            column {
                name = "mail";
                mapping = String.class;
            }
            primaryKeyColumn {
                name = "id"
            }
        }
        table {
            name = "visit";
            column {
                name = "id";
                mapping = Integer.class;
            }
            column {
                name = "time";
                //mapping = LocalDateTime.class;
                // mapping = Time.class;
                mapping = Timestamp.class;
            }
            column {
                name = "user";
                mapping = Integer.class;
            }
            column {
                name = "image";
                mapping = String.class;
            }
            primaryKeyColumn {
                name = "id"
            }
        }
    }
}