/**
 * 
 */
package titan.lightbatis.mybatis;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.*;
import com.querydsl.sql.SQLSerializer;
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

/**
 * @author lifei114@126.com
 *
 */
@Slf4j
public class QueryDslBuilder {

	private Configuration configuration = null;
	private SQLTemplatesRegistry templatesRegistry = new SQLTemplatesRegistry();
	private SQLTemplates sqlTemplates;
	private SQLSerializer sqlSerializer;
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
			this.sqlSerializer = new LightbatisSQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
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
	public void buildQuerySQL(DynamicContext context,  List<ParameterMapping> parameterMappings, boolean forCountRow){
		this.sqlSerializer = new LightbatisSQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
		this.sqlSerializer.serialize(queryMetadata,forCountRow);
		String sql = sqlSerializer.toString();
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

//	/**
//	 * 构建排序语句
//	 * @param context
//	 * @param orderSpecifier
//	 */
//	public void buildOrder(DynamicContext context, OrderSpecifier<?> orderSpecifier) {
//		SQLSerializer sqlSerializer = new SQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
//		DefaultQueryMetadata queryMetadata =new  DefaultQueryMetadata();
//
//		queryMetadata.addOrderBy(orderSpecifier);
//		sqlSerializer.serialize(queryMetadata,false);
//		String sql = sqlSerializer.toString();
//
//
//
////		Expression<?> target = orderSpecifier.getTarget();
////		PathImpl<?> path = (PathImpl<?>) target;
////		context.appendSql(path.getMetadata().getElement().toString());
////		context.appendSql(" ");
////		context.appendSql(orderSpecifier.getOrder().name());
//		context.appendSql(sql);
//		log.debug(context.getSql());
//	}
//
//	/**
//	 * 构建查询条件语句
//	 * @param context
//	 * @param predicates
//	 * @param parameterMappings
//	 */
//	public void buildPredicate(DynamicContext context,  List<ParameterMapping> parameterMappings, Predicate... predicates) {
//		BooleanBuilder builder = new BooleanBuilder();
//		builder.orAllOf(predicates);
//		SQLSerializer sqlSerializer = new SQLSerializer(new com.querydsl.sql.Configuration(sqlTemplates));
//
//		sqlSerializer.handle(builder);
//
//		String sql = sqlSerializer.toString();
//		log.debug(sql);
//		context.appendSql(" ");
//		context.appendSql(sql);
//
//		List<Object> constantList = sqlSerializer.getConstants();
//		List<Path<?>> fields = sqlSerializer.getConstantPaths();
//		for (int i=0; i < fields.size(); i++ ) {
//			Path<?> path = fields.get(i);
//			PathMetadata pathMeta = path.getMetadata();
//			String name = pathMeta.getName() + "_" + i;
//			Class<?> type = path.getType();
//
//			parameterMappings.add(new ParameterMapping.Builder(configuration,name, type).build());
//
//			Object value = constantList.get(i);
//			//boundSql.setAdditionalParameter(name, value);
//			context.getBindings().put(name, value);
//		}
//	}
}
