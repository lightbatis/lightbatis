/**
 * 
 */
package titan.lightbatis.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import titan.lightbatis.mybatis.meta.*;
import titan.lightbatis.result.PageList;

import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import java.sql.*;
import java.util.*;

/**
 * 分页的处理
 * 
 * @author lifei
 *
 */

//@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
@Intercepts(
		{
				@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
				@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
		}
)
@Slf4j
public class PageListInterceptor implements Interceptor {
	public static final String ROW_ROUNDS_KEY = "__RowBounds";
	public static final String COUNT_MSID_KEY = "_COUNT";
	//private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PageListInterceptor.class);

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
	 * Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

//		Object[] args = invocation.getArgs();
//		Object result = invocation.proceed();
//		Object target = invocation.getTarget();
//
//		if (target instanceof ResultSetHandler) {
//			ResultSetHandler handler = (ResultSetHandler) target;
//			MetaObject metaHandler = SystemMetaObject.forObject(handler);
//			MappedStatement ms = (MappedStatement) metaHandler.getValue("mappedStatement");
//			String msId = ms.getId();
//			MapperMeta meta = MapperMetaManger.getMeta(msId);
//			if (meta == null) {
//				return result;
//			}
//			EntityMeta entityMeta = EntityMetaManager.getEntityMeta(msId);
//			if (entityMeta != null) {
//				// 如果涉及到多表操作
//				if (entityMeta.isMultiTable()) {
//					//log.debug("多表操作");
//					Statement stmt = (Statement) args[0];
//					entityPlus(result, entityMeta, stmt.getConnection(), ms);
//				}
//			}
//			Class<?> clz = meta.getResultClz();
//			if (PageList.class.isAssignableFrom(clz)) {
//				BoundSql boundSql = (BoundSql) metaHandler.getValue("boundSql");
//				Statement stmt = (Statement) args[0];
//				int count = getCount(stmt.getConnection(), stmt, boundSql, ms);
//				if (result instanceof List) {
//					List list = (List) result;
//					PageList pList = new PageList<>();
//					pList.setTotalSize(count);
//					pList.addAll(list);
//					return pList;
//				}
//			}
//		}
//
//		return result;

		//
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameter = args[1];

		RowBounds rowBounds = (RowBounds) args[2];
		ResultHandler resultHandler = (ResultHandler) args[3];
		Executor executor = (Executor) invocation.getTarget();
		CacheKey cacheKey;
		BoundSql boundSql;



		//由于逻辑关系，只会进入一次
		if(args.length == 4){
			//4 个参数时
			if (rowBounds != RowBounds.DEFAULT) {
				if (parameter instanceof Map) {
					Map paramMap = (Map) parameter;
					paramMap.put(ROW_ROUNDS_KEY, rowBounds);
				}
			}
			boundSql = ms.getBoundSql(parameter);
			//boundSql.setAdditionalParameter("_LIMIT",10);
			cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
		} else {
			//6 个参数时
			cacheKey = (CacheKey) args[4];
			boundSql = (BoundSql) args[5];
			//boundSql.setAdditionalParameter("_LIMIT",10);
		}

		//检查是否需要分页
		//rowBounds用参数值，不使用分页插件处理时，仍然支持默认的内存分页
		String msId = ms.getId();
		MapperMeta meta = MapperMetaManger.getMeta(msId);
		if (meta == null) {
			List resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
			return resultList;
		}
		List resultList = executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, boundSql);
		Class<?> clz = meta.getResultClz();
		if (PageList.class.isAssignableFrom(clz)) {
			PageList pList = new PageList<>();
			//如果需要查询总条数
			if (meta.isCoutable()) {
				String countMsId = ms.getId()+ COUNT_MSID_KEY;
				MappedStatement countMs = ms.getConfiguration().getMappedStatement(countMsId);
				BoundSql countBoundSql = countMs.getBoundSql(parameter);
				long count = queryCount(executor, countMs, parameter, countBoundSql, rowBounds, resultHandler);
				pList.setTotalSize(new Long(count).intValue());
			}
			pList.addAll(resultList);
			return pList;
		}
		return resultList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {

	}
	private Long queryCount(Executor executor, MappedStatement countMs,
								  Object parameter, BoundSql countBoundSql,
								  RowBounds rowBounds, ResultHandler resultHandler) throws IllegalAccessException, SQLException {

		//创建 count 查询的缓存 key
		CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, countBoundSql);
		//执行 count 查询
		Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
		Long count = (Long) ((List) countResultList).get(0);
		return count;
	}
	/**
	 * 
	 * 基于数据的附加操作，多表的操作
	 * 
	 */
	private void entityPlus(Object result, EntityMeta entityMeta, Connection conn, MappedStatement ms) throws Exception{
		// 检查需要处理哪些附表
		HashMap<String, SecondTableMeta> tables = entityMeta.getSecondTables();
		Iterator<SecondTableMeta> secondIterator = tables.values().iterator();
		for (; secondIterator.hasNext();) {
			SecondTableMeta tableMeta = secondIterator.next();
			// 如果是普通类型，一次性加载数据，并且添加到属性上去
			// 处理一对一的关系的数据
			if (!tableMeta.getPrimitiveColumns().isEmpty()) {
				loadPrimitiveData(conn, result, entityMeta, tableMeta, ms);
			} else { // 如果是 List 类型的数据，处理一对多的关系的数据
				loadListData(conn, result, entityMeta, tableMeta, ms);
			}
		}
	}

	/**
	 * 处理一对一关系的数据 处理主表中字段为普通类型的属性
	 * 
	 * @param conn
	 * @param result
	 * @param entityMeta
	 * @param secondTable
	 * @param ms
	 */
	private void loadPrimitiveData(Connection conn, Object result, EntityMeta entityMeta, SecondTableMeta secondTable,
			MappedStatement ms) {
		// 先从数据库里批量加载数据
		HashMap matchResultMap=new HashMap();
		SecondaryTable tableDesc = entityMeta.getSecondaryTable(secondTable.getTableName());
		PrimaryKeyJoinColumn pkColumns[] = tableDesc.pkJoinColumns();
		if (pkColumns.length != 1) {
			throw new IllegalArgumentException("加载从表的数据，目前只支持一个字段的主外键映射！");
		}
		PrimaryKeyJoinColumn pkCol = pkColumns[0];
		// 主表的关键字
		String primaryKeyField = pkCol.referencedColumnName();
		// 获取主表的值的集合
		ColumnMeta primaryColumn = entityMeta.findColumnByColumn(primaryKeyField);
		Set<Object> values = getEntityValues(result, primaryColumn);

		// 把从表的主键和相关涉及的列组成SQL
		String sql =null;//TODO LightbatisSQLBuilder.selectColumns(secondTable.getTableName(), secondTable.getPrimitiveColumns(),pkCol.name());
		StringBuffer sqlBuffer = new StringBuffer(sql);
		sqlBuffer.append(" WHERE ").append(pkCol.name()).append(" IN (");

		String hodler = StringUtils.repeat("?", ",", values.size());
		sqlBuffer.append(hodler);
		sqlBuffer.append(")");

		sql = sqlBuffer.toString();
		TypeHandlerRegistry handlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();

		PreparedStatement ps = null;
		ResultSet rs = null;
		// 查询从表里的数据
		try {
			ps = conn.prepareStatement(sql);
			Object[] elements = new Object[values.size()];
			elements = values.toArray(elements);
			for (int i = 0; i < elements.length; i++) {
				ps.setObject(i + 1, elements[i]);
			}
			rs = ps.executeQuery();
			// 查询从表的里的数据，循环元素并添加到主表里的元素对应的属性中去。
			while (rs.next()) {
				Object pk = rs.getObject(pkCol.name());
				// 根据关键字查找到指定的元素，这里返回的是主表的元素。
				Object matchItem = matchResultTarget(result, pk, primaryColumn,matchResultMap);
				if (matchItem != null) {
					// 将从表的字段追加到主表的元素里去
					MetaObject target = SystemMetaObject.forObject(matchItem);
					// 从表里的所有的元素。
					for (ColumnMeta col : secondTable.getPrimitiveColumns()) {
						TypeHandler<?> handler = handlerRegistry.getTypeHandler(col.getJavaType());
						// 获取字段值
						Object fieldValue = handler.getResult(rs, col.getColumn());
						// 添加到主表的元素里去。
						target.setValue(col.getProperty(), fieldValue);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	/**
	 * 处理一对多关系的数据
	 * 
	 * @param conn
	 * @param result
	 * @param entityMeta
	 * @param secondTable
	 * @param ms
	 */
	private void loadListData(Connection conn, Object result, EntityMeta entityMeta, SecondTableMeta secondTable,
			MappedStatement ms) throws Exception{
		HashMap matchResultMap=new HashMap();
		System.out.println("处理一对多的关系");
		//log.debug("主从表的处理 主表=" + entityMeta.getName() + " 从表= " + secondTable.getTableName());
		// 先从数据库里批量加载数据
		SecondaryTable tableDesc = entityMeta.getSecondaryTable(secondTable.getTableName());
		PrimaryKeyJoinColumn pkColumns[] = tableDesc.pkJoinColumns();
		if (pkColumns.length != 1) {
			throw new IllegalArgumentException("加载从表的数据，目前只支持一个字段的主外键映射！");
		}
		PrimaryKeyJoinColumn pkCol = pkColumns[0];
		// 主表的关键字
		String primaryKeyField = pkCol.referencedColumnName();
		// 获取主表的值的集合
		ColumnMeta primaryColumn = entityMeta.findColumnByColumn(primaryKeyField);
		Set<Object> values = getEntityValues(result, primaryColumn);
		
		// 获取从表的数据
		ColumnMeta listColumn = secondTable.getListColumn();
		EntityMeta baseMeta = listColumn.getCollectionBaseType();
		
		// 把从表的主键和相关涉及的列组成SQL
		String sql = null;//TODO LightbatisSQLBuilder.selectColumns(baseMeta.getName(), baseMeta.getClassColumns(), null);
		StringBuffer sqlBuffer = new StringBuffer(sql);
		sqlBuffer.append(" WHERE ").append(pkCol.name()).append(" IN (");

		String hodler = StringUtils.repeat("?", ",", values.size());
		sqlBuffer.append(hodler);
		sqlBuffer.append(")");

		sql = sqlBuffer.toString();
		//log.debug(sql);
		TypeHandlerRegistry handlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();

		PreparedStatement ps = null;
		ResultSet rs = null;
		// 查询从表里的数据
		try {
			ps = conn.prepareStatement(sql);
			Object[] elements = new Object[values.size()];
			elements = values.toArray(elements);
			for (int i = 0; i < elements.length; i++) {
				ps.setObject(i + 1, elements[i]);
			}
			rs = ps.executeQuery();

			// 查询从表的里的数据，循环元素并添加到主表里的元素对应的属性中去。
			while (rs.next()) {
				//实例化 List, 这个 List 就是主类中字段的属性类型的，这个地方应该根据类型来构建。
				List items = null;
				Object pk = rs.getObject(pkCol.name());
				// 根据关键字查找到指定的元素，这里返回的是主表的元素,由于是根据关键字查询的，所以这里的返回的值只能是一个。
				Object matchItem = matchResultTarget(result, pk, primaryColumn,matchResultMap);
				if (matchItem != null) {
					// 将从表的字段追加到主表的元素里去
					MetaObject target = SystemMetaObject.forObject(matchItem);
					//找到这个属性的值
					Object masterValue = target.getValue(listColumn.getProperty());
					if (masterValue != null) {
						items = (List) masterValue;
					} else {
						items = new ArrayList();
						target.setValue(listColumn.getProperty(), items);
					}
					Object item = baseMeta.getEntityClass().newInstance();
					MetaObject itemMeta = SystemMetaObject.forObject(item);
					// 从表里的所有的元素。
					for (ColumnMeta col : baseMeta.getClassColumns()) {
						TypeHandler<?> handler = handlerRegistry.getTypeHandler(col.getJavaType());
						// 获取字段值
						Object fieldValue = handler.getResult(rs, col.getColumn());
						// 添加到主表的元素里去。
						itemMeta.setValue(col.getProperty(), fieldValue);
					}
					items.add(item);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	/**
	 * 根据关键字查找结果中指定的元素
	 * 
	 * @param result
	 * @param pkValue
	 * @param primaryColumn
	 * @return
	 */
	/**
	 * 根据关键字查找结果中指定的元素
	 *
	 * @param result
	 * @param pkValue
	 * @param primaryColumn
	 * @return
	 */
	private Object matchResultTarget(Object result, Object pkValue, ColumnMeta primaryColumn,HashMap<Object,Object> matchResultMap) {
		String property = primaryColumn.getProperty();
		if (List.class.isAssignableFrom(result.getClass())) {
			List list = (List) result;
//			return list.stream().filter(new Predicate<Object>() {
//				@Override
//				public boolean test(Object t) {
//					MetaObject target = SystemMetaObject.forObject(t);
//					Object obj = target.getValue(property);
//					return obj.equals(pkValue);
//				}
//			}).findFirst().get();
			if(matchResultMap.size()==0){
				list.forEach(t->{
					matchResultMap.put(SystemMetaObject.forObject(t).getValue(property),t);
				});
			}
			return  matchResultMap.get(pkValue);
		} else {
			MetaObject target = SystemMetaObject.forObject(result);
			Object obj = target.getValue(property);
			if (obj.equals(pkValue)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * 获取结果集指定列（属性）的所有的数据
	 * 
	 * @param result
	 * @param col
	 * @return
	 */
	private Set<Object> getEntityValues(Object result, ColumnMeta col) {
		String property = col.getProperty();
		Set<Object> values = new HashSet<>();
		if (List.class.isAssignableFrom(result.getClass())) {
			List list = (List) result;
			list.forEach(item -> {
				MetaObject target = SystemMetaObject.forObject(item);
				Object obj = target.getValue(property);
				values.add(obj);
			});
		} else {
			MetaObject target = SystemMetaObject.forObject(result);
			Object obj = target.getValue(property);
			values.add(obj);
		}
		return values;
	}

	private int getCount(Connection connection, Statement stmt, BoundSql boundSql, MappedStatement mappedStatement) throws SQLException{
		// 记录总记录数
		String sql = boundSql.getSql();
		String countSql = "select count(0) from (" + sql + ") temp";
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
					boundSql.getParameterMappings(), boundSql.getParameterObject());

			Object object;
			MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(boundSql);
			Map<String,Object> parameters = (Map<String, Object>) metaObject.getValue("additionalParameters");
			for (Map.Entry<String,Object> entry : parameters.entrySet()) {
				countBS.setAdditionalParameter(entry.getKey(), entry.getValue());
			}
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			return totalCount;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				countStmt.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * 代入参数值
	 *
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}
}
