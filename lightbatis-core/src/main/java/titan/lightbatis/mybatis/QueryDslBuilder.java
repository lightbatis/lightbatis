/**
 * 
 */
package titan.lightbatis.mybatis;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.*;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplatesRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.mybatis.meta.QEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author lifei114@126.com
 *
 */
@Slf4j
public class QueryDslBuilder {

	private Configuration configuration = null;
	private SQLTemplatesRegistry templatesRegistry = new SQLTemplatesRegistry();
	private SQLTemplates sqlTemplates;
	private DefaultQueryMetadata queryMetadata = null;

	/**
	 * 
	 */
	public QueryDslBuilder(Configuration configuration) {
		this.configuration = configuration;
		this.queryMetadata = new DefaultQueryMetadata();
		DataSource ds = this.configuration.getEnvironment().getDataSource();
		try {
			Connection connection = ds.getConnection();
			sqlTemplates = templatesRegistry.getTemplates(connection.getMetaData());
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public QueryDslBuilder from(QEntity queryEntity) {
		queryMetadata.addJoin(JoinType.DEFAULT,queryEntity);
		return this;
	}
	public QueryDslBuilder where(Predicate... predicates) {
		for (Predicate predicate: predicates) {
			queryMetadata.addWhere(predicate);
		}
		return this;
	}
	public void buildDeleteSQL(DynamicContext context, List<ParameterMapping> parameterMappings, RelationalPath<?> queryEntry) {
		LightbatisSQLSerializer sqlSerializer = new LightbatisSQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
		sqlSerializer.serializeDelete(queryMetadata, queryEntry);
		String sql = sqlSerializer.toString();
		buildSQL(context, sql, sqlSerializer, parameterMappings);
	}
	public void buildUpdateSQL (DynamicContext context, List<ParameterMapping> parameterMappings, RelationalPath<?> queryEntry, Map<Path<?>, Expression<?>> updates) {
		LightbatisSQLSerializer sqlSerializer = new LightbatisSQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
		//sqlSerializer.serialize(queryMetadata,false);
		sqlSerializer.serializeUpdate(queryMetadata, queryEntry, updates);
		String sql = sqlSerializer.toString();
		buildSQL(context, sql, sqlSerializer, parameterMappings);
	}

	public void buildQuerySQL(DynamicContext context,  List<ParameterMapping> parameterMappings, boolean forCountRow){
		LightbatisSQLSerializer sqlSerializer = new LightbatisSQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
		sqlSerializer.serialize(queryMetadata,forCountRow);
		String sql = sqlSerializer.toString();
		//context.appendSql(sql);
		buildSQL(context, sql, sqlSerializer, parameterMappings);
//		List<Object> constantList = sqlSerializer.getConstants();
//		List<Path<?>> fields = sqlSerializer.getConstantPaths();
//
//		for (int i=0; i < fields.size(); i++ ) {
//			Path<?> path = fields.get(i);
//			Object value = constantList.get(i);
//			String name = null;
//			if (path == null) {
//				name =  "param_" + i;
//				Class<?> paramType = value.getClass();
//				parameterMappings.add(new ParameterMapping.Builder(configuration,name, paramType).build());
//				//continue;
//			} else {
//				PathMetadata pathMeta = path.getMetadata();
//				name = pathMeta.getName() + "_" + i;
//				Class<?> type = path.getType();
//				parameterMappings.add(new ParameterMapping.Builder(configuration,name, type).build());
//			}
//			//boundSql.setAdditionalParameter(name, value);
//			context.getBindings().put(name, value);
//		}
	}

	public void buildSQL(DynamicContext context,String sql, LightbatisSQLSerializer sqlSerializer,   List<ParameterMapping> parameterMappings) {
		context.appendSql(sql);
		List<Object> constantList = sqlSerializer.getConstants();
		List<Path<?>> fields = sqlSerializer.getConstantPaths();

		for (int i=0; i < fields.size(); i++ ) {
			Path<?> path = fields.get(i);
			Object value = constantList.get(i);
			String name = null;
			if (path == null) {
				name =  "param_" + i;
				Class<?> paramType = value.getClass();
				parameterMappings.add(new ParameterMapping.Builder(configuration,name, paramType).build());
				//continue;
			} else {
				PathMetadata pathMeta = path.getMetadata();
				name = pathMeta.getName() + "_" + i;
				Class<?> type = path.getType();
				parameterMappings.add(new ParameterMapping.Builder(configuration,name, type).build());
			}
			//boundSql.setAdditionalParameter(name, value);
			context.getBindings().put(name, value);
		}
	}
	/**
	 * 构建排序语句
	 * @param orderSpecifier
	 */
	public void orderBy(OrderSpecifier<?> orderSpecifier) {
		queryMetadata.addOrderBy(orderSpecifier);
	}

	public void pagination(Integer offset, Integer limit) {
		queryMetadata.setOffset(offset.longValue());
		queryMetadata.setLimit(limit.longValue());
	}
}
