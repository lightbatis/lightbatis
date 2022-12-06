package titan.lightbatis.dataset.states;

import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.table.ColumnSchema;

import java.util.ArrayList;
import java.util.List;

public class SaveState extends AbstractRowState{
    @Override
    public SqlContext getSqlContext(DataRow row) {
        DataTable table = row.getTable();
        StringBuffer buf = new StringBuffer(100);
        List argList = new ArrayList();
//        List argTypeList = new ArrayList();
        List<Integer> argTypeList = new ArrayList();
        //buf.append("INSERT INTO ");
        buf.append("INSERT INTO ");
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
        buf.append(" ON DUPLICATE KEY UPDATE ");
        for (int i = 0; i < table.getColumnSize(); ++i) {
            ColumnSchema column = table.getColumn(i);
            if (column.isWritable()) {
                buf.append(column.getColumnName());
                buf.append(" = ?, ");
                argList.add(row.getValue(i));
                //argTypeList.add(column.getColumnClz());
                argTypeList.add(column.getType());
            }
        }
        buf.setLength(buf.length() - 2);

        return new SqlContext(buf.toString(), argList.toArray(),argTypeList);
    }
}
