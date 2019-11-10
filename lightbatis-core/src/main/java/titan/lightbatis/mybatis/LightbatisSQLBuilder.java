/**
 * 
 */
package titan.lightbatis.mybatis;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;

/**
 * 增加SQL 的辅助方法
 * 
 * @author lifei
 *
 */
public class LightbatisSQLBuilder {

	private static String getDynamicTableName(Class<?> entityClass, String tableName) {
		return tableName;
	}

	public static String getDynamicTableName(Class<?> entityClass, String tableName, String parameterName) {
		return tableName;
	}

	/**
	 * <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
	 *
	 * @param column
	 * @return
	 */
	public static String getBindCache(ColumnMeta column) {
		StringBuilder sql = new StringBuilder();
		sql.append("<bind name=\"");
		sql.append(column.getProperty()).append("_cache\" ");
		sql.append("value=\"").append(column.getProperty()).append("\"/>");
		return sql.toString();
	}

	/**
	 * <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
	 *
	 * @param column
	 * @return
	 */
	public static String getBindValue(ColumnMeta column, String value) {
		StringBuilder sql = new StringBuilder();
		sql.append("<bind name=\"");
		sql.append(column.getProperty()).append("_bind\" ");
		sql.append("value='").append(value).append("'/>");
		return sql.toString();
	}

	/**
	 * <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
	 *
	 * @param column
	 * @return
	 */
	public static String getIfCacheNotNull(ColumnMeta column, String contents) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"").append(column.getProperty()).append("_cache != null\">");
		sql.append(contents);
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * 如果_cache == null
	 *
	 * @param column
	 * @return
	 */
	public static String getIfCacheIsNull(ColumnMeta column, String contents) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"").append(column.getProperty()).append("_cache == null\">");
		sql.append(contents);
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * 判断自动!=null的条件结构
	 *
	 * @param column
	 * @param contents
	 * @param empty
	 * @return
	 */
	public static String getIfNotNull(ColumnMeta column, String contents, boolean empty) {
		return getIfNotNull(null, column, contents, empty);
	}

	/**
	 * 判断自动==null的条件结构
	 *
	 * @param column
	 * @param contents
	 * @param empty
	 * @return
	 */
	public static String getIfIsNull(ColumnMeta column, String contents, boolean empty) {
		return getIfIsNull(null, column, contents, empty);
	}

	/**
	 * 判断自动!=null的条件结构
	 *
	 * @param entityName
	 * @param column
	 * @param contents
	 * @param empty
	 * @return
	 */
	public static String getIfNotNull(String entityName, ColumnMeta column, String contents, boolean empty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"");
		if (StringUtils.isNotEmpty(entityName)) {
			sql.append(entityName).append(".");
		}
		sql.append(column.getProperty()).append(" != null");
		if (empty && column.getJavaType().equals(String.class)) {
			sql.append(" and ");
			if (StringUtils.isNotEmpty(entityName)) {
				sql.append(entityName).append(".");
			}
			sql.append(column.getProperty()).append(" != '' ");
		}
		sql.append("\">");
		sql.append(contents);
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * 判断自动==null的条件结构
	 *
	 * @param entityName
	 * @param column
	 * @param contents
	 * @param empty
	 * @return
	 */
	public static String getIfIsNull(String entityName, ColumnMeta column, String contents, boolean empty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"");
		if (StringUtils.isNotEmpty(entityName)) {
			sql.append(entityName).append(".");
		}
		sql.append(column.getProperty()).append(" == null");
		if (empty && column.getJavaType().equals(String.class)) {
			sql.append(" or ");
			if (StringUtils.isNotEmpty(entityName)) {
				sql.append(entityName).append(".");
			}
			sql.append(column.getProperty()).append(" == '' ");
		}
		sql.append("\">");
		sql.append(contents);
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * 获取所有查询列，如id,name,code...
	 *
	 * @param entityClass
	 * @return
	 */
	public static String getAllColumns(Class<?> entityClass) {
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		StringBuilder sql = new StringBuilder();
		for (ColumnMeta entityColumn : columnList) {
			sql.append(entityColumn.getColumn()).append(",");
		}
		return sql.substring(0, sql.length() - 1);
	}

	/**
	 * 获取指定表所有查询列，如id,name,code...
	 *
	 * @param entityClass
	 * @return
	 */
	public static String getAllColumns(String tableName, Class<?> entityClass) {
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(tableName, entityClass);

		StringBuilder sql = new StringBuilder();
		for (ColumnMeta entityColumn : columnList) {
			sql.append(entityColumn.getColumn()).append(",");
		}
		return sql.substring(0, sql.length() - 1);
	}

	public static String selectAllColumns(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(getAllColumns(entityClass));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * select xxx,xxx...
	 *
	 * @param entityClass
	 * @return
	 */
	public static String selectAllColumns(String tableName, Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(getAllColumns(tableName, entityClass));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * select xxx,xxx... from table
	 * 
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public static String selectColumns(String tableName, Set<ColumnMeta> columns, String primaryFieldName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if (primaryFieldName != null) {
			sql.append(primaryFieldName).append(",");
		}
		for (ColumnMeta entityColumn : columns) {
			sql.append(entityColumn.getColumn()).append(",");
		}
		sql = new StringBuilder(sql.substring(0, sql.length() - 1));
		sql.append(" ").append(" FROM ").append(tableName).append(" ");
		return sql.toString();
	}

	/**
	 * select count(x)
	 *
	 * @param entityClass
	 * @return
	 */
	public static String selectCount(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		Set<ColumnMeta> pkColumns = EntityMetaManager.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		return sql.toString();
	}

	/**
	 * select case when count(x) > 0 then 1 else 0 end
	 *
	 * @param entityClass
	 * @return
	 */
	public static String selectCountExists(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CASE WHEN ");
		Set<ColumnMeta> pkColumns = EntityMetaManager.getPKColumns(entityClass);
		if (pkColumns.size() == 1) {
			sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
		} else {
			sql.append("COUNT(*) ");
		}
		sql.append(" > 0 THEN 1 ELSE 0 END AS result ");
		return sql.toString();
	}

	/**
	 * from tableName - 动态表名
	 *
	 * @param entityClass
	 * @param defaultTableName
	 * @return
	 */
	public static String fromTable(Class<?> entityClass, String defaultTableName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM ");
		sql.append(getDynamicTableName(entityClass, defaultTableName));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * update tableName - 动态表名
	 *
	 * @param entityClass
	 * @param defaultTableName
	 * @return
	 */
	public static String updateTable(Class<?> entityClass, String defaultTableName) {
		return updateTable(entityClass, defaultTableName, null);
	}

	/**
	 * update tableName - 动态表名
	 *
	 * @param entityClass
	 * @param defaultTableName 默认表名
	 * @param entityName       别名
	 * @return
	 */
	public static String updateTable(Class<?> entityClass, String defaultTableName, String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(getDynamicTableName(entityClass, defaultTableName, entityName));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * delete tableName - 动态表名
	 *
	 * @param entityClass
	 * @param defaultTableName
	 * @return
	 */
	public static String deleteFromTable(Class<?> entityClass, String defaultTableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(getDynamicTableName(entityClass, defaultTableName));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * insert into tableName - 动态表名
	 *
	 * @param entityClass
	 * @param defaultTableName
	 * @return
	 */
	public static String insertIntoTable(Class<?> entityClass, String defaultTableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(getDynamicTableName(entityClass, defaultTableName));
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * insert table()列
	 *
	 * @param entityClass
	 * @param skipId      是否从列中忽略id类型
	 * @param notNull     是否判断!=null
	 * @param notEmpty    是否判断String类型!=''
	 * @return
	 */
	public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (ColumnMeta column : columnList) {
			if (!column.isInsertable()) {
				continue;
			}
			if (skipId && column.isId()) {
				continue;
			}
			if (notNull) {
				sql.append(LightbatisSQLBuilder.getIfNotNull(column, column.getColumn() + ",", notEmpty));
			} else {
				sql.append(column.getColumn() + ",");
			}
		}
		sql.append("</trim>");
		return sql.toString();
	}

	/**
	 * insert-values()列
	 *
	 * @param entityClass
	 * @param skipId      是否从列中忽略id类型
	 * @param notNull     是否判断!=null
	 * @param notEmpty    是否判断String类型!=''
	 * @return
	 */
	public static String insertValuesColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\">");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (ColumnMeta column : columnList) {
			if (!column.isInsertable()) {
				continue;
			}
			if (skipId && column.isId()) {
				continue;
			}
			if (notNull) {
				sql.append(LightbatisSQLBuilder.getIfNotNull(column, column.getColumnHolder() + ",", notEmpty));
			} else {
				sql.append(column.getColumnHolder() + ",");
			}
		}
		sql.append("</trim>");
		return sql.toString();
	}

	/**
	 * update set列
	 *
	 * @param entityClass
	 * @param entityName  实体映射名
	 * @param notNull     是否判断!=null
	 * @param notEmpty    是否判断String类型!=''
	 * @return
	 */
	public static String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<set>");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (ColumnMeta column : columnList) {
			if (!column.isId() && column.isUpdatable()) {
				if (notNull) {
					sql.append(LightbatisSQLBuilder.getIfNotNull(entityName, column,
							column.getColumnEqualsHolder(entityName) + ",", notEmpty));
				} else {
					sql.append(column.getColumnEqualsHolder(entityName) + ",");
				}
			}
		}
		sql.append("</set>");
		return sql.toString();
	}

	/**
	 * where主键条件
	 *
	 * @param entityClass
	 * @return
	 */
	public static String wherePKColumns(Class<?> entityClass, boolean useVersion) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getPKColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (ColumnMeta column : columnList) {
			sql.append(" AND " + column.getColumnEqualsHolder());
		}
		sql.append("</where>");
		return sql.toString();
	}

	/**
	 * where所有列的条件，会判断是否!=null
	 *
	 * @param entityClass
	 * @return
	 */
	public static String whereAllIfColumns(Class<?> entityClass, boolean empty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (ColumnMeta column : columnList) {
			sql.append(getIfNotNull(column, " AND " + column.getColumnEqualsHolder(), empty));
		}
		sql.append("</where>");
		return sql.toString();
	}

	/**
	 * 获取默认的orderBy，通过注解设置的
	 *
	 * @param entityClass
	 * @return
	 */
	public static String orderByDefault(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		String orderByClause = EntityMetaManager.getOrderByClause(entityClass);
		if (orderByClause.length() > 0) {
			sql.append(" ORDER BY ");
			sql.append(orderByClause);
		}
		return sql.toString();
	}

	/**
	 * example支持查询指定列时
	 *
	 * @return
	 */
	public static String exampleSelectColumns(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)\">");
		sql.append("<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">");
		sql.append("${selectColumn}");
		sql.append("</foreach>");
		sql.append("</if>");
		// 不支持指定列的时候查询全部列
		sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)\">");
		sql.append(getAllColumns(entityClass));
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * example支持查询指定列时
	 *
	 * @return
	 */
	public static String exampleCountColumn(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("<choose>");
		sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@hasCountColumn(_parameter)\">");
		sql.append("COUNT(${countColumn})");
		sql.append("</when>");
		sql.append("<otherwise>");
		sql.append("COUNT(0)");
		sql.append("</otherwise>");
		sql.append("</choose>");
		// 不支持指定列的时候查询全部列
		sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasNoSelectColumns(_parameter)\">");
		sql.append(getAllColumns(entityClass));
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * example查询中的orderBy条件，会判断默认orderBy
	 *
	 * @return
	 */
	public static String exampleOrderBy(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"orderByClause != null\">");
		sql.append("order by ${orderByClause}");
		sql.append("</if>");
		String orderByClause = EntityMetaManager.getOrderByClause(entityClass);
		if (orderByClause.length() > 0) {
			sql.append("<if test=\"orderByClause == null\">");
			sql.append("ORDER BY " + orderByClause);
			sql.append("</if>");
		}
		return sql.toString();
	}

	/**
	 * example 支持 for update
	 *
	 * @return
	 */
	public static String exampleForUpdate() {
		StringBuilder sql = new StringBuilder();
		sql.append("<if test=\"@tk.mybatis.mapper.util.OGNL@hasForUpdate(_parameter)\">");
		sql.append("FOR UPDATE");
		sql.append("</if>");
		return sql.toString();
	}

	/**
	 * example 支持 for update
	 *
	 * @return
	 */
	public static String exampleCheck(Class<?> entityClass) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"<bind name=\"checkExampleEntityClass\" value=\"@tk.mybatis.mapper.util.OGNL@checkExampleEntityClass(_parameter, '");
		sql.append(entityClass.getCanonicalName());
		sql.append("')\"/>");
		return sql.toString();
	}

	/**
	 * Example查询中的where结构，用于只有一个Example参数时
	 *
	 * @return
	 */
	public static String exampleWhereClause() {
		return "<if test=\"_parameter != null\">" + "<where>\n"
				+ "  <foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\">\n"
				+ "    <if test=\"criteria.valid\">\n"
				+ "      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n"
				+ "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" + "          <choose>\n"
				+ "            <when test=\"criterion.noValue\">\n" + "              and ${criterion.condition}\n"
				+ "            </when>\n" + "            <when test=\"criterion.singleValue\">\n"
				+ "              and ${criterion.condition} #{criterion.value}\n" + "            </when>\n"
				+ "            <when test=\"criterion.betweenValue\">\n"
				+ "              and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n"
				+ "            </when>\n" + "            <when test=\"criterion.listValue\">\n"
				+ "              and ${criterion.condition}\n"
				+ "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n"
				+ "                #{listItem}\n" + "              </foreach>\n" + "            </when>\n"
				+ "          </choose>\n" + "        </foreach>\n" + "      </trim>\n" + "    </if>\n"
				+ "  </foreach>\n" + "</where>" + "</if>";
	}

	/**
	 * Example-Update中的where结构，用于多个参数时，Example带@Param("example")注解时
	 *
	 * @return
	 */
	public static String updateByExampleWhereClause() {
		return "<where>\n" + "  <foreach collection=\"example.oredCriteria\" item=\"criteria\" separator=\"or\">\n"
				+ "    <if test=\"criteria.valid\">\n"
				+ "      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n"
				+ "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" + "          <choose>\n"
				+ "            <when test=\"criterion.noValue\">\n" + "              and ${criterion.condition}\n"
				+ "            </when>\n" + "            <when test=\"criterion.singleValue\">\n"
				+ "              and ${criterion.condition} #{criterion.value}\n" + "            </when>\n"
				+ "            <when test=\"criterion.betweenValue\">\n"
				+ "              and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n"
				+ "            </when>\n" + "            <when test=\"criterion.listValue\">\n"
				+ "              and ${criterion.condition}\n"
				+ "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n"
				+ "                #{listItem}\n" + "              </foreach>\n" + "            </when>\n"
				+ "          </choose>\n" + "        </foreach>\n" + "      </trim>\n" + "    </if>\n"
				+ "  </foreach>\n" + "</where>";
	}

	/**
	 * 按指定的列名组成 SQL 语句
	 * 
	 * @param entityClass
	 * @param names
	 * @return
	 */
	public static String whereColumns(Class<?> entityClass, String[] names) {
		boolean empty = true;
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		// 获取全部列
		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);

		for (String name : names) {
			Optional<ColumnMeta> option = columnList.stream()
					.filter(column -> column.getColumn().equals(name) || column.getProperty().equals(name)).findFirst();
			ColumnMeta entityColumn = null;
			if (option.isPresent()) {
				entityColumn = option.get();
			}
			// sql.append(getIfNotNull(entityColumn, " " +
			// entityColumn.getColumnEqualsHolder(), empty));
			// if (entityColumn != null) {
			sql.append("AND  " + entityColumn.getColumnEqualsHolder());
			// }
//			if (entityColumn == null ) {
//				System.err.println("================ " + name + " is NULL");
//			}
		}
		sql.append("</where>");
		return sql.toString();
	}
}
