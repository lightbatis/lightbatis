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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.Timestamp;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;
import titan.lightbatis.dataset.*;
import titan.lightbatis.dataset.exception.IORuntimeException;
import titan.lightbatis.dataset.util.*;
import titan.lightbatis.table.ColumnSchema;


public class XlsReader implements DataReader, DataSetConstants {
    public static String VERSION_2003 = "2003";
    public static String VERSION_2007 = "2007";

    protected DataSet dataSet;


    protected Workbook workbook;


    protected DataFormat dataFormat;


    protected boolean trimString = true;

    private ExcelOption option = new ExcelOption();

    public XlsReader(String path) {
        this(path, true);
    }


    public XlsReader(String path, boolean trimString) {
        this(ResourceUtil.getResourceAsStream(path), trimString);

    }


    public XlsReader(String dirName, String fileName) {
        this(dirName, fileName, true);
    }


    public XlsReader(String dirName, String fileName, boolean trimString) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName, trimString);
    }


    public XlsReader(File dir, String fileName) {
        this(dir, fileName, true);
    }


    public XlsReader(File dir, String fileName, boolean trimString) {
        this(new File(dir, fileName), trimString, new ExcelOption());
    }


    public XlsReader(File file, ExcelOption option) {
        this(file, true, option);
    }


    public XlsReader(File file, boolean trimString, ExcelOption option) {
        this.trimString = trimString;
        this.option = option;
        try{
            String fileName = file.getName();
            String ext = FileNameUtils.getExtension(fileName);
            String version = VERSION_2007;
            if (".xls".equals(ext.toLowerCase())) {
                version = VERSION_2003;
            }else {
                version = VERSION_2007;
            }
            open(FileInputStreamUtil.create(file), version);
        }catch (Exception ex) {
            try {
                open(FileInputStreamUtil.create(file), VERSION_2003);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public XlsReader(InputStream in) {
        this(in, true);
    }


    public XlsReader(InputStream in, boolean trimString) {
        this.trimString = trimString;
        try{
            open(in, VERSION_2007);
        }catch (Exception ex) {
            try {
                open(in, VERSION_2003);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    protected void open(InputStream in , String version) throws Exception {
        if (version.equals(VERSION_2007)) {
            try {
                workbook = new XSSFWorkbook(in);
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
        if (version.equals(VERSION_2003)) {
            try {
                workbook = new HSSFWorkbook(in);
            } catch (Exception ex) {
                throw ex;
            }
        }
        dataFormat = workbook.createDataFormat();
        dataSet = new DataSetImpl();
        for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
            createTable(workbook.getSheetName(i), workbook.getSheetAt(i));
        }
    }

    public DataSet read() {
        return dataSet;
    }


    protected DataTable createTable(String sheetName, Sheet sheet) {
        DataTable table = dataSet.addTable(sheetName);
        int rowCount = sheet.getLastRowNum();
        if (rowCount > 0) {
            setupColumns(table, sheet);
            setupRows(table, sheet);
        } else if (rowCount == 0) {
            setupColumns(table, sheet);
        }
        return table;
    }


    protected void setupColumns(DataTable table, Sheet sheet) {

        Row nameRow = sheet.getRow(option.getColumnNameStartRow());
        Row valueRow = sheet.getRow(option.getColumnNameStartRow() +1);

        for (int i = 0; i <= Short.MAX_VALUE; ++i) {
            Cell nameCell = nameRow.getCell( i);
            if (nameCell == null) {
                break;
            }

            String columnName = nameCell.getRichStringCellValue().getString();
            if (columnName.length() == 0) {
                break;
            }
            Cell valueCell = null;
            if (valueRow != null) {
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    valueCell = sheet.getRow(j).getCell((short) i);
                    if (valueCell != null
                            && !StringUtils.isEmpty(valueCell.toString())) {
                        break;
                    }
                }
            }
            if (valueCell != null) {
                ColumnSchema columnSchema = getColumnType(valueCell);
                columnSchema.setColumnName(columnName);
//                table.addColumn(columnName, getColumnType(valueCell));
                table.addColumn(columnSchema);
            } else {
                ColumnSchema columnSchema = new ColumnSchema(columnName);
                table.addColumn(columnSchema);
                //table.addColumn(columnName);
            }
        }
        if (table.getColumnSize() > 0) {
            ColumnSchema columnSchema = new ColumnSchema();
            columnSchema.setColumnName("_id");
            columnSchema.setColumnClz(String.class);
            columnSchema.setColumnIndex(table.getColumnSize());
            columnSchema.setWritable(false);
            table.addColumn(columnSchema);
        }
    }


    protected void setupRows(DataTable table, Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            setupRow(table, row,i);
        }
    }


    protected void setupRow(DataTable table, Row row, Integer index) {
        DataRow dataRow = table.addRow();
        for (int i = 0; i < table.getColumnSize(); ++i) {
            Cell cell = row.getCell((short) i);
            Object value = getValue(cell);
            dataRow.setValue(i, value);
        }
        dataRow.setValue("_id", String.valueOf(index));
    }


    public boolean isCellBase64Formatted(Cell cell) {
        CellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        return BASE64_FORMAT.equals(dataFormat.getFormat(dfNum));
    }


    public boolean isCellDateFormatted(Cell cell) {
        CellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        String format = dataFormat.getFormat(dfNum);
        if (StringUtils.isEmpty(format)) {
            return false;
        }
        if (format.indexOf('/') > 0 || format.indexOf('y') > 0
                || format.indexOf('m') > 0 || format.indexOf('d') > 0) {
            return true;
        }
        return false;
    }


    public Object getValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                if (isCellDateFormatted(cell)) {
                    return TimestampConversionUtil.toTimestamp(cell
                            .getDateCellValue());
                }
                final double numericCellValue = cell.getNumericCellValue();
                if (isInt(numericCellValue)) {
                    return new BigDecimal((int) numericCellValue);
                }
                return new BigDecimal(Double.toString(numericCellValue));
            case STRING:
                String s = cell.getRichStringCellValue().getString();
                if (s != null) {
                    s = StringUtil.rtrim(s);
                    if (!trimString && s.length() > 1 && s.startsWith("\"")
                            && s.endsWith("\"")) {
                        s = s.substring(1, s.length() - 1);
                    }
                }
                if ("".equals(s)) {
                    s = null;
                }
                if (isCellBase64Formatted(cell)) {
                    return Base64Util.decode(s);
                }
                return s;
            case BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                return Boolean.valueOf(b);
        default:
            return null;
        }
    }


    protected ColumnSchema getColumnType(Cell cell) {
        ColumnSchema columnSchema = new ColumnSchema();

        switch (cell.getCellType())  {
            case NUMERIC:
                if (isCellDateFormatted(cell)) {
                    columnSchema.setColumnClz(Timestamp.class);
                    columnSchema.setType(JDBCType.TIMESTAMP.getVendorTypeNumber());
                }else {
                    columnSchema.setColumnClz(Long.class);
                    columnSchema.setType(JDBCType.BIGINT.getVendorTypeNumber());
                }
            case BOOLEAN:
                columnSchema.setColumnClz(Boolean.class);
                columnSchema.setType(JDBCType.BOOLEAN.getVendorTypeNumber());
            case STRING:
                if (isCellBase64Formatted(cell)) {
                    columnSchema.setColumnClz(byte[].class);
                    columnSchema.setType(JDBCType.BINARY.getVendorTypeNumber());
                }else if (trimString) {
                    columnSchema.setColumnClz(String.class);
                    columnSchema.setType(JDBCType.VARCHAR.getVendorTypeNumber());
                }else {
                    columnSchema.setColumnClz(String.class);
                    columnSchema.setType(JDBCType.VARCHAR.getVendorTypeNumber());
                }
            default:
                columnSchema.setColumnClz(String.class);
                columnSchema.setType(JDBCType.VARCHAR.getVendorTypeNumber());
        }


        return columnSchema;
    }
//
//    protected ColumnType getColumnType(HSSFCell cell) {
//        switch (cell.getCellType()) {
//        case HSSFCell.CELL_TYPE_NUMERIC:
//            if (isCellDateFormatted(cell)) {
//                return ColumnTypes.TIMESTAMP;
//            }
//            return ColumnTypes.BIGDECIMAL;
//        case HSSFCell.CELL_TYPE_BOOLEAN:
//            return ColumnTypes.BOOLEAN;
//        case HSSFCell.CELL_TYPE_STRING:
//            if (isCellBase64Formatted(cell)) {
//                return ColumnTypes.BINARY;
//            } else if (trimString) {
//                return ColumnTypes.STRING;
//            } else {
//                return ColumnTypes.NOT_TRIM_STRING;
//            }
//        default:
//            return ColumnTypes.STRING;
//        }
//    }


    protected boolean isInt(final double numericCellValue) {
        return ((int) numericCellValue) == numericCellValue;
    }

}
