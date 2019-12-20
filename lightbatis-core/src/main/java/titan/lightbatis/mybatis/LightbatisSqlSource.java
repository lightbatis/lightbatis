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
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.SimpleTypeRegistry;
import titan.lightbatis.mybatis.interceptor.PageListInterceptor;
import titan.lightbatis.mybatis.meta.*;

import java.util.*;

/**
 * @author lifei114@126.com
 *
 */
@Slf4j
public class LightbatisSqlSource implements SqlSource {
	private final Configuration configuration;
	private final MapperMeta mapperMeta;
	private Class<?> entityClass;
	private QEntity queryEntity ;
	@Getter
	@Setter
	private String tableName;

	//private QueryDslBuilder dslBuilder = null;
	private boolean forCountRow = false;
	/**
	 *
	 */
	public LightbatisSqlSource(Configuration configuration, MapperMeta mapperMeta, boolean forCountRow) {
		this.configuration = configuration;
		this.mapperMeta = mapperMeta;

		this.forCountRow = forCountRow;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
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
				} else {
					ColumnMeta columnMeta = entityMeta.findColumnByProperty(param.getName());
					//如果值为空，不进行查询。
					if (columnMeta != null) {
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


	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
		this.queryEntity = EntityMetaManager.getQueryEntity(entityClass);
	}


}
