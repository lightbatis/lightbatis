package titan.lightbatis.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.mybatis.meta.*;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ExecuteSqlSource implements SqlSource {
    private final Configuration configuration;
    private final MapperMeta mapperMeta;
    protected Class<?> entityClass;
    private final SqlSourceBuilder sqlSourceParser;
    @Getter
    @Setter
    private String tableName;

    public ExecuteSqlSource(Configuration configuration, MapperMeta mapperMeta, Class<?> entityClass, String tableName) {
        this.configuration = configuration;
        this.mapperMeta = mapperMeta;
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.sqlSourceParser = new SqlSourceBuilder(configuration);
    }

//    public ExecuteSqlSource(Configuration configuration, Annotation executeAnnotation, Class<?> type, Method method) {
//        this.configuration = configuration;
//        this.entityClass = type;
//        this.sqlSourceParser = new SqlSourceBuilder(configuration);
//        this.mapperMeta = MapperMetaManger.parse(method);
//    }
//    private final Configuration configuration;
//    private final Class<?> entityClass;

//    public ExecuteSqlSource(Configuration configuration, Class<?> entityClass) {
//        this.configuration = configuration;
//        this.entityClass = entityClass;
//    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
//        QueryDslBuilder dslBuilder = new QueryDslBuilder(this.configuration);
//        insert(parameterObject);
        return null;
    }

    private BoundSql insert(Object parameterObject) {
//        StringBuilder sql = new StringBuilder();
//        Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
//        Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, columnList);
//        String insertSQL = null;
//        try {
//            insertSQL = MybatisScriptFactory.buildSave(tableName, insertColumns, insertColumns);
//            sql.append(insertSQL);
//            System.out.println(insertSQL);
//            if (parameterObject instanceof Map) {
//                Map<String, Object> params = (Map<String, Object>) parameterObject;
//                Set<ParamMeta> paramSet = mapperMeta.getPredicateParams();
//                for (ParamMeta param : paramSet) {
//                    if (entityClass.isAssignableFrom(param.getType())){
//                        String name = param.getName();
//                        Object paramObject = params.get(name);
//
//                        Class<?> parameterType = paramObject == null ? Object.class : entityClass;
//                        SqlSource source = sqlSourceParser.parse(replacePlaceholder(insertSQL), parameterType, new HashMap<String, Object>());
//                        BoundSql boundSql =  source.getBoundSql(paramObject);
//                        return boundSql;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }
    private String replacePlaceholder(String sql) {
        return PropertyParser.parse(sql, configuration.getVariables());
    }
}
