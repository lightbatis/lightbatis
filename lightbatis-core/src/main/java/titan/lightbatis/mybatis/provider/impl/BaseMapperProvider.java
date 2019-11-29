/**
 * 
 */
package titan.lightbatis.mybatis.provider.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import titan.lightbatis.generator.GeneratedValueType;
import titan.lightbatis.generator.SnowflakeIdKeyGenerator;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.provider.MapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
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
    
	public String insert(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		//获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		processKey(sql, entityClass, ms, columnList);
		try {
			String insertSQL = MybatisScriptFactory.buildInsert(tableName(entityClass), EntityMetaManager.getColumns(entityClass), columnList);
			sql.append(insertSQL);
			log.debug("==========");
			//System.out.println(insertSQL);
			log.debug(insertSQL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sql.toString();
	}

	private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<ColumnMeta> columnList){
		for (ColumnMeta column: columnList) {
			if (column.isIdentity()) {
				log.debug("column = " + column.getProperty() + " is identity");
				//sql.append(LightbatisSQLBuilder.getBindCache(column));
				if (column.getGenerator() != null) {
					if (column.getGenerator().equals(GeneratedValueType.SNOWFLAKE)) {
						//给 MappedStatement 设置自动增长的值
						setKeyGenerator(ms,column, new SnowflakeIdKeyGenerator());
					} else {
						// TODO 其它情况的处理
						log.warn(ms.getId() + " = " + column.getGenerator() + " 类型的自动增长，目前还不支持！！");
					}
				}
			}
		}
	}


}
