package titan.lightbatis.mybatis;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

public class LightbatisConfiguration extends Configuration {
	private static final Log logger = LogFactory.getLog(LightbatisConfiguration.class);

	protected boolean mapUnderscoreToCamelCase = true;
	/**
	 * Mapper 注册
	 */
	public final LightbatisMapperRegistry mybatisMapperRegistry = new LightbatisMapperRegistry(this);

    public LightbatisConfiguration() {
		super();
	}

	@Override
    public MapperRegistry getMapperRegistry() {
        return mybatisMapperRegistry;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mybatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        mybatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type);
    }

    @Override
    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    @Override
    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        super.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }
}
