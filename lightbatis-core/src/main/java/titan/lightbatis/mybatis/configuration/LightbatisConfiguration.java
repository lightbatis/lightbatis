package titan.lightbatis.mybatis.configuration;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LightbatisConfiguration extends Configuration {
    private static final Logger logger = LoggerFactory.getLogger(LightbatisAutoConfiguration.class);
    private Configuration configuration = null;


    public LightbatisConfiguration() {
        super();
    }

    public LightbatisConfiguration(Environment environment) {
        super(environment);
    }

    public LightbatisConfiguration(Configuration configuration) {
        this.configuration = configuration;
        this.environment = configuration.getEnvironment();
        this.safeResultHandlerEnabled = configuration.isSafeResultHandlerEnabled();
        this.safeResultHandlerEnabled = configuration.isSafeResultHandlerEnabled();
        this.mapUnderscoreToCamelCase = configuration.isMapUnderscoreToCamelCase();
        this.aggressiveLazyLoading = configuration.isAggressiveLazyLoading();
        this.multipleResultSetsEnabled = configuration.isMultipleResultSetsEnabled();
        this.useGeneratedKeys = configuration.isUseGeneratedKeys();
        this.useColumnLabel = configuration.isUseColumnLabel();
        this.cacheEnabled = configuration.isCacheEnabled();
        this.callSettersOnNulls = configuration.isCallSettersOnNulls();
        this.useActualParamName = configuration.isUseActualParamName();
        this.returnInstanceForEmptyRow = configuration.isReturnInstanceForEmptyRow();
        this.logPrefix = configuration.getLogPrefix();
        this.logImpl = configuration.getLogImpl();
        this.vfsImpl = configuration.getVfsImpl();
        this.defaultStatementTimeout = configuration.getDefaultStatementTimeout();
        this.defaultFetchSize = configuration.getDefaultFetchSize();
        this.lazyLoadingEnabled = configuration.isLazyLoadingEnabled();
        this.databaseId = configuration.getDatabaseId();
        this.configurationFactory = configuration.getConfigurationFactory();
    }


    public void removeResource(String resource) {
        loadedResources.remove(resource);
        Set<String> removeKeys = new HashSet<>();
        Set<Map.Entry<String, MappedStatement>> statements = mappedStatements.entrySet();
        statements.forEach(entry -> {
            Object obj = entry.getValue();
            if (obj instanceof  MappedStatement) {
                MappedStatement ms = (MappedStatement) obj;
                if (ms.getResource().equals(resource)) {
                    removeKeys.add(entry.getKey());
                    logger.debug("删除资源文件 " + resource);
                }
            } else {
//                if (obj instanceof Configuration.StrictMap) {
//                    logger.debug("类型不知道:" + obj);
//                }
            }
        });
        removeKeys.stream().forEach( key ->{
            mappedStatements.remove(key);
            resultMaps.remove(key);
            parameterMaps.remove(key);
            keyGenerators.remove(key);
            System.out.println("============= remove key ==" + key);
            String stkey = key + "_sql";
            mappedStatements.remove(stkey);
            sqlFragments.remove(stkey);
            stkey = key + "_PAGE";
            mappedStatements.remove(stkey);
            stkey = key + "_COUNT";
            mappedStatements.remove(stkey);

        });
//        mappedStatements.remove(resource);
        caches.remove(resource);
        resultMaps.remove(resource);
        removeKeys.clear();
        parameterMaps.remove(resource);
        keyGenerators.remove(resource);
        sqlFragments.remove(resource);
    }
}
