package titan.lightbatis.table;

public class MySqlTableSchemaSQLBuilder implements ITableSchemaSQLBuilder {
    /**
     * 构建查询表结构信息的SQL
     *
     * @return
     */
    @Override
    public String buildTableSQL() {
        return "show table status";
    }

    /**
     * 构建查询表的字段信息的SQL
     *
     * @param table
     * @return
     */
    @Override
    public String buildColumsSQL(String table) {
        return "show full columns from " + table;
    }
}
