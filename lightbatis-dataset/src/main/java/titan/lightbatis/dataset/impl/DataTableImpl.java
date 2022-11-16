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

import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.states.RowStates;
import titan.lightbatis.dataset.exception.ColumnNotFoundRuntimeException;
import titan.lightbatis.dataset.util.ArrayMap;
import titan.lightbatis.dataset.util.CaseInsensitiveMap;
import titan.lightbatis.dataset.util.StringUtil;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.table.TableSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DataTableImpl implements DataTable {

    private String tableName;

    private List<DataRow> rows = new ArrayList();

    private List removedRows = new ArrayList();

    private ArrayMap columns = new CaseInsensitiveMap();

    private boolean hasMetaData = false;


    public DataTableImpl(String tableName) {
        setTableName(tableName);
    }


    public String getTableName() {
        return tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public int getRowSize() {
        return rows.size();
    }


    public DataRow getRow(int index) {
        return (DataRow) rows.get(index);
    }

    public DataRow getRowById(String rowId)  {
        Optional<DataRow> rowOptional = rows.stream().filter(row-> row.getRowId().equals(rowId)).findFirst();
        if (rowOptional.isPresent()) {
           return rowOptional.get();
        }
        return  null;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public DataRow addRow() {
        DataRow row = new DataRowImpl(this);
        rows.add(row);
        row.setState(RowStates.CREATED);
        return row;
    }


    public int getRemovedRowSize() {
        return removedRows.size();
    }


    public DataRow getRemovedRow(int index) {
        return (DataRow) removedRows.get(index);
    }


    public DataRow[] removeRows() {
        for (int i = 0; i < rows.size();) {
            DataRow row = getRow(i);
            if (row.getState().equals(RowStates.REMOVED)) {
                removedRows.add(row);
                rows.remove(i);
            } else {
                ++i;
            }
        }
        return (DataRow[]) removedRows.toArray(new DataRow[removedRows.size()]);
    }


    public int getColumnSize() {
        return columns.size();
    }

    public ColumnSchema getColumn(int index) {
        return (ColumnSchema) columns.get(index);
    }


    public ColumnSchema getColumn(String columnName) {
        ColumnSchema column = getColumn0(columnName);
        if (column == null) {
            throw new ColumnNotFoundRuntimeException(tableName, columnName);
        }
        return column;
    }

    private ColumnSchema getColumn0(String columnName) {
        ColumnSchema column = (ColumnSchema) columns.get(columnName);
        if (column == null) {
            String name = StringUtil.replace(columnName, "_", "");
            column = (ColumnSchema) columns.get(name);
            if (column == null) {
                for (int i = 0; i < columns.size(); ++i) {
                    String key = (String) columns.getKey(i);
                    String key2 = StringUtil.replace(key, "_", "");
                    if (key2.equalsIgnoreCase(name)) {
                        column = (ColumnSchema) columns.get(i);
                        break;
                    }
                }
            }
        }
        return column;
    }


    public boolean hasColumn(String columnName) {
        return getColumn0(columnName) != null;
    }


    public String getColumnName(int index) {
        return getColumn(index).getColumnName();
    }


    public ColumnSchema addColumn(ColumnSchema column) {
        columns.put(column.getColumnName(), column);
        return column;
    }



    public boolean hasMetaData() {
        return hasMetaData;
    }


    public void setupMetaData(TableSchema tableSchema) {
        List<ColumnSchema> tableSchemaColumns =  tableSchema.getColumns();
        for (int i=0;i<tableSchemaColumns.size();i++){
            ColumnSchema col = tableSchemaColumns.get(i);
           columns.put(col.getColumnName(), col);
        }
        hasMetaData = true;
    }


    public void setupColumns(Class beanClass) {
//        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(beanClass);
//        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
//            PropertyDesc pd = beanDesc.getPropertyDesc(i);
//            addColumn(pd.getPropertyName(), ColumnTypes.getColumnType(pd
//                    .getPropertyType()));
//        }
    }

    public void copyFrom(Object source) {
        if (source instanceof List) {
            copyFromList((List) source);
        } else {
            copyFromBeanOrMap(source);
        }

    }

    private void copyFromList(List source) {
        for (int i = 0; i < source.size(); ++i) {
            DataRow row = addRow();
            row.copyFrom(source.get(i));
            row.setState(RowStates.UNCHANGED);
        }
    }

    private void copyFromBeanOrMap(Object source) {
        DataRow row = addRow();
        row.copyFrom(source);
        row.setState(RowStates.UNCHANGED);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append(tableName);
        buf.append(":");
        for (int i = 0; i < columns.size(); ++i) {
            buf.append(getColumnName(i));
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append("\n");
        for (int i = 0; i < rows.size(); ++i) {
            buf.append(getRow(i) + "\n");
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataTable)) {
            return false;
        }
        DataTable other = (DataTable) o;
        if (getRowSize() != other.getRowSize()) {
            return false;
        }
        for (int i = 0; i < getRowSize(); ++i) {
            if (!getRow(i).equals(other.getRow(i))) {
                return false;
            }
        }
        if (getRemovedRowSize() != other.getRemovedRowSize()) {
            return false;
        }
        for (int i = 0; i < getRemovedRowSize(); ++i) {
            if (!getRemovedRow(i).equals(other.getRemovedRow(i))) {
                return false;
            }
        }
        return true;
    }
}