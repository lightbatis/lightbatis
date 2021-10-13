package titan.lightbatis.mybatis;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.SimpleTypeRegistry;
import titan.lightbatis.mybatis.meta.*;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class ExecuteSqlSource implements SqlSource {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    private final Configuration configuration;
    private final MapperMeta mapperMeta;
    protected Class<?> entityClass;
    private QEntity queryEntity ;
    private final SqlSourceBuilder sqlSourceParser;
    private SqlCommandType commandType = null;
    @Getter
    @Setter
    private String tableName;

    public ExecuteSqlSource(Configuration configuration, MapperMeta mapperMeta, Class<?> entityClass, String tableName, SqlCommandType commandType) {
        this.configuration = configuration;
        this.mapperMeta = mapperMeta;
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.sqlSourceParser = new SqlSourceBuilder(configuration);
        this.commandType = commandType;
        this.queryEntity = EntityMetaManager.getQueryEntity(entityClass);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        if (SqlCommandType.INSERT.equals(this.commandType)) {
            return generateInsertSQL(parameterObject);
        } else if (SqlCommandType.UPDATE.equals(this.commandType)) {
            return generateUpdateSQL(parameterObject);
        } else if (SqlCommandType.DELETE.equals(this.commandType)) {
            return generateDeleteSQL(parameterObject);
        }
        return null;
    }
    private BoundSql generateDeleteSQL(Object parameterObject) {
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
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        List<ParameterMapping> parameterMappings = new ArrayList<>();

//        EntityMeta entityMeta = EntityMetaManager.getEntityMeta(entityClass);

        Set<ParamMeta> predicateParams = mapperMeta.getPredicateParams();
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
                }else {
                    Path path = queryEntity.getPath(param.getName());
                    if (path == null){
                        continue;
                    }
                    Object obj = paramMap.get(param.getName());
                    if (path instanceof StringPath) {
                        StringPath fieldPath = (StringPath)path;
                        dslBuilder.where(fieldPath.eq(obj.toString()));
                    } else if (path instanceof NumberPath) {
                        NumberPath fieldPath = (NumberPath)path;
                        dslBuilder.where(fieldPath.eq((Number)obj));
                    } else if (path instanceof DatePath) {
                        DatePath fieldPath = (DatePath)path;
                        dslBuilder.where( fieldPath.eq((Date)obj));
                    } else if (path instanceof DateTimePath) {
                        DateTimePath fieldPath = (DateTimePath) path;
                        dslBuilder.where(fieldPath.eq((Timestamp)obj));
                    }
                }
            }
        }

        dslBuilder.buildDeleteSQL(context, parameterMappings, queryEntity);
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
    private BoundSql generateUpdateSQL(Object parameterObject) {
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
       // dslBuilder.from(this.queryEntity);
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        List<ParameterMapping> parameterMappings = new ArrayList<>();

//        EntityMeta entityMeta = EntityMetaManager.getEntityMeta(entityClass);

        Set<ParamMeta> predicateParams = mapperMeta.getPredicateParams();
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
            }
        }

        Map<Path<?>, Expression<?>> pathExpressionMap = new HashMap<>();
        Set<ParamMeta> updateParams = mapperMeta.getUpdateParams();
        for (ParamMeta param: updateParams) {
            Path path = queryEntity.getPath(param.getName());
            Object obj = paramMap.get(param.getName());
            if (path instanceof StringPath) {
                StringPath fieldPath = (StringPath)path;
                pathExpressionMap.put(fieldPath, fieldPath.eq(obj.toString()));
            } else if (path instanceof NumberPath) {
                NumberPath fieldPath = (NumberPath)path;
                pathExpressionMap.put(fieldPath, fieldPath.eq((Number)obj));
            } else if (path instanceof DatePath) {
                DatePath fieldPath = (DatePath)path;
                pathExpressionMap.put(fieldPath, fieldPath.eq((Date)obj));
            } else if (path instanceof DateTimePath) {
                DateTimePath fieldPath = (DateTimePath) path;
                pathExpressionMap.put(fieldPath, fieldPath.eq((Timestamp)obj));
            }
        }

        dslBuilder.buildUpdateSQL(context, parameterMappings, queryEntity, pathExpressionMap);
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



    private BoundSql generateInsertSQL(Object parameterObject) {
        StringBuilder sql = new StringBuilder();
        Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
        Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, columnList);
//        Set<ColumnMeta> updateColumns = BaseMapperProvider.processUpdate(sql, entityClass, columnList);
        try {
            Boolean updateOn = false;
            BeanMap map = BeanMap.create(parameterObject);
            if (entityClass.isAssignableFrom(parameterObject.getClass())) {
                Set<ColumnMeta> pkColumns = EntityMetaManager.getPKColumns(entityClass);
                if (!pkColumns.isEmpty()) {
                    for (ColumnMeta col: pkColumns) {
                        String property = col.getProperty();
                        Object obj = map.get(property);
                        if (obj ==null) {
                            updateOn = false;
                        } else {
                            updateOn = true;
                            break;
                        }
                    }
                }
            }
            String insertSQL = MybatisScriptFactory.buildSave(tableName, insertColumns, insertColumns, insertColumns, updateOn);
            sql.append(insertSQL);

            SqlSource sqlSource = languageDriver.createSqlSource(this.configuration, "<script>\n\t" + sql.toString() + "</script>", null);
            BoundSql boundSql =  sqlSource.getBoundSql(parameterObject);
            return boundSql;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
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
