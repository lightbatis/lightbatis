package titan.lightbatis.mybatis;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.builder.annotation.MethodResolver;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import titan.lightbatis.annotations.LightDelete;
import titan.lightbatis.annotations.LightSave;
import titan.lightbatis.annotations.LightUpdate;
import titan.lightbatis.mybatis.interceptor.PageListInterceptor;
import titan.lightbatis.mybatis.meta.MapperMeta;
import titan.lightbatis.mybatis.meta.MapperMetaManger;
import titan.lightbatis.mybatis.provider.impl.DynamicSelectProvider;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

class LightbatisMapperAnnotationBuilder extends MapperAnnotationBuilder {
    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);
    private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();
    private final Set<Class<? extends Annotation>> sqlProviderAnnotationTypes = new HashSet<Class<? extends Annotation>>();


    private final Set<Class<? extends Annotation>> sqlInsertAnnotationTypes = MapperMetaManger.sqlInsertAnnotationTypes;

    private final Configuration configuration;
    private final MapperBuilderAssistant assistant;
    private final Class<?> type;

    public LightbatisMapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        super(configuration,type);
        this.configuration = configuration;
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.type = type;

        sqlAnnotationTypes.add(Select.class);
        sqlAnnotationTypes.add(Insert.class);
        sqlAnnotationTypes.add(Update.class);
        sqlAnnotationTypes.add(Delete.class);

        sqlProviderAnnotationTypes.add(SelectProvider.class);
        sqlProviderAnnotationTypes.add(InsertProvider.class);
        sqlProviderAnnotationTypes.add(UpdateProvider.class);
        sqlProviderAnnotationTypes.add(DeleteProvider.class);

//        sqlInsertAnnotationTypes.add(LightSave.class);
//        sqlInsertAnnotationTypes.add(LightUpdate.class);
//        sqlInsertAnnotationTypes.add(LightDelete.class);
    }

    @Override
    public void parse() {
        String resource = type.toString();
        //如果已经被MyBatis 处理过,此时只处 Mybatis 没有处理过的方法就可以了，具体的就是方法上没有任何的注释。
        if (!configuration.isResourceLoaded(resource)) {
            loadXmlResource();
            configuration.addLoadedResource(resource);
        }
        configuration.setUseGeneratedKeys(true);
        assistant.setCurrentNamespace(type.getName());
        parseCache();
        parseCacheRef();
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            try {
                // issue #237
                if (!method.isBridge()) {
                    parseStatement(method);
                }
            } catch (IncompleteElementException e) {
                 configuration.addIncompleteMethod(new MethodResolver(this, method));
            }
        }
        parsePendingMethods();
    }

    private void parsePendingMethods() {
        Collection<MethodResolver> incompleteMethods = configuration.getIncompleteMethods();
        synchronized (incompleteMethods) {
            Iterator<MethodResolver> iter = incompleteMethods.iterator();
            while (iter.hasNext()) {
                try {
                    iter.next().resolve();
                    iter.remove();
                } catch (IncompleteElementException e) {
                    // This method is still missing a resource
                }
            }
        }
    }

    private void loadXmlResource() {
        // Spring may not know the real resource name so we check a flag
        // to prevent loading again a resource twice
        // this flag is set at XMLMapperBuilder#bindMapperForNamespace
        if (!configuration.isResourceLoaded("namespace:" + type.getName())) {
            String xmlResource = type.getName().replace('.', '/') + ".xml";
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(type.getClassLoader(), xmlResource);
            } catch (IOException e) {
                // ignore, resource is not required
            }
            if (inputStream != null) {
                XMLMapperBuilder xmlParser = new XMLMapperBuilder(inputStream, assistant.getConfiguration(),
                        xmlResource, configuration.getSqlFragments(), type.getName());
                xmlParser.parse();
            }
        }
    }

    private void parseCache() {
        CacheNamespace cacheDomain = type.getAnnotation(CacheNamespace.class);
        if (cacheDomain != null) {
            Integer size = cacheDomain.size() == 0 ? null : cacheDomain.size();
            Long flushInterval = cacheDomain.flushInterval() == 0 ? null : cacheDomain.flushInterval();
            Properties props = convertToProperties(cacheDomain.properties());
            assistant.useNewCache(cacheDomain.implementation(), cacheDomain.eviction(), flushInterval, size,
                    cacheDomain.readWrite(), cacheDomain.blocking(), props);
        }
    }

    private Properties convertToProperties(Property[] properties) {
        if (properties.length == 0) {
            return null;
        }
        Properties props = new Properties();
        for (Property property : properties) {
            props.setProperty(property.name(), PropertyParser.parse(property.value(), configuration.getVariables()));
        }
        return props;
    }

    private void parseCacheRef() {
        CacheNamespaceRef cacheDomainRef = type.getAnnotation(CacheNamespaceRef.class);
        if (cacheDomainRef != null) {
            Class<?> refType = cacheDomainRef.value();
            String refName = cacheDomainRef.name();
            if (refType == void.class && refName.isEmpty()) {
                throw new BuilderException(
                        "Should be specified either value() or name() attribute in the @CacheNamespaceRef");
            }
            if (refType != void.class && !refName.isEmpty()) {
                throw new BuilderException("Cannot use both value() and name() attribute in the @CacheNamespaceRef");
            }
            String namespace = (refType != void.class) ? refType.getName() : refName;
            assistant.useCacheRef(namespace);
        }
    }

    private String parseResultMap(Method method) {
        Class<?> returnType = getReturnType(method);
        ConstructorArgs args = method.getAnnotation(ConstructorArgs.class);
        Results results = method.getAnnotation(Results.class);
        TypeDiscriminator typeDiscriminator = method.getAnnotation(TypeDiscriminator.class);
        String resultMapId = generateResultMapName(method);
        applyResultMap(resultMapId, returnType, argsIf(args), resultsIf(results), typeDiscriminator);
        return resultMapId;
    }

    private String generateResultMapName(Method method) {
        Results results = method.getAnnotation(Results.class);
        if (results != null && !results.id().isEmpty()) {
            return type.getName() + "." + results.id();
        }
        StringBuilder suffix = new StringBuilder();
        for (Class<?> c : method.getParameterTypes()) {
            suffix.append("-");
            suffix.append(c.getSimpleName());
        }
        if (suffix.length() < 1) {
            suffix.append("-void");
        }
        return type.getName() + "." + method.getName() + suffix;
    }

    private void applyResultMap(String resultMapId, Class<?> returnType, Arg[] args, Result[] results,
                                TypeDiscriminator discriminator) {
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        applyConstructorArgs(args, returnType, resultMappings);
        applyResults(results, returnType, resultMappings);
        Discriminator disc = applyDiscriminator(resultMapId, returnType, discriminator);
        // TODO add AutoMappingBehaviour
        assistant.addResultMap(resultMapId, returnType, null, disc, resultMappings, null);
        createDiscriminatorResultMaps(resultMapId, returnType, discriminator);
    }

    private void createDiscriminatorResultMaps(String resultMapId, Class<?> resultType,
                                               TypeDiscriminator discriminator) {
        if (discriminator != null) {
            for (Case c : discriminator.cases()) {
                String caseResultMapId = resultMapId + "-" + c.value();
                List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
                // issue #136
                applyConstructorArgs(c.constructArgs(), resultType, resultMappings);
                applyResults(c.results(), resultType, resultMappings);
                // TODO add AutoMappingBehaviour
                assistant.addResultMap(caseResultMapId, c.type(), resultMapId, null, resultMappings, null);
            }
        }
    }

    private Discriminator applyDiscriminator(String resultMapId, Class<?> resultType, TypeDiscriminator discriminator) {
        if (discriminator != null) {
            String column = discriminator.column();
            Class<?> javaType = discriminator.javaType() == void.class ? String.class : discriminator.javaType();
            JdbcType jdbcType = discriminator.jdbcType() == JdbcType.UNDEFINED ? null : discriminator.jdbcType();
            @SuppressWarnings("unchecked")
            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (discriminator
                    .typeHandler() == UnknownTypeHandler.class ? null : discriminator.typeHandler());
            Case[] cases = discriminator.cases();
            Map<String, String> discriminatorMap = new HashMap<String, String>();
            for (Case c : cases) {
                String value = c.value();
                String caseResultMapId = resultMapId + "-" + value;
                discriminatorMap.put(value, caseResultMapId);
            }
            return assistant.buildDiscriminator(resultType, column, javaType, jdbcType, typeHandler, discriminatorMap);
        }
        return null;
    }

	void parseStatement(Method method){
        Class<?> parameterTypeClass = getParameterType(method);
        LanguageDriver languageDriver = getLanguageDriver(method);
        SqlSource sqlSource = getSqlSourceFromAnnotations(method, parameterTypeClass, languageDriver);
        //如果不为空，说明 MyBatis 已经处理过了，现在只处理没有注释的。
        if (sqlSource == null) {
            try {
                parseDynaticStatement(method);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                configuration.addIncompleteMethod(new MethodResolver(this, method));
            }
        }else {
//            Class<? extends Annotation> sqlExecuteAnnoationType = chooseAnnotationType(method, sqlInsertAnnotationTypes);
//            if (method.getName().equals("save")) {
//                final String mappedStatementId = type.getName() + "." + method.getName();
//                //System.out.println("save ..............");
//                //MappedStatement statement = configuration.getMappedStatement(mappedStatementId);
//                //MetaObject meta = SystemMetaObject.forObject(statement);
//                //meta.setValue("sqlSource",new SqlSourceProxy(sqlSource));
//            }
        }
    }

    /**
     * 如果方法没有注释，则使用对普通的方法进行分析，解析出 MappedStatement
     * @param method
     */
    void parseDynaticStatement (Method method) throws Exception{
        Class<?> parameterTypeClass = getParameterType(method);
        LanguageDriver languageDriver = getLanguageDriver(method);
        MapperBuilder mapperBuilder= new MapperBuilder();

        if (method.getName().equalsIgnoreCase("listPageBy")) {
            System.out.println("listPageBy==================" + method.getName());
        }

        //DynamicMethodSqlSource sqlSource = createDynaticSqlSource(method, parameterTypeClass, languageDriver);

        Options options = method.getAnnotation(Options.class);
        final String mappedStatementId = type.getName() + "." + method.getName();
        Integer fetchSize = null;
        Integer timeout = null;
        StatementType statementType = StatementType.PREPARED;
        ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
        //根据方法猜测查询的类型。
        SqlCommandType sqlCommandType = MapperMetaManger.guessSqlCommandType(method);

        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        boolean flushCache = !isSelect;
        boolean useCache = isSelect;


        DynamicSelectProvider provider = new DynamicSelectProvider(configuration,method, type, mapperBuilder, sqlCommandType);
        MapperMeta meta = provider.getMapperMate();
        if (meta == null) {
            meta = new MapperMeta();
        }

        //将当前的 MappedStatementId 放到 SQLSource 中去。
        //sqlSource.setMappedStatementId(mappedStatementId);
        //创建 SQL
        SqlSource sqlSource = provider.buildDynamicSQL(mappedStatementId, false);


        KeyGenerator keyGenerator;
        String keyProperty = "id";
        String keyColumn = null;
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // first check for SelectKey annotation - that overrides everything else
            SelectKey selectKey = method.getAnnotation(SelectKey.class);
            if (selectKey != null) {
                keyGenerator = handleSelectKeyAnnotation(selectKey, mappedStatementId, getParameterType(method),
                        languageDriver);
                keyProperty = selectKey.keyProperty();
            } else if (options == null) {
                keyGenerator = configuration.isUseGeneratedKeys() ? Jdbc3KeyGenerator.INSTANCE
                        : NoKeyGenerator.INSTANCE;
            } else {
                keyGenerator = options.useGeneratedKeys() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
                keyProperty = options.keyProperty();
                keyColumn = options.keyColumn();
            }
        } else {
            keyGenerator = NoKeyGenerator.INSTANCE;
        }

        if (options != null) {
            if (Options.FlushCachePolicy.TRUE.equals(options.flushCache())) {
                flushCache = true;
            } else if (Options.FlushCachePolicy.FALSE.equals(options.flushCache())) {
                flushCache = false;
            }
            useCache = options.useCache();
            fetchSize = options.fetchSize() > -1 || options.fetchSize() == Integer.MIN_VALUE ? options.fetchSize()
                    : null; // issue #348
            timeout = options.timeout() > -1 ? options.timeout() : null;
            statementType = options.statementType();
            resultSetType = options.resultSetType();
        }

        String resultMapId = null;
        ResultMap resultMapAnnotation = method.getAnnotation(ResultMap.class);
        if (resultMapAnnotation != null) {
            String[] resultMaps = resultMapAnnotation.value();
            StringBuilder sb = new StringBuilder();
            for (String resultMap : resultMaps) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(resultMap);
            }
            resultMapId = sb.toString();
        } else if (isSelect) {
            resultMapId = parseResultMap(method);
        }



        MappedStatement mappedStatement = assistant.addMappedStatement(mappedStatementId, sqlSource, statementType, sqlCommandType, fetchSize,
                timeout,
                // ParameterMapID
                null, parameterTypeClass, resultMapId, getReturnType(method), resultSetType, flushCache, useCache,
                // TODO gcode issue #577
                false, keyGenerator, keyProperty, keyColumn,
                // DatabaseID
                null, languageDriver,
                // ResultSets
                options != null ? nullOrEmpty(options.resultSets()) : null);
        provider.registeResultMap(mappedStatement);
        meta.setMappedStatementId(mappedStatementId);
        meta.setResultClz(method.getReturnType());
        MapperMetaManger.addMeta(mappedStatementId, meta);

        //检查是否要查询总条数
        if (meta.isCoutable()) {
            sqlSource = provider.buildDynamicSQL(mappedStatementId, true);
            String countMsId = mappedStatementId + PageListInterceptor.COUNT_MSID_KEY;
            //count查询返回值int
//            List<ResultMap> resultMaps = new ArrayList<ResultMap>();
//            ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, EMPTY_RESULTMAPPING).build();
//            resultMaps.add(resultMap);
            MappedStatement countMs = newCountMappedStatement(mappedStatement, countMsId, sqlSource);
            mappedStatement.getConfiguration().addMappedStatement(countMs);
        }

       // provider = null;


    }
    /**
     * 新建count查询的MappedStatement
     *
     * @param ms
     * @param newMsId
     * @return
     */
    private MappedStatement newCountMappedStatement(MappedStatement ms, String newMsId, SqlSource countSQLSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), newMsId, countSQLSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<org.apache.ibatis.mapping.ResultMap> resultMaps = new ArrayList<org.apache.ibatis.mapping.ResultMap>();
        org.apache.ibatis.mapping.ResultMap resultMap = new org.apache.ibatis.mapping.ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, EMPTY_RESULTMAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private LanguageDriver getLanguageDriver(Method method) {
        Lang lang = method.getAnnotation(Lang.class);
        Class<?> langClass = null;
        if (lang != null) {
            langClass = lang.value();
        }
        return assistant.getLanguageDriver(langClass);
    }

    private Class<?> getParameterType(Method method) {
        Class<?> parameterType = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> currentParameterType : parameterTypes) {
            if (!RowBounds.class.isAssignableFrom(currentParameterType)
                    && !ResultHandler.class.isAssignableFrom(currentParameterType)) {
                if (parameterType == null) {
                    parameterType = currentParameterType;
                } else {
                    // issue #135
                    parameterType = MapperMethod.ParamMap.class;
                }
            }
        }
        return parameterType;
    }

    private Class<?> getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, type);
        if (resolvedReturnType instanceof Class) {
            returnType = (Class<?>) resolvedReturnType;
            if (returnType.isArray()) {
                returnType = returnType.getComponentType();
            }
            // gcode issue #508
            if (void.class.equals(returnType)) {
                ResultType rt = method.getAnnotation(ResultType.class);
                if (rt != null) {
                    returnType = rt.value();
                }
            }
        } else if (resolvedReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) resolvedReturnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(rawType) || Cursor.class.isAssignableFrom(rawType)) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    Type returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class<?>) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        // (gcode issue #443) actual type can be a also a parameterized type
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
                                .getGenericComponentType();
                        // (gcode issue #525) support List<byte[]>
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            } else if (method.isAnnotationPresent(MapKey.class) && Map.class.isAssignableFrom(rawType)) {
                // (gcode issue 504) Do not look into Maps if there is not MapKey annotation
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    Type returnTypeParameter = actualTypeArguments[1];
                    if (returnTypeParameter instanceof Class<?>) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        // (gcode issue 443) actual type can be a also a parameterized type
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }
                }
            }
        }

        return returnType;
    }

    private SqlSource getSqlSourceFromAnnotations(Method method, Class<?> parameterType,
                                                  LanguageDriver languageDriver) {
        try {
            Class<? extends Annotation> sqlAnnotationType = getSqlAnnotationType(method);
            Class<? extends Annotation> sqlProviderAnnotationType = getSqlProviderAnnotationType(method);
            Class<? extends Annotation> sqlExecuteAnnoationType = null;//chooseAnnotationType(method, sqlInsertAnnotationTypes);
            if (sqlAnnotationType != null) {
                if (sqlProviderAnnotationType != null) {
                    throw new BindingException(
                            "You cannot supply both a static SQL and SqlProvider to method named " + method.getName());
                }
                Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
                final String[] strings = (String[]) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
                return buildSqlSourceFromStrings(strings, parameterType, languageDriver);
            } else if (sqlProviderAnnotationType != null) {
                Annotation sqlProviderAnnotation = method.getAnnotation(sqlProviderAnnotationType);
                return new ProviderSqlSource(assistant.getConfiguration(), sqlProviderAnnotation, type, method);
            }

            return null;
        } catch (Exception e) {
            throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
        }
    }

    private SqlSource buildSqlSourceFromStrings(String[] strings, Class<?> parameterTypeClass,
                                                LanguageDriver languageDriver) {
        final StringBuilder sql = new StringBuilder();
        for (String fragment : strings) {
            sql.append(fragment);
            sql.append(" ");
        }
        return languageDriver.createSqlSource(configuration, sql.toString().trim(), parameterTypeClass);
    }

    /**
     * 通过对 Method 的分析，依据规范来创建 CommandType 的类型
     * @param method
     * @return
     */
    private SqlCommandType guessSqlCommandType(Method method) {
        Class<? extends Annotation> type = chooseAnnotationType(method, sqlInsertAnnotationTypes);
        if (type != null) {
            if (type == LightSave.class) {
                return SqlCommandType.INSERT;
            } else if (type == LightUpdate.class) {
                return SqlCommandType.UPDATE;
            } else if (type == LightDelete.class) {
                return SqlCommandType.DELETE;
            }
        } else {
            String methodName = method.getName();
            if (methodName.startsWith("save")) {
                return SqlCommandType.INSERT;
            }else if (methodName.startsWith("update")) {
                return SqlCommandType.UPDATE;
            }else if (methodName.startsWith("delete")) {
                return SqlCommandType.DELETE;
            }
        }
        return SqlCommandType.SELECT;
    }

    private SqlCommandType getSqlCommandType(Method method) {
        Class<? extends Annotation> type = getSqlAnnotationType(method);

        if (type == null) {
            type = getSqlProviderAnnotationType(method);

            if (type == null) {
                return SqlCommandType.UNKNOWN;
            }

            if (type == SelectProvider.class) {
                type = Select.class;
            } else if (type == InsertProvider.class) {
                type = Insert.class;
            } else if (type == UpdateProvider.class) {
                type = Update.class;
            } else if (type == DeleteProvider.class) {
                type = Delete.class;
            }
        }

        return SqlCommandType.valueOf(type.getSimpleName().toUpperCase(Locale.ENGLISH));
    }

    private Class<? extends Annotation> getSqlAnnotationType(Method method) {
        return chooseAnnotationType(method, sqlAnnotationTypes);
    }

    private Class<? extends Annotation> getSqlProviderAnnotationType(Method method) {
        return chooseAnnotationType(method, sqlProviderAnnotationTypes);
    }

    private Class<? extends Annotation> chooseAnnotationType(Method method, Set<Class<? extends Annotation>> types) {
        for (Class<? extends Annotation> type : types) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) {
                return type;
            }
        }
        return null;
    }

    private void applyResults(Result[] results, Class<?> resultType, List<ResultMapping> resultMappings) {
        for (Result result : results) {
            List<ResultFlag> flags = new ArrayList<ResultFlag>();
            if (result.id()) {
                flags.add(ResultFlag.ID);
            }
            @SuppressWarnings("unchecked")
            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) ((result
                    .typeHandler() == UnknownTypeHandler.class) ? null : result.typeHandler());
            ResultMapping resultMapping = assistant.buildResultMapping(resultType, nullOrEmpty(result.property()),
                    nullOrEmpty(result.column()), result.javaType() == void.class ? null : result.javaType(),
                    result.jdbcType() == JdbcType.UNDEFINED ? null : result.jdbcType(),
                    hasNestedSelect(result) ? nestedSelectId(result) : null, null, null, null, typeHandler, flags, null,
                    null, isLazy(result));
            resultMappings.add(resultMapping);
        }
    }

    private String nestedSelectId(Result result) {
        String nestedSelect = result.one().select();
        if (nestedSelect.length() < 1) {
            nestedSelect = result.many().select();
        }
        if (!nestedSelect.contains(".")) {
            nestedSelect = type.getName() + "." + nestedSelect;
        }
        return nestedSelect;
    }

    private boolean isLazy(Result result) {
        boolean isLazy = configuration.isLazyLoadingEnabled();
        if (result.one().select().length() > 0 && FetchType.DEFAULT != result.one().fetchType()) {
            isLazy = result.one().fetchType() == FetchType.LAZY;
        } else if (result.many().select().length() > 0 && FetchType.DEFAULT != result.many().fetchType()) {
            isLazy = result.many().fetchType() == FetchType.LAZY;
        }
        return isLazy;
    }

    private boolean hasNestedSelect(Result result) {
        if (result.one().select().length() > 0 && result.many().select().length() > 0) {
            throw new BuilderException("Cannot use both @One and @Many annotations in the same @Result");
        }
        return result.one().select().length() > 0 || result.many().select().length() > 0;
    }

    private void applyConstructorArgs(Arg[] args, Class<?> resultType, List<ResultMapping> resultMappings) {
        for (Arg arg : args) {
            List<ResultFlag> flags = new ArrayList<ResultFlag>();
            flags.add(ResultFlag.CONSTRUCTOR);
            if (arg.id()) {
                flags.add(ResultFlag.ID);
            }
            @SuppressWarnings("unchecked")
            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (arg
                    .typeHandler() == UnknownTypeHandler.class ? null : arg.typeHandler());
            ResultMapping resultMapping = assistant.buildResultMapping(resultType, nullOrEmpty(arg.name()),
                    nullOrEmpty(arg.column()), arg.javaType() == void.class ? null : arg.javaType(),
                    arg.jdbcType() == JdbcType.UNDEFINED ? null : arg.jdbcType(), nullOrEmpty(arg.select()),
                    nullOrEmpty(arg.resultMap()), null, null, typeHandler, flags, null, null, false);
            resultMappings.add(resultMapping);
        }
    }

    private String nullOrEmpty(String value) {
        return value == null || value.trim().length() == 0 ? null : value;
    }

    private Result[] resultsIf(Results results) {
        return results == null ? new Result[0] : results.value();
    }

    private Arg[] argsIf(ConstructorArgs args) {
        return args == null ? new Arg[0] : args.value();
    }

    private KeyGenerator handleSelectKeyAnnotation(SelectKey selectKeyAnnotation, String baseStatementId,
                                                   Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        String id = baseStatementId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        Class<?> resultTypeClass = selectKeyAnnotation.resultType();
        StatementType statementType = selectKeyAnnotation.statementType();
        String keyProperty = selectKeyAnnotation.keyProperty();
        String keyColumn = selectKeyAnnotation.keyColumn();
        boolean executeBefore = selectKeyAnnotation.before();

        // defaults
        boolean useCache = false;
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        Integer fetchSize = null;
        Integer timeout = null;
        boolean flushCache = false;
        String parameterMap = null;
        String resultMap = null;
        ResultSetType resultSetTypeEnum = null;

        SqlSource sqlSource = buildSqlSourceFromStrings(selectKeyAnnotation.statement(), parameterTypeClass,
                languageDriver);
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;

        assistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap,
                parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, false,
                keyGenerator, keyProperty, keyColumn, null, languageDriver, null);

        id = assistant.applyCurrentNamespace(id, false);

        MappedStatement keyStatement = configuration.getMappedStatement(id, false);
        SelectKeyGenerator answer = new SelectKeyGenerator(keyStatement, executeBefore);
        configuration.addKeyGenerator(id, answer);
        return answer;
    }
}
