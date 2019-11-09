package titan.lightbatis.table;

/**
 * 获取表结构信息的SQL语句构造器
 */
public interface ITableSchemaSQLBuilder {


    /**
     * 构建查询表结构信息的SQL
     *
     * @return
     */
    public String buildTableSQL() ;


    /**
     * 构建查询表的字段信息的SQL
     * @param table
     * @return
     */
    public String buildColumsSQL(String table);

}
