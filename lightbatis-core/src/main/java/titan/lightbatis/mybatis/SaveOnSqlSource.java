package titan.lightbatis.mybatis;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.cglib.beans.BeanMap;
import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SaveOnSqlSource implements SqlSource {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    private final Configuration configuration;
    private final SqlSourceBuilder sqlSourceParser;
    private final Class<?> providerType;
    private final Class<?> entityClass;
    private Method providerMethod;
    private String[] providerMethodArgumentNames;
    private Class<?>[] providerMethodParameterTypes;
    private ProviderContext providerContext;
    private Integer providerContextIndex;
    private String tableName;
    public SaveOnSqlSource(Configuration configuration, Class<?> providerType, Method providerMethod, Class<?> entityClass, String tableName) {
        String providerMethodName;
        try {
            this.configuration = configuration;
            this.sqlSourceParser = new SqlSourceBuilder(configuration);
            this.entityClass = entityClass;
            this.tableName = tableName;
            this.providerType = providerType;
            providerMethodName = "save";
            this.providerMethod = providerMethod;
            this.providerMethodArgumentNames = new ParamNameResolver(configuration, providerMethod).getNames();
            this.providerMethodParameterTypes = providerMethod.getParameterTypes();
        } catch (BuilderException e) {
            throw e;
        } catch (Exception e) {
            throw new BuilderException("Error creating SqlSource for SqlProvider.  Cause: " + e, e);
        }
        if (this.providerMethod == null) {
            throw new BuilderException("Error creating SqlSource for SqlProvider. Method '"
                    + providerMethodName + "' not found in SqlProvider '" + this.providerType.getName() + "'.");
        }
//        for (int i = 0; i< this.providerMethodParameterTypes.length; i++) {
//            Class<?> parameterType = this.providerMethodParameterTypes[i];
//            if (parameterType == ProviderContext.class) {
//                if (this.providerContext != null){
//                    throw new BuilderException("Error creating SqlSource for SqlProvider. ProviderContext found multiple in SqlProvider method ("
//                            + this.providerType.getName() + "." + providerMethod.getName()
//                            + "). ProviderContext can not define multiple in SqlProvider method argument.");
//                }
//                this.providerContext = new ProviderContext(mapperType, mapperMethod);
//                this.providerContextIndex = i;
//            }
//        }
    }


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
//        BeanMap map = BeanMap.create(parameterObject);
//        HashMap parameters = new HashMap(map);
        SqlSource sqlSource = createSqlSource(parameterObject);
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//        boundSql.getParameterMappings().removeIf(parameterMapping -> {
//            return parameterMapping.getProperty().equals("id_cache");
//        });
        boundSql.setAdditionalParameter("updateOn", Boolean.TRUE);
        boundSql.setAdditionalParameter("id_cache", null);
        return boundSql;
    }

    private SqlSource createSqlSource(Object parameterObject) {
        try {
//            int bindParameterCount = providerMethodParameterTypes.length - (providerContext == null ? 0 : 1);
//            String sql;
//            if (providerMethodParameterTypes.length == 0) {
//                sql = invokeProviderMethod();
//            } else if (bindParameterCount == 0) {
//                sql = invokeProviderMethod(providerContext);
//            } else if (bindParameterCount == 1 &&
//                    (parameterObject == null || providerMethodParameterTypes[(providerContextIndex == null || providerContextIndex == 1) ? 0 : 1].isAssignableFrom(parameterObject.getClass()))) {
//                sql = invokeProviderMethod(extractProviderMethodArguments(parameterObject));
//            } else if (parameterObject instanceof Map) {
//                @SuppressWarnings("unchecked")
//                Map<String, Object> params = (Map<String, Object>) parameterObject;
//                sql = invokeProviderMethod(extractProviderMethodArguments(params, providerMethodArgumentNames));
//            } else {
//                throw new BuilderException("Error invoking SqlProvider method ("
//                        + providerType.getName() + "." + providerMethod.getName()
//                        + "). Cannot invoke a method that holds "
//                        + (bindParameterCount == 1 ? "named argument(@Param)": "multiple arguments")
//                        + " using a specifying parameterObject. In this case, please specify a 'java.util.Map' object.");
//            }
            StringBuilder sql = new StringBuilder();
            Set<ColumnMeta> columnList = EntityMetaManager.getColumns(entityClass);
            Set<ColumnMeta> insertColumns = BaseMapperProvider.processKey(sql, entityClass, columnList);
            Set<ColumnMeta> updateColumns = BaseMapperProvider.processUpdate(sql, entityClass, columnList);

            try {
                //String insertSQL = MybatisScriptFactory.buildInsert(tableName(entityClass), EntityMetaManager.getColumns(entityClass), columnList);
                String insertSQL = MybatisScriptFactory.buildSave(tableName, insertColumns, insertColumns, updateColumns, true);
                sql.append(insertSQL);
                //log.debug("==========");
                //System.out.println(insertSQL);
                //log.debug(insertSQL);
//			ExecuteSqlSource sqlSource = new ExecuteSqlSource(ms.getConfiguration(), null, entityClass, tableName(entityClass));
//			return sqlSource;
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            return languageDriver.createSqlSource(this.configuration, "<script>\n\t" + sql.toString() + "</script>", null);

//            Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
//            return sqlSourceParser.parse(replacePlaceholder(sql.toString()), parameterType, new HashMap<String, Object>());
        } catch (BuilderException e) {
            throw e;
        } catch (Exception e) {
            throw new BuilderException("Error invoking SqlProvider method ("
                    + providerType.getName() + "." + providerMethod.getName()
                    + ").  Cause: " + e, e);
        }
    }

    private Object[] extractProviderMethodArguments(Object parameterObject) {
        if (providerContext != null) {
            Object[] args = new Object[2];
            args[providerContextIndex == 0 ? 1 : 0] = parameterObject;
            args[providerContextIndex] = providerContext;
            return args;
        } else {
            return new Object[] { parameterObject };
        }
    }

    private Object[] extractProviderMethodArguments(Map<String, Object> params, String[] argumentNames) {
        Object[] args = new Object[argumentNames.length];
        for (int i = 0; i < args.length; i++) {
            if (providerContextIndex != null && providerContextIndex == i) {
                args[i] = providerContext;
            } else {
                args[i] = params.get(argumentNames[i]);
            }
        }
        return args;
    }

    private String invokeProviderMethod(Object... args) throws Exception {
        Object targetObject = null;
        if (!Modifier.isStatic(providerMethod.getModifiers())) {
            targetObject = providerType.newInstance();
        }
        CharSequence sql = (CharSequence) providerMethod.invoke(targetObject, args);
        return sql != null ? sql.toString() : null;
    }

    private String replacePlaceholder(String sql) {
        return PropertyParser.parse(sql, configuration.getVariables());
    }
}
