package titan.lightbatis.mybatis.provider.impl;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.mybatis.LightbatisSqlSource;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.provider.MapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class RuntimeDynamicMapperProvider extends DynamicSelectProvider {


    public RuntimeDynamicMapperProvider(Configuration config, Method method, Class<?> mapperClass, MapperBuilder mapperHelper) {
        super(config, method, mapperClass, mapperHelper);
    }

}
