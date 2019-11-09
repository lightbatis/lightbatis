/**
 * 
 */
package titan.lightbatis.mybatis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author lifei
 *
 */
public class LightbatisMapperRegistry extends MapperRegistry {
//    private final Map<Class<?>, PageMapperProxyFactory<?>> knownMappers = new HashMap<>();
    private final Configuration config;
    private final Map<Class<?>, LightMapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, LightMapperProxyFactory<?>>();

	public LightbatisMapperRegistry(Configuration config) {
		super(config);
		this.config = config;
	}

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final LightMapperProxyFactory<T> mapperProxyFactory = (LightMapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MybatisPlusMapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }
    
    @Override
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // TODO 如果之前注入 直接返回
                return;
                // throw new BindingException("Type " + type +
                // " is already known to the MybatisPlusMapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
            		
            		knownMappers.put(type, new LightMapperProxyFactory<T>(type));
                // It's important that the type is added before the parser is run
                // otherwise the binding may automatically be attempted by the
                // mapper parser. If the type is already known, it won't try.
                LightbatisMapperAnnotationBuilder parser = new LightbatisMapperAnnotationBuilder(config, type);
                parser.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }
    /**
     * @since 3.2.2
     */
    @Override
    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(knownMappers.keySet());
    }
}
