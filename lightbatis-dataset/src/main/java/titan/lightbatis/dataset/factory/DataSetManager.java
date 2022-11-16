package titan.lightbatis.dataset.factory;

import titan.lightbatis.dataset.DataQuery;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.impl.DataQueryImpl;

import java.util.HashMap;

public class DataSetManager {
    private static Object lock = new Object();
    private static DataSetManager instance = null;

    private HashMap<String, DataQueryImpl> tableQuery = new HashMap<>();
    private DataSetManager() {

    }

    public static DataSetManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new DataSetManager();
            }
        }
        return instance;
    }

    public DataQuery getDataQuery(DataTable table) {
        if (tableQuery.containsKey(table.getTableName())) {
            return tableQuery.get(table.getTableName());
        }
        DataQueryImpl dataQuery = new DataQueryImpl(table);
        tableQuery.put(table.getTableName(), dataQuery);
        return dataQuery;
    }
}
