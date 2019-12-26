/**
 *
 * 联系作者扫描以下二维码：
 *
 █████████████████████████████████████
 █████████████████████████████████████
 ████ ▄▄▄▄▄ █▀█ █▄▄▀▀ ▀▄█ █ ▄▄▄▄▄ ████
 ████ █   █ █▀▀▀█ ▀▀ ████▄█ █   █ ████
 ████ █▄▄▄█ █▀ █▀▀▄▀▀▄ ▀█ █ █▄▄▄█ ████
 ████▄▄▄▄▄▄▄█▄▀ ▀▄█▄▀▄█ ▀ █▄▄▄▄▄▄▄████
 ████ ▄ ▄ ▀▄  ▄▀▄▀▄ █ █▀ █ ▀ ▀▄█▄▀████
 ████▄ ▄   ▄▄██▄█▀▄  ▄▄▀█ ▄▀  ▀█▀█████
 ████ ▀▄▄█ ▄▄▄ ▄█▄▄▀▄▄█▀ ▀▀▀▀▀▄▄█▀████
 █████ ▀ ▄ ▄▄█▀  ▄██ █▄▄▀  ▄ ▀▄▄▀█████
 ████▀▄  ▄▀▄▄█▄▀▄▀█▄▀▀ ▄ ▀▀▀ ▀▄ █▀████
 ████ ██▄▄▄▄█▀▄▀█▀█▀▄▀█ ▀▄▄█▀██▄▀█████
 ████▄███▄█▄█▀▄ █▄▀▄▄▀▄██ ▄▄▄ ▀   ████
 ████ ▄▄▄▄▄ █▄█▄ ▄▄  ██▄  █▄█ ▄▄▀█████
 ████ █   █ █ ▀█▄ ▀ ▄▄▀▀█ ▄▄▄▄▀ ▀ ████
 ████ █▄▄▄█ █ ▄▀███▀▄▄▄▄▄ █▄▀  ▄ █████
 ████▄▄▄▄▄▄▄█▄███▄█▄▄▄▄▄██▄█▄▄▄▄██████
 █████████████████████████████████████
 █████████████████████████████████████
 *
 * 基于 MyBatis 扩展的数据访问统一层
 */
package titan.lightbatis.mybatis.provider.impl;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import titan.lightbatis.exception.LightbatisException;
import titan.lightbatis.mybatis.LightbatisSqlSource;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.ColumnMeta;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.meta.MapperMeta;
import titan.lightbatis.mybatis.meta.MapperMetaManger;
import titan.lightbatis.mybatis.provider.MapperProvider;
import titan.lightbatis.mybatis.script.MybatisScriptFactory;
import titan.lightbatis.result.Page;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * 将生成动态 查询SQL 的提供者。
 * @author lifei114@126.com
 */
public class DynamicSelectProvider extends MapperProvider{
	private Method method = null;
	private MapperMeta mapperMate = null;
	private Configuration configuration = null;
	private ParamNameResolver parameterResolver = null;
	public DynamicSelectProvider(Configuration config, Method method, Class<?> mapperClass, MapperBuilder mapperHelper) {
		super(mapperClass, mapperHelper);
		parameterResolver= new ParamNameResolver(config, method);
		this.mapperMate =  MapperMetaManger.parse(method);
		this.method = method;
		this.configuration = config;
	}

	public MapperMeta getMapperMate () {
		return this.mapperMate;
	}
	/**
	 * 如果访问的方法中有 Path, OrderSpecifier 类型时，将使用动态的 SQL 进行访问。
	 * @return
	 * @param mappedStatementId
	 */
	public SqlSource buildDynamicSQL(String mappedStatementId, boolean forCountRow) throws Exception{
		//如果查询语句中出现了 Path, OrderSpecifier 类型时
		Class<?> entityClass = getEntityClass(mappedStatementId, method);
		String tableName = tableName(entityClass);
		LightbatisSqlSource sqlSource = new LightbatisSqlSource(this.configuration, mapperMate, forCountRow);
		sqlSource.setEntityClass(entityClass);
		sqlSource.setTableName(tableName);
		return sqlSource;
	}

	/**
	 * 将返回值的类型，注册到 MappedStatement 中去
	 * 
	 * @param mappedStatement
	 */
	public void registeResultMap(MappedStatement mappedStatement) {
		String msId = mappedStatement.getId();
		Class<?> entityClass = getEntityClass(msId, method);
		// 修改返回值类型为实体类型
		setResultType(mappedStatement, entityClass);
	}

	public Class<?> getEntityClass(String msId, Method method) {
		if (entityClassMap.containsKey(msId)) {
			return entityClassMap.get(msId);
		} else {
			Class<?> entityClass = null;
			Class<?> mClass = method.getReturnType();
			if (List.class.isAssignableFrom(mClass)) {
				Type methodType = method.getGenericReturnType();
				if (methodType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) methodType;
					Type[] actualTypes = pt.getActualTypeArguments();
					if (actualTypes.length > 0) {
						Type t = actualTypes[0];
						if (t instanceof Class) {
							entityClass = (Class<?>) t;
						}
						
					}
				}
				if (entityClass == null) {
					Type[] types = mapperClass.getGenericInterfaces();
					for (Type type : types) {
						if (type instanceof ParameterizedType) {
							ParameterizedType t = (ParameterizedType) type;
							Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
							entityClass = returnType;
						}
					}
				}
			} else {
				entityClass = mClass;
			}
			if (entityClass != null) {
				// 获取该类型后，第一次对该类型进行初始化
				try {
					EntityMetaManager.initEntityNameMap(entityClass, mapperBuilder.getConfig(),msId);
					entityClassMap.put(msId, entityClass);
					return entityClass;
				} catch (Exception e) {
					System.err.println(msId + " = " + method + " 加载失败！");
					e.printStackTrace(System.err);
					throw new LightbatisException("无法获取Mapper<T>泛型类型:" + msId);
				}

			}
		}
		throw new LightbatisException("无法获取Mapper<T>泛型类型:" + msId);
	}
}
