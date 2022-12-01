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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterFactory;
import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.RowState;
import titan.lightbatis.dataset.states.RowStates;
import titan.lightbatis.dataset.exception.ColumnNotFoundRuntimeException;
import titan.lightbatis.dataset.util.ArrayMap;
import titan.lightbatis.dataset.util.CaseInsensitiveMap;
import titan.lightbatis.table.ColumnSchema;


public class DataRowImpl implements DataRow {

    private DataTable table_;

    private CaseInsensitiveMap values_ = new CaseInsensitiveMap();

    private RowState state_ = RowStates.UNCHANGED;

//    private String rowId = null;
    public DataRowImpl(DataTable table) {
        table_ = table;
        initValues();
    }

    private void initValues() {
        for (int i = 0; i < table_.getColumnSize(); ++i) {
            values_.put(table_.getColumnName(i), null);
        }
    }


//    @Override
//    public String setRowId(String rowId) {
//        return this.rowId = rowId;
//    }
//
//    @Override
//    public String getRowId() {
//        return rowId;
//    }

    public Object getValue(int index) {
        return values_.get(index);
    }


    public Object getValue(String columnName)
            throws ColumnNotFoundRuntimeException {

        ColumnSchema column = table_.getColumn(columnName);
        Object obj = values_.get(columnName);
        return obj;
        //return values_.get(column.getColumnIndex());
    }


    public void setValue(String columnName, Object value)
            throws ColumnNotFoundRuntimeException {

        ColumnSchema column = table_.getColumn(columnName);
        values_.put(columnName, convert(value, column));
        modify();

    }


    public void setValue(int index, Object value) {
        ColumnSchema column = table_.getColumn(index);
        Object val = convert(value, column);
        values_.put(column.getColumnName(), val);
        values_.set(index, convert(value, column));
        modify();
    }



    private void modify() {
        if (state_.equals(RowStates.UNCHANGED)) {
            state_ = RowStates.MODIFIED;
        }
    }


    public void remove() {
        state_ = RowStates.REMOVED;
    }


    public DataTable getTable() {
        return table_;
    }


    public RowState getState() {
        return state_;
    }


    public void setState(RowState state) {
        state_ = state;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append("{");
        for (int i = 0; i < values_.size(); ++i) {
            buf.append(getValue(i));
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append('}');
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataRow)) {
            return false;
        }
        DataRow other = (DataRow) o;
        for (int i = 0; i < table_.getColumnSize(); ++i) {
            String columnName = table_.getColumnName(i);
            Object value = values_.get(i);
            Object otherValue = other.getValue(columnName);
            if(value.equals(otherValue)){
                continue;
            }
            return false;
        }
        return true;
    }


    public void copyFrom(Object source) {
        if (source instanceof Map) {
            copyFromMap((Map) source);
        } else if (source instanceof DataRow) {
            copyFromRow((DataRow) source);
        } else {
            copyFromBean(source);
        }

    }

    @Override
    public Map values() {
        return values_;
    }

    private void copyFromMap(Map source) {
        for (Iterator i = source.keySet().iterator(); i.hasNext();) {
            String columnName = (String) i.next();
            if (table_.hasColumn(columnName)) {
                ColumnSchema columnSchema = table_.getColumn(columnName);
                Object value = source.get(columnName);
                setValue(columnName, convert(value, columnSchema));
            }
        }
    }

    private void copyFromRow(DataRow source) {
        for (int i = 0; i < source.getTable().getColumnSize(); ++i) {
            ColumnSchema columnSchema = source.getTable().getColumn(i);

            String columnName = columnSchema.getColumnName();
            if (table_.hasColumn(columnName)) {
                Object value = source.getValue(i);
                setValue(columnName, convert(value, columnSchema));
            }
        }
    }

    private void copyFromBean(Object source) {
//        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(source.getClass());
//        for (int i = 0; i < table_.getColumnSize(); ++i) {
//            String columnName = table_.getColumnName(i);
//            String propertyName = StringUtil.replace(columnName, "_", "");
//            if (beanDesc.hasPropertyDesc(propertyName)) {
//                PropertyDesc pd = beanDesc.getPropertyDesc(propertyName);
//                Object value = pd.getValue(source);
//                setValue(columnName, convertValue(value));
//            }
//        }
    }

//    private Object convertValue(Object value) {
//        if (value == null) {
//            return null;
//        }
//        ColumnType columnType = ColumnTypes.getColumnType(value.getClass());
//        return columnType.convert(value, null);
//    }

    private Object convert(Object value, ColumnSchema columnSchema) {
        if (value == null) {
            return null;
        }
        ConversionService conversionService = ApplicationConversionService.getSharedInstance();
        if (conversionService.canConvert(value.getClass(), columnSchema.getColumnClz())) {
            return conversionService.convert(value, columnSchema.getColumnClz());
        }else {
            Converter converter = ConvertUtils.lookup(value.getClass());
            Object obj =  converter.convert(columnSchema.getColumnClz(), value);
            return obj;
        }
    }
}