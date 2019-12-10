/**
 * 
 */
package titan.lightbatis.mybatis;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.mybatis.meta.*;

import java.util.*;

import static org.apache.ibatis.mapping.ParameterMapping.Builder;

/**
 * @author lifei114@126.com
 *
 */
@Slf4j
public class LightbatisSqlSource implements SqlSource {
	private final Configuration configuration;
	private final MapperMeta mapperMeta;
	private Class<?> entityClass;
	@Getter
	@Setter
	private String tableName;

	private QueryDslBuilder dslBuilder = null;
	/**
	 *
	 */
	public LightbatisSqlSource(Configuration configuration, MapperMeta mapperMeta) {
		this.configuration = configuration;
		this.mapperMeta = mapperMeta;
		this.dslBuilder = new QueryDslBuilder(configuration);
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		HashMap<String, Object> paramMap =  (HashMap<String, Object>) parameterObject;
		DynamicContext context = new DynamicContext(configuration, parameterObject);
		context.appendSql("select");
		Set<ParamMeta> projectionParams = mapperMeta.getProjectionParams();
		log.debug("---------- 拼接 SELECT 字段");
		if (projectionParams.isEmpty()) {
			context.appendSql(" * ");
		} else {
			boolean appendComma = false;
			//如果方法中只有一个参数，且参数的类型是数组，参数的名称将定义为 array
			for (ParamMeta param: projectionParams) {
				Path[] paths = null;
				if (param.getType().isArray()) {
					String paraName = mapperMeta.getParamCount() == 1 ?"array": param.getName();
					paths = (Path<?>[]) paramMap.get(paraName);
				} else {
					paths =new Path[1];
					String name = param.getName();
					Path<?> path = (Path<?>) paramMap.get(name);
					paths[0] = path;
				}
				for (Path path: paths) {
					Path<?> parent = path.getMetadata().getParent();
					if (appendComma) {
						context.appendSql(",");
					}
					if (parent instanceof RelationalPath) {
						RelationalPath relationalPath = (RelationalPath)parent;
						ColumnMetadata columnMeta = relationalPath.getMetadata(path);
						context.appendSql(columnMeta.getName());
					} else {
						context.appendSql(path.getMetadata().getName());
					}
					appendComma = true;
				}
			}
		}
		context.appendSql(" from ");
		context.appendSql(tableName);
		context.appendSql(" as ");
		context.appendSql(tableName);


		log.debug("拼接 查询条件 WHERE ");
		List<ParameterMapping> parameterMappings = new ArrayList<>();
		Set<ParamMeta> predicateParams = mapperMeta.getPredicateParams();
		EntityMeta entityMeta = EntityMetaManager.getEntityMeta(entityClass);
		if (!predicateParams.isEmpty()) {
			context.appendSql(" where ");

			boolean appendAnd = false;
			for (ParamMeta param: predicateParams) {
				if (appendAnd) {
					context.appendSql(" and ");
				}
				if (Predicate.class.isAssignableFrom(param.getType()) || Predicate[].class.isAssignableFrom(param.getType())) {
					Predicate[] predicates = null;
					if (param.getType().isArray()) {
						if (mapperMeta.getParamCount() == 1) {
							predicates = (Predicate[])paramMap.get("array");
							paramMap.put(param.getName(),predicates);
						} else {
							predicates = (Predicate[]) paramMap.get(param.getName());
						}
					} else {
						predicates = new Predicate[1];
						predicates[0] = (Predicate) paramMap.get(param.getName());
					}
					this.dslBuilder.buildPredicate(context, parameterMappings, predicates);
					appendAnd = true;
				} else {
					ColumnMeta columnMeta = entityMeta.findColumnByProperty(param.getName());
					if (columnMeta != null) {
						context.appendSql(columnMeta.getColumn());
						context.appendSql(" = ? ");
						parameterMappings.add(new Builder(configuration, param.getName(), param.getType()).build());
						context.bind(param.getName(),paramMap.get(param.getName()));

						appendAnd = true;
					} else {
						throw new RuntimeException(mapperMeta.getMappedStatementId() +  " param = " + param.getName() + " 没有找到该数据列");
					}
				}
			}
			Set<ColumnMeta> columns = entityMeta.getClassColumns();
			for(ColumnMeta column: columns) {
				if (column.isLogicDelete()) {
					if (appendAnd) {
						context.appendSql(" and (");
					}
					context.appendSql(column.getColumn());
					context.appendSql(" != 1)");
					break;
				}
			}
		}else {
			Set<ColumnMeta> columns = entityMeta.getClassColumns();
			for(ColumnMeta column: columns) {
				if (column.isLogicDelete()) {
					context.appendSql(" where ");
					context.appendSql(column.getColumn());
					context.appendSql(" != 1");
					break;
				}
			}
		}
		log.debug("处理 排序字段 ");
		//处理 OrderBy 排序
		Set<ParamMeta> orderSet = mapperMeta.getOrders();
		if (!orderSet.isEmpty()) {
			//context.appendSql(" order by ");
			boolean appendComma = false;
			for (ParamMeta param: orderSet) {
				if (appendComma) {
					context.appendSql(",");
				}
				if (OrderSpecifier.class.isAssignableFrom(param.getType())) {
					OrderSpecifier<?> orderSpecifier = (OrderSpecifier<?>) paramMap.get(param.getName());
					this.dslBuilder.buildOrder(context, orderSpecifier);
					appendComma = true;
				}
			}
		}
		
		String sql = context.getSql();
		log.debug(sql);
		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql, parameterMappings);
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}
		return boundSql;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}


}
