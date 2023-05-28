/**
 * 
 */
package titan.lightbatis.table;

import javax.sql.DataSource;
import java.util.List;

/**
 * 表结构管理
 * @author lifei
 *
 */
public interface ITableSchemaManager {

	/**
	 * 查找数据库中指定的表的结构
	 * @param tableName
	 * @return
	 */
	public abstract TableSchema getTable(String tableName);

	/**
	 * 获取当前 DataSource 中所有表结构
	 * @return
	 */
	public abstract List<TableSchema> listTables();

	public abstract List<TableSchema> listTables(DataSource dataSource,String dsName);

	/*
	 * 获取下一个自增长的ID
	 */
	abstract Long nextId();
	
	/**
	 * 批量获取下一个自增长的ID
	 * @return
	 */
	abstract Long[] nextIds(int size);

	static ITableSchemaManager getInstance() {
		return DataSourceTableSchemaManager.manager;
	}
	
}
