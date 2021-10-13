package titan.lightbatis.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.mybatis.meta.*;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


public class ExecuteSqlSource implements SqlSource {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
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

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        StringBuilder sql = new StringBuilder();
        Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
        Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, columnList);
        Set<ColumnMeta> updateColumns = BaseMapperProvider.processUpdate(sql, entityClass, columnList);
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

            //Class<?> parameterType = parameterObject == null ? Object.class : entityClass;
            //SqlSource source = sqlSourceParser.parse(replacePlaceholder(insertSQL), parameterType, new HashMap<String, Object>());
            SqlSource sqlSource = languageDriver.createSqlSource(this.configuration, "<script>\n\t" + sql.toString() + "</script>", null);

            BoundSql boundSql =  sqlSource.getBoundSql(parameterObject);
            String skipField = "updateOn";

            //boundSql.setAdditionalParameter(skipField, Boolean.FALSE);


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
