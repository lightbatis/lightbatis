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
package titan.lightbatis.dataset.impl;

import titan.lightbatis.dataset.DataSet;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.exception.TableNotFoundRuntimeException;
import titan.lightbatis.dataset.util.ArrayMap;
import titan.lightbatis.dataset.util.CaseInsensitiveMap;
import titan.lightbatis.table.TableSchema;


public class DataSetImpl implements DataSet {

    private ArrayMap tables = new CaseInsensitiveMap();


    public DataSetImpl() {
    }


    public int getTableSize() {
        return tables.size();
    }


    public String getTableName(int index) {
        return getTable(index).getTableName();
    }


    public DataTable getTable(int index) {
        return (DataTable) tables.get(index);
    }


    public boolean hasTable(String tableName) {
        return tables.containsKey(tableName);
    }


    public DataTable getTable(String tableName)
            throws TableNotFoundRuntimeException {

        DataTable table = (DataTable) tables.get(tableName);
        if (table == null) {
            throw new TableNotFoundRuntimeException(tableName);
        }
        return table;
    }


    public DataTable addTable(String tableName) {
        return addTable(new DataTableImpl(tableName));
    }


    public DataTable addTable(DataTable table) {
        tables.put(table.getTableName(), table);
        return table;
    }


    public DataTable removeTable(DataTable table) {
        return removeTable(table.getTableName());
    }

    @Override
    public DataTable addTable(TableSchema schema) {
        DataTableImpl table  = new DataTableImpl(schema.getTableName());
        schema.getColumns().forEach(columnSchema -> {
            table.addColumn(columnSchema);
        });
        return addTable(table);
    }


    public DataTable removeTable(int index) {
        return (DataTable) tables.remove(index);
    }


    public DataTable removeTable(String tableName) {
        DataTable table = (DataTable) tables.remove(tableName);
        if (table == null) {
            throw new TableNotFoundRuntimeException(tableName);
        }
        return table;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        for (int i = 0; i < getTableSize(); ++i) {
            buf.append(getTable(i));
            buf.append("\n");
        }
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataSet)) {
            return false;
        }
        DataSet other = (DataSet) o;
        if (getTableSize() != other.getTableSize()) {
            return false;
        }
        for (int i = 0; i < getTableSize(); ++i) {
            if (!getTable(i).equals(other.getTable(i))) {
                return false;
            }
        }
        return true;
    }
}
