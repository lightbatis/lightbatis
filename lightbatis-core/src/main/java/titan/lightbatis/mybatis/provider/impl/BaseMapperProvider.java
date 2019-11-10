/**
 * 
 */
package titan.lightbatis.mybatis.provider.impl;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.MappedStatement;
import titan.lightbatis.mybatis.LightbatisSQLBuilder;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.keygen.SnowflakeIdKeyGenerator;
import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.meta.GeneratedValueType;
import titan.lightbatis.mybatis.provider.MapperProvider;

import java.util.Set;

/**
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
    public String updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(LightbatisSQLBuilder.updateTable(entityClass, tableName(entityClass)));
        sql.append(LightbatisSQLBuilder.updateSetColumns(entityClass, null, false, false));
        sql.append(LightbatisSQLBuilder.wherePKColumns(entityClass, true));
        return sql.toString();
    }
    
    public String deleteByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(LightbatisSQLBuilder.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(LightbatisSQLBuilder.wherePKColumns(entityClass, true));
        return sql.toString();
    }
    
	public String insert(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		//获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		ColumnMeta logicDeleteColumn = null;//LightbatisSQLBuilder.getLogicDeleteColumn(entityClass);
		processKey(sql, entityClass, ms, columnList);
		sql.append(LightbatisSQLBuilder.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append(LightbatisSQLBuilder.insertColumns(entityClass, false, false, false));
		sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
		for (ColumnMeta column : columnList) {
			if (!column.isInsertable()) {
				continue;
			}
			if (logicDeleteColumn != null && logicDeleteColumn == column) {
				//sql.append(LightbatisSQLBuilder.getLogicDeletedValue(column, false)).append(",");
				continue;
			}
			//优先使用传入的属性值,当原属性property!=null时，用原属性
			//自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
			if (column.isIdentity()) {
				sql.append(LightbatisSQLBuilder.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
			} else {
				//其他情况值仍然存在原property中
				sql.append(LightbatisSQLBuilder.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
			}
			//当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
			if (column.isIdentity()) {
				sql.append(LightbatisSQLBuilder.getIfCacheIsNull(column, column.getColumnHolder() + ","));
			} else {
				//当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
				sql.append(LightbatisSQLBuilder.getIfIsNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
			}
		}
		sql.append("</trim>");
		//System.out.println(sql);
		return sql.toString();
	}

//		public String query() {
//			SQL sql = new SQL();
//			return null;
//		}
	private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<ColumnMeta> columnList){
		for (ColumnMeta column: columnList) {
			if (column.isIdentity()) {
				log.debug("column = " + column.getProperty() + " is identity");
				sql.append(LightbatisSQLBuilder.getBindCache(column));

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
