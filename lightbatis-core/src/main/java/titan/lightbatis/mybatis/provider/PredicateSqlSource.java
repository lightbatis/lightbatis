package titan.lightbatis.mybatis.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.SQLTemplates;

import titan.lightbatis.mybatis.MapperBuilder;

// 使用 LightbatisSqlSource 了
@Deprecated
public class PredicateSqlSource implements SqlSource {

	private final Configuration configuration;
	private final MapperBuilder mapperHelper;
	private final String sql;
	private com.querydsl.sql.Configuration queryConfig;
	public PredicateSqlSource(Configuration configuration, MapperBuilder mapperHelper, String sql) {
		super();
		this.configuration = configuration;
		this.mapperHelper = mapperHelper;
		this.sql = sql;
		SQLTemplates templates = new MySQLTemplates();
		queryConfig = new com.querydsl.sql.Configuration(templates);
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		//DynamicContext context = new DynamicContext(configuration, parameterObject);
		//context.appendSql(sql);
		Map paramMap = (Map)parameterObject;
		Predicate[] predicates = (Predicate[]) paramMap.get("array");
		SQLSerializer serializer = new SQLSerializer(queryConfig);
		BooleanBuilder builder = new BooleanBuilder();
		builder.orAllOf(predicates);
		serializer.handle(builder);
		
		
		String whereSQL = serializer.toString();
		StringBuilder sqlBuilder = new StringBuilder(this.sql);
		sqlBuilder.append(" WHERE ");
		sqlBuilder.append(whereSQL);
		
		List<Object> constantList = serializer.getConstants();
		List<Path<?>> fiedList = serializer.getConstantPaths();
		List<ParameterMapping> paraList = new ArrayList<>();
		Map<String,Object> params = new HashMap<>();
		for (int i=0; i < fiedList.size(); i++ ) {
			Path<?> path = fiedList.get(i);
			PathMetadata pathMeta = path.getMetadata();
			String name = pathMeta.getName();
			Class<?> type = path.getType();
			
			paraList.add(new ParameterMapping.Builder(configuration,name, type).build());
			
			Object value = constantList.get(i);
			//boundSql.setAdditionalParameter(name, value);
			params.put(name, value);
		}
		String sql = sqlBuilder.toString();
		BoundSql boundSql = new BoundSql(configuration, sql, paraList, params);
		
		for (int i=0; i < fiedList.size(); i++ ) {
			Path<?> path = fiedList.get(i);
			PathMetadata pathMeta = path.getMetadata();
			String name = pathMeta.getName();
			
			Object value = constantList.get(i);
			boundSql.setAdditionalParameter(name, value);
		}
		
	    return boundSql;
	}

}
