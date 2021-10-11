/**
 * 
 */
package titan.lightbatis.mybatis;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.springframework.beans.BeanUtils;
import titan.lightbatis.mybatis.interceptor.PageListInterceptor;
import titan.lightbatis.mybatis.meta.*;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author lifei114@126.com
 *
 */
@Slf4j
public class LightbatisSqlSource implements SqlSource {
	private final Configuration configuration;
	private final MapperMeta mapperMeta;
	protected Class<?> entityClass;
	private QEntity queryEntity ;
	@Getter
	@Setter
	private String tableName;

	//private QueryDslBuilder dslBuilder = null;
	private boolean forCountRow = false;
	private SqlCommandType commandType = null;
	/**
	 *
	 */
	public LightbatisSqlSource(Configuration configuration, MapperMeta mapperMeta, boolean forCountRow) {
		this.configuration = configuration;
		this.mapperMeta = mapperMeta;

		this.forCountRow = forCountRow;
		this.commandType = SqlCommandType.SELECT;
	}

	public LightbatisSqlSource(Configuration configuration, MapperMeta meta, SqlCommandType commandType) {
		this.configuration = configuration;
		this.mapperMeta = meta;

		this.forCountRow = false;
		this.commandType = commandType;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
//		if (SqlCommandType.INSERT == this.commandType) {
//			return insert(parameterObject);
//		}
		QueryDslBuilder dslBuilder = new QueryDslBuilder(configuration);
		HashMap<String, Object> paramMap = null;
		boolean paramIsObject = false;
		//如果是简单的类型
		if (parameterObject != null &&  SimpleTypeRegistry.isSimpleType(parameterObject.getClass())) {
			paramMap = new HashMap<>();
			for (ParamMeta param: mapperMeta.getPredicateParams()) {
				paramMap.put(param.getName(), parameterObject);
			}
			// 如果参数是 Map 的类型
		} else if (parameterObject instanceof Map) {
			paramMap =  (HashMap<String, Object>) parameterObject;
			//参数是 Object 的类型
		}

		dslBuilder.from(this.queryEntity);

		Set<ParamMeta> predicateParams = mapperMeta.getPredicateParams();
		EntityMeta entityMeta = EntityMetaManager.getEntityMeta(entityClass);
		if (!predicateParams.isEmpty()) {
			for (ParamMeta param: predicateParams) {
				if (Predicate.class.isAssignableFrom(param.getType()) || Predicate[].class.isAssignableFrom(param.getType())) {
					Predicate[] predicates = null;
					if (param.getType().isArray()) {
						if (mapperMeta.getParamCount() == 1 || mapperMeta.getPredicateParams().size() == 1) {
							if (paramMap.containsKey(param.getName())) {
								predicates = (Predicate[]) paramMap.get(param.getName());
							} else {
								predicates = (Predicate[])paramMap.get("array");
							}
							paramMap.put(param.getName(),predicates);
						} else {
							predicates = (Predicate[]) paramMap.get(param.getName());
						}
					} else {
						predicates = new Predicate[1];
						//如果参数是一个 Predicate 的对象
						if (paramMap == null) {
							Predicate pt = (Predicate) parameterObject;
							predicates[0] = pt;
						} else {
							predicates[0] = (Predicate) paramMap.get(param.getName());
						}
					}
					dslBuilder.where(predicates);
				}
				else if (param.getType().equals(Object.class) && entityClass.isAssignableFrom(parameterObject.getClass())) {
					// 如果输入的是实体对象类型本身，按值不空的方式进行查询。
					BeanMap bean = new BeanMap(parameterObject);
					Set<ColumnMeta> columnMetaSet = entityMeta.getClassColumns();
					for(ColumnMeta col: columnMetaSet) {
						String property = col.getProperty();
						Object value = bean.get(property);
						//将值不为 null 的添加到查询条件
						if (value != null) {
							Path path = this.queryEntity.getPath(property);
							ComparableExpressionBase comparablePath = (ComparableExpressionBase)path;
							dslBuilder.where(comparablePath.eq(value));
						}
					}
				}
				else {
					ColumnMeta columnMeta = entityMeta.findColumnByProperty(param.getName());
//					Object paramValue = null;
//					if (paramMap.containsKey(param.getName())) {
//						paramValue = paramMap.get(param.getName());
//					}
//					if (paramValue != null && columnMeta == null) {
//						System.out.println(paramValue.getClass());
//					}
					//如果 没有找到这一列，录入的值有，且类型是 实体类型，则说明输入是一个实体对象。
					Object obj = paramMap.get(param.getName());
					if (columnMeta == null) {
						if (paramMap.containsKey(param.getName()) && ( entityClass.isAssignableFrom(paramMap.get(param.getName()).getClass()))) {
							Object entityObject = paramMap.get(param.getName());
							BeanMap bean = new BeanMap(entityObject);
							Set<ColumnMeta> columnMetaSet = entityMeta.getClassColumns();
							for(ColumnMeta col: columnMetaSet) {
								String property = col.getProperty();
								Object value = bean.get(property);
								//将值不为 null 的添加到查询条件
								if (value != null) {
									Path path = this.queryEntity.getPath(property);
									ComparableExpressionBase comparablePath = (ComparableExpressionBase)path;
									dslBuilder.where(comparablePath.eq(value));
								}
							}
						} else {
//							Iterator<String> keyIterator = paramMap.keySet().iterator();
//							for (;keyIterator.hasNext();) {
//								String key = keyIterator.next();
//
//							}
							Set<ColumnMeta> columnMetaSet = entityMeta.getClassColumns();
							for(ColumnMeta col: columnMetaSet) {
								String property = col.getProperty();
								Object value = paramMap.get(property);
								//将值不为 null 的添加到查询条件
								if (value != null) {
									Path path = this.queryEntity.getPath(property);
									ComparableExpressionBase comparablePath = (ComparableExpressionBase)path;
									dslBuilder.where(comparablePath.eq(value));
								}
							}
						}

					} else if (columnMeta != null) {//如果值为空，不进行查询。
						// && paramMap.get(param.getName()) != null
						if (paramMap != null && paramMap.get(param.getName()) != null) {
							Path path = this.queryEntity.getPath(columnMeta.getProperty());
							ComparableExpressionBase comparablePath = (ComparableExpressionBase)path;
							dslBuilder.where(comparablePath.eq(paramMap.get(param.getName())));
						}
					} else {
						throw new RuntimeException(mapperMeta.getMappedStatementId() +  " param = " + param.getName() + " 没有找到该数据列");
					}
				}
			}
			Set<ColumnMeta> columns = entityMeta.getClassColumns();
			for(ColumnMeta column: columns) {
				if (column.isLogicDelete()) {
					NumberPath logicPath = this.queryEntity.numberPath(column.getProperty());
					dslBuilder.where(logicPath.ne(1));
				}
			}
		}else {
			Set<ColumnMeta> columns = entityMeta.getClassColumns();
			for(ColumnMeta column: columns) {
				if (column.isLogicDelete()) {
					NumberPath logicPath = this.queryEntity.numberPath(column.getProperty());
					dslBuilder.where(logicPath.ne(1));
					break;
				}
			}
		}

		log.debug("处理 排序字段 ");
		//处理 OrderBy 排序
		Set<ParamMeta> orderSet = mapperMeta.getOrders();
		if (!orderSet.isEmpty()) {
			for (ParamMeta param: orderSet) {
				if (OrderSpecifier.class.isAssignableFrom(param.getType())) {
					OrderSpecifier<?> orderSpecifier = (OrderSpecifier<?>) paramMap.get(param.getName());
					dslBuilder.orderBy(orderSpecifier);
				}
			}
		}
		if (paramMap != null && paramMap.containsKey(PageListInterceptor.ROW_ROUNDS_KEY)) {
			RowBounds rowBounds = (RowBounds) paramMap.get(PageListInterceptor.ROW_ROUNDS_KEY);
			dslBuilder.pagination(rowBounds.getOffset(), rowBounds.getLimit());
		}
		DynamicContext context = new DynamicContext(configuration, parameterObject);
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		dslBuilder.buildQuerySQL(context, parameterMappings, forCountRow);
		String sql = context.getSql();
		log.debug(sql);
		//StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql, parameterMappings);
		// sqlSource.getBoundSql(parameterObject);
		BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}

		return boundSql;
	}

//	private BoundSql insert(Object parameterObject) {
//		StringBuilder sql = new StringBuilder();
//		Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
//		Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, columnList);
//		String insertSQL = null;
//		try {
//			insertSQL = MybatisScriptFactory.buildSave(tableName, insertColumns, insertColumns);
//			sql.append(insertSQL);
//			System.out.println(insertSQL);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//
//		return null;
//	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
		this.queryEntity = EntityMetaManager.getQueryEntity(entityClass);
	}


}
