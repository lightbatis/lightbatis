/**
 * 
 */
package titan.lightbatis.mybatis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import lombok.extern.slf4j.Slf4j;
import titan.lightbatis.configuration.MapperConfig;
import titan.lightbatis.exception.LightbatisException;
import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.mybatis.provider.EmptyProvider;
import titan.lightbatis.mybatis.provider.MapperProvider;
import titan.lightbatis.mybatis.provider.impl.QueryMapperProvider;

import static titan.lightbatis.utils.MapperUtils.getMapperClass;

/**
 * 重新构建 SqlSource
 * 
 * @author lifei114@126.com
 *
 */
@Slf4j
public class MapperBuilder {
	/**
	 * 缓存skip结果
	 */
	private final Map<String, Boolean> msIdSkip = new ConcurrentHashMap<String, Boolean>();

	/**
	 * 注册的接口
	 */
	private List<Class<?>> registerClass = new ArrayList<Class<?>>();

	/**
	 * 注册的通用Mapper接口
	 */
	private Map<Class<?>, MapperProvider> registerMapper = new ConcurrentHashMap<Class<?>, MapperProvider>();

	/**
	 * 缓存msid和MapperProvider
	 */
	private Map<String, MapperProvider> msIdCache = new ConcurrentHashMap<String, MapperProvider>();

	private MapperConfig config = new MapperConfig();

	/**
	 * 通过通用Mapper接口获取对应的 MapperProvider
	 *
	 * @param mapperClass
	 * @return
	 * @throws Exception
	 */
	private MapperProvider fromMapperClass(Class<?> mapperClass) {
		Method[] methods = mapperClass.getDeclaredMethods();
		Class<?> templateClass = null;
		Class<?> tempClass = null;
		Set<String> methodSet = new HashSet<String>();
		MapperProvider defaultProvider = new QueryMapperProvider(mapperClass, this);
		for (Method method : methods) {
			if (method.isAnnotationPresent(SelectProvider.class)) {
				SelectProvider provider = method.getAnnotation(SelectProvider.class);
				tempClass = provider.type();
				methodSet.add(method.getName());
			} else if (method.isAnnotationPresent(InsertProvider.class)) {
				InsertProvider provider = method.getAnnotation(InsertProvider.class);
				tempClass = provider.type();
				methodSet.add(method.getName());
			} else if (method.isAnnotationPresent(DeleteProvider.class)) {
				DeleteProvider provider = method.getAnnotation(DeleteProvider.class);
				tempClass = provider.type();
				methodSet.add(method.getName());
			} else if (method.isAnnotationPresent(UpdateProvider.class)) {
				UpdateProvider provider = method.getAnnotation(UpdateProvider.class);
				tempClass = provider.type();
				methodSet.add(method.getName());
			} else {
				defaultProvider.addMethodMap(method.getName(), method);
			}

			if (templateClass == null) {
				templateClass = tempClass;
			} else if (templateClass != tempClass) {
				throw new LightbatisException("一个通用Mapper中只允许存在一个 MapperProvider 子类!");
			}
		}
		if (templateClass == null || !MapperProvider.class.isAssignableFrom(templateClass)) {
			templateClass = EmptyProvider.class;
		}
		MapperProvider mapperProvider = null;
		try {
			mapperProvider = (MapperProvider) templateClass.getConstructor(Class.class, MapperBuilder.class)
					.newInstance(mapperClass, this);
		} catch (Exception e) {
			throw new LightbatisException("实例化 MapperProvider 对象失败:" + e.getMessage());
		}
		// 注册方法
		for (String methodName : methodSet) {
			try {
				mapperProvider.addMethodMap(methodName, templateClass.getMethod(methodName, MappedStatement.class));
			} catch (NoSuchMethodException e) {
				throw new LightbatisException(templateClass.getCanonicalName() + "中缺少" + methodName + "方法!");
			}
		}
		return mapperProvider;
	}

	/**
	 * 注册通用Mapper接口
	 *
	 * @param mapperClass
	 */
	public void registerMapper(Class<?> mapperClass) {
		if (!registerMapper.containsKey(mapperClass)) {
			registerClass.add(mapperClass);
			registerMapper.put(mapperClass, fromMapperClass(mapperClass));
		}
		// 自动注册继承的接口
		Class<?>[] interfaces = mapperClass.getInterfaces();
		if (interfaces != null && interfaces.length > 0) {
			for (Class<?> anInterface : interfaces) {
				registerMapper(anInterface);
			}
		}
	}

	/**
	 * 注册通用Mapper接口
	 *
	 * @param mapperClass
	 */
	public void registerMapper(String mapperClass) {
		try {
			registerMapper(Class.forName(mapperClass));
		} catch (ClassNotFoundException e) {
			throw new LightbatisException("注册通用Mapper[" + mapperClass + "]失败，找不到该通用Mapper!");
		}
	}

	/**
	 * 判断接口是否包含通用接口
	 *
	 * @param mapperInterface
	 * @return
	 */
	public boolean isExtendCommonMapper(Class<?> mapperInterface) {
		for (Class<?> mapperClass : registerClass) {
			if (mapperClass.isAssignableFrom(mapperInterface)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 重新设置SqlSource
	 * <p/>
	 * 执行该方法前必须使用isMapperMethod判断，否则msIdCache会空
	 *
	 * @param ms
	 */
	public void setSqlSource(MappedStatement ms) {
		MapperProvider mapperProvider = msIdCache.get(ms.getId());
		try {
			if (mapperProvider != null) {
				mapperProvider.setSqlSource(ms);
			}
		} catch (Exception e) {
			throw new LightbatisException(e);
		}
	}

	/**
	 * 配置属性
	 *
	 * @param properties
	 */
	public void setProperties(Properties properties) {
		// config.setProperties(properties);
		// 注册通用接口
		String mapper = null;
		if (properties != null) {
			mapper = properties.getProperty("mappers");
		}
		if (StringUtils.isNotEmpty(mapper)) {
			String[] mappers = mapper.split(",");
			for (String mapperClass : mappers) {
				if (mapperClass.length() > 0) {
					registerMapper(mapperClass);
				}
			}
		}
	}

	/**
	 * 如果当前注册的接口为空，自动注册默认接口
	 */
	public void ifEmptyRegisterDefaultInterface() {
		if (registerClass.size() == 0) {
			registerMapper(LightbatisMapper.class.getName());
		}
	}

	public void processConfiguration(Configuration configuration) {
		this.processConfiguration(configuration, null);
	}

	public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
		String prefix;
		if (mapperInterface != null) {
			prefix = mapperInterface.getCanonicalName();
		} else {
			prefix = "";
		}
		for (Object object : new ArrayList<Object>(configuration.getMappedStatements())) {
			if (object instanceof MappedStatement) {
				MappedStatement ms = (MappedStatement) object;
				if (ms.getId().startsWith(prefix)) {
					processMappedStatement(ms);
				}
			}
		}
	}

	/**
	 * 处理 MappedStatement
	 *
	 * @param ms
	 */
	public void processMappedStatement(MappedStatement ms) {
		MapperProvider mapperProvider = isMapperMethod(ms.getId());
		if (mapperProvider != null && ms.getSqlSource() instanceof ProviderSqlSource) {
			setSqlSource(ms, mapperProvider);
		}
	}

	/**
	 * 判断当前的接口方法是否需要进行拦截
	 *
	 * @param msId
	 * @return
	 */
	public MapperProvider isMapperMethod(String msId) {
		MapperProvider mapperProvider = getMapperProviderByMsId(msId);
		if (mapperProvider == null) {
			// 通过 @RegisterMapper 注解自动注册的功能
			try {
				Class<?> mapperClass = getMapperClass(msId);
				if (mapperClass.isInterface() && hasRegisterMapper(mapperClass)) {
					mapperProvider = getMapperProviderByMsId(msId);
				}
			} catch (Exception e) {
				log.warn("特殊情况: " + e);
			}
		}
		return mapperProvider;
	}

	/**
	 * 增加通过 @RegisterMapper 注解自动注册的功能
	 *
	 * @param mapperInterface
	 * @return
	 */
	private boolean hasRegisterMapper(Class<?> mapperInterface) {
		// 如果一个都没匹配上，很可能是还没有注册 mappers，此时通过 @RegisterMapper 注解进行判断
		Class<?>[] interfaces = mapperInterface.getInterfaces();
		boolean hasRegisterMapper = false;
		if (interfaces != null && interfaces.length > 0) {
			for (Class<?> anInterface : interfaces) {
				// 自动注册标记了 @RegisterMapper 的接口
				if (anInterface.isAnnotationPresent(titan.lightbatis.annotations.RegisterMapper.class)) {
					hasRegisterMapper = true;
					// 如果已经注册过，就避免在反复调用下面会迭代的方法
					if (!registerMapper.containsKey(anInterface)) {
						registerMapper(anInterface);
					}
				}
				// 如果父接口的父接口存在注解，也可以注册
				else if (hasRegisterMapper(anInterface)) {
					hasRegisterMapper = true;
				}
			}
		}
		return hasRegisterMapper;
	}

	/**
	 * 根据 msId 获取 MapperProvider
	 *
	 * @param msId
	 * @return
	 */
	public MapperProvider getMapperProviderByMsId(String msId) {
		for (Map.Entry<Class<?>, MapperProvider> entry : registerMapper.entrySet()) {
			if (entry.getValue().supportMethod(msId)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * 重新设置SqlSource
	 * <p/>
	 * 执行该方法前必须使用isMapperMethod判断，否则msIdCache会空
	 *
	 * @param ms
	 * @param mapperProvider
	 */
	public void setSqlSource(MappedStatement ms, MapperProvider mapperProvider) {
		try {
			if (mapperProvider != null) {
				mapperProvider.setSqlSource(ms);
			}
		} catch (Exception e) {
			throw new LightbatisException(e);
		}
	}

	public MapperConfig getConfig() {
		return config;
	}

	public void setConfig(MapperConfig config) {
		this.config = config;
//		if (config.getResolveClass() != null) {
//			try {
//				EntityHelper.setResolve(config.getResolveClass().newInstance());
//			} catch (Exception e) {
//				log.error("创建 " + config.getResolveClass().getCanonicalName() + " 实例失败，请保证该类有默认的构造方法!", e);
//				throw new MapperException("创建 " + config.getResolveClass().getCanonicalName() + " 实例失败，请保证该类有默认的构造方法!",
//						e);
//			}
//		}
//		if (config.getMappers() != null && config.getMappers().size() > 0) {
//			for (Class mapperClass : config.getMappers()) {
//				registerMapper(mapperClass);
//			}
//		}
	}

}
