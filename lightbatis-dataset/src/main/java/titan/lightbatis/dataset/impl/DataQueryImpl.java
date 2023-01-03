package titan.lightbatis.dataset.impl;

import titan.lightbatis.dataset.DataQuery;
import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;

import java.util.List;
import java.util.Optional;

public class DataQueryImpl implements DataQuery {
    private DataTable table = null;

    public DataQueryImpl(DataTable table) {
        this.table = table;
    }

    @Override
    public DataRow findBy(String column, Object value) {
        List<DataRow> rows = table.getRows();
        Optional<DataRow> rowOptional = rows.stream().filter((row)->{
           Object val = row.getValue(column);

           if (val != null) {
               String strVal = val.toString();
               String val2 = value.toString();
                if (strVal.equals(val2)) {
                    return true;
                }
               return false;
           }
            return false;
        }).findFirst();
        if (rowOptional.isPresent()) {
            return rowOptional.get();
        }
        return null;
    }

    public void query(String sql) {


    }
}
