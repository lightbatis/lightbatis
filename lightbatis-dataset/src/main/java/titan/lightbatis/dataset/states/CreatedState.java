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
package titan.lightbatis.dataset.states;

import java.util.ArrayList;
import java.util.List;

import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.table.ColumnSchema;


public class CreatedState extends AbstractRowState {

    public String toString() {
        return "CREATED";
    }

    public SqlContext getSqlContext(DataRow row) {
        DataTable table = row.getTable();
        StringBuffer buf = new StringBuffer(100);
        List argList = new ArrayList();
//        List argTypeList = new ArrayList();
        List<Integer> argTypeList = new ArrayList();
        //buf.append("INSERT INTO ");
        buf.append("REPLACE INTO ");
        buf.append(table.getTableName());
        buf.append(" (");
        int writableColumnSize = 0;
        for (int i = 0; i < table.getColumnSize(); ++i) {
            ColumnSchema column = table.getColumn(i);
            if (column.isWritable()) {
                ++writableColumnSize;
                buf.append(column.getColumnName());
                buf.append(", ");
                argList.add(row.getValue(i));
//                argTypeList.add(column.getColumnClz());
                argTypeList.add(column.getType());
            }
        }
        buf.setLength(buf.length() - 2);
        buf.append(") VALUES (");
        for (int i = 0; i < writableColumnSize; ++i) {
            buf.append("?, ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(")");
        return new SqlContext(buf.toString(), argList.toArray(),argTypeList);
    }
    /*
     * protected String getSql(DataTable table) { String sql = (String)
     * sqlCache.get(table); if (sql == null) { sql = createSql(table);
     * sqlCache.put(table, sql); } return sql; }
     * 
     * private static String createSql(DataTable table) { StringBuffer buf = new
     * StringBuffer(100); buf.append("INSERT INTO ");
     * buf.append(table.getTableName()); buf.append(" ("); int
     * writableColumnSize = 0; for (int i = 0; i < table.getColumnSize(); ++i) {
     * DataColumn column = table.getColumn(i); if (column.isWritable()) {
     * ++writableColumnSize; buf.append(column.getColumnName()); buf.append(",
     * "); } } buf.setLength(buf.length() - 2); buf.append(") VALUES ("); for
     * (int i = 0; i < writableColumnSize; ++i) { buf.append("?, "); }
     * buf.setLength(buf.length() - 2); buf.append(")"); return buf.toString(); }
     * 
     * protected Object[] getArgs(DataRow row) { DataTable table =
     * row.getTable(); List bindVariables = new ArrayList(); for (int i = 0; i <
     * table.getColumnSize(); ++i) { if (table.getColumn(i).isWritable()) {
     * bindVariables.add(row.getValue(i)); } } return bindVariables.toArray(); }
     */
}