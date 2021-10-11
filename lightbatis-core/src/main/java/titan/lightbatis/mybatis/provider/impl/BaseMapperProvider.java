/**
 * 
 */
package titan.lightbatis.mybatis.provider.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.SqlSource;
import titan.lightbatis.generator.GeneratedValueType;
import titan.lightbatis.generator.SnowflakeIdKeyGenerator;
import titan.lightbatis.mybatis.ExecuteSqlSource;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.provider.MapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 基础应用的SQL提供
 * @author lifei114@126.com
 *
 */
@Slf4j
public class BaseMapperProvider extends MapperProvider {

	/**
	 * @param mapperClass
	 * @param mapperBuilder
	 */
	public BaseMapperProvider(Class<?> mapperClass, MapperBuilder mapperBuilder) {
		super(mapperClass, mapperBuilder);
	}

	 /**
     * 通过主键更新全部字段
     *
     * @param ms
     */
    public String updateByPrimaryKey(MappedStatement ms) throws IOException {
        Class<?> entityClass = getEntityClass(ms);
		return updateEntityByPrimaryKey(entityClass);
    }
	public String updateEntityByPrimaryKey(Class<?> entityClass) throws IOException {
		String updateSql = MybatisScriptFactory.updateByPrimaryKey(tableName(entityClass),EntityMetaManager.getColumns(entityClass),EntityMetaManager.getPKColumns(entityClass));
		return updateSql;
	}
    public String deleteByPrimaryKey(MappedStatement ms) throws IOException {
        Class<?> entityClass = getEntityClass(ms);
		Set<ColumnMeta> columns = EntityMetaManager.getColumns(entityClass);
		ColumnMeta logicColumn = null;
		for(ColumnMeta column: columns) {
			if (column.isLogicDelete()) {
				logicColumn = column;
				break;
			}
		}
		if (logicColumn == null) {
			String deleteSql = MybatisScriptFactory.deleteByPrimaryKey(tableName(entityClass),EntityMetaManager.getPKColumns(entityClass));
			return deleteSql;
		} else {
			String removeSql = MybatisScriptFactory.removeByPrimaryKey(tableName(entityClass), logicColumn,EntityMetaManager.getPKColumns(entityClass));
			return removeSql;
		}
    }
    public String save(MappedStatement ms) {
		//System.out.println("处理.... save On ");
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, ms, columnList);
		Set<ColumnMeta> updateColumns = BaseMapperProvider.processUpdate(sql, entityClass, columnList);
		try {
			//String insertSQL = MybatisScriptFactory.buildInsert(tableName(entityClass), EntityMetaManager.getColumns(entityClass), columnList);
			String insertSQL = MybatisScriptFactory.buildSave(tableName(entityClass), insertColumns, insertColumns, updateColumns);
			sql.append(insertSQL);
			//log.debug("==========");
			//System.out.println(insertSQL);
			//log.debug(insertSQL);
//			ExecuteSqlSource sqlSource = new ExecuteSqlSource(ms.getConfiguration(), null, entityClass, tableName(entityClass));
//			return sqlSource;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return sql.toString();
	}
	public String insert(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		//获取全部列

		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		Set<ColumnMeta> insertColumns = processKey(sql, entityClass, ms, columnList);
		try {
			//String insertSQL = MybatisScriptFactory.buildInsert(tableName(entityClass), EntityMetaManager.getColumns(entityClass), columnList);
			String insertSQL = MybatisScriptFactory.buildInsert(tableName(entityClass), insertColumns, insertColumns);
			sql.append(insertSQL);
			//log.debug("==========");
			//System.out.println(insertSQL);
			//log.debug(insertSQL);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return sql.toString();
	}

	public String get(MappedStatement ms) {
    	StringBuilder sql = new StringBuilder();
		Class<?> entityClass = getEntityClass(ms);
		String table = tableName(entityClass);
		String msId = ms.getId();
		try {
			String selectSQL = MybatisScriptFactory.buildSelectSQL(msId, table, EntityMetaManager.getPKColumns(entityClass), null);
			return selectSQL;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Set<ColumnMeta> processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<ColumnMeta> columnList){
		Set<ColumnMeta> insertColumns = new HashSet<>();
		for (ColumnMeta column: columnList) {
			if (column.isIdentity()) {
				log.debug("column = " + column.getProperty() + " is identity");
				//sql.append(LightbatisSQLBuilder.getBindCache(column));
				if (column.getGenerator() != null) {
					if (column.getGenerator().equals(GeneratedValueType.SNOWFLAKE)) {
						//给 MappedStatement 设置自动增长的值
						setKeyGenerator(ms,column, new SnowflakeIdKeyGenerator());
						insertColumns.add(column);
					} else if (column.getGenerator().equals("JDBC")) {
						setKeyGenerator(ms,column, new Jdbc3KeyGenerator());
					}else {
						insertColumns.add(column);
						// TODO 其它情况的处理
						log.warn(ms.getId() + " = " + column.getGenerator() + " 类型的自动增长，目前还不支持！！");
					}
				}
			}else {
				insertColumns.add(column);
			}
		}
		return insertColumns;
	}

	public static Set<ColumnMeta> processKey(StringBuilder sql, Class<?> entityClass, Set<ColumnMeta> columnList){
		Set<ColumnMeta> insertColumns = new HashSet<>();
		for (ColumnMeta column: columnList) {
			if (column.isIdentity()) {
				log.debug("column = " + column.getProperty() + " is identity");
				//sql.append(LightbatisSQLBuilder.getBindCache(column));
				if (column.getGenerator() != null) {
					if (column.getGenerator().equals(GeneratedValueType.SNOWFLAKE)) {
						//给 MappedStatement 设置自动增长的值
						insertColumns.add(column);
					} else if (column.getGenerator().equals("JDBC")) {
					}else {
						insertColumns.add(column);
						// TODO 其它情况的处理
					}
				}
			}else {
				insertColumns.add(column);
			}
		}
		return insertColumns;
	}

	public static Set<ColumnMeta> processUpdate(StringBuilder sql, Class<?> entityClass, Set<ColumnMeta> columnList) {
		Set<ColumnMeta> insertColumns = new HashSet<>();
		for (ColumnMeta column: columnList) {
			if (column.isIdentity()) {

			}else {
				insertColumns.add(column);
			}
		}
		return insertColumns;
	}
}
