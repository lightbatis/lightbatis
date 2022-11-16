/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package titan.lightbatis.dataset;

import titan.lightbatis.dataset.exception.ColumnNotFoundRuntimeException;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.table.TableSchema;

import java.util.List;


public interface DataTable {


    String getTableName();


    void setTableName(String tableName);


    int getRowSize();


    DataRow getRow(int index);


    DataRow addRow();


    int getRemovedRowSize();


    DataRow getRemovedRow(int index);


    DataRow[] removeRows();

    /**
     * カラム数を返します。
     * 
     * @return カラム数
     */
    int getColumnSize();


    ColumnSchema getColumn(int index);


    ColumnSchema getColumn(String columnName)
            throws ColumnNotFoundRuntimeException;


    boolean hasColumn(String columnName);

    String getColumnName(int index);


    ColumnSchema addColumn(ColumnSchema column);


    boolean hasMetaData();

    void setupMetaData(TableSchema dbMetaData);


    void setupColumns(Class beanClass);


    void copyFrom(Object source);

    DataRow getRowById(String rowId);

    List<DataRow> getRows();
}
