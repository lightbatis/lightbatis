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

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import titan.lightbatis.exception.LightbatisException;
import titan.lightbatis.mybatis.*;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.meta.MapperMeta;
import titan.lightbatis.mybatis.meta.MapperMetaManger;
import titan.lightbatis.mybatis.provider.MapperProvider;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 将生成动态 查询SQL 的提供者。
 * @author lifei114@126.com
 */
public class DynamicSelectProvider extends MapperProvider{
	private Method method = null;
	private MapperMeta mapperMate = null;
	private Configuration configuration = null;
	private ParamNameResolver parameterResolver = null;
	private SqlCommandType commandType = null;
	public DynamicSelectProvider(Configuration config, Method method, Class<?> mapperClass, MapperBuilder mapperHelper, SqlCommandType sqlCommandType) {
		super(mapperClass, mapperHelper);
		parameterResolver= new ParamNameResolver(config, method);
		this.mapperMate =  MapperMetaManger.parse(method);
		this.method = method;
		this.configuration = config;
		this.commandType = sqlCommandType;
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
		if (SqlCommandType.INSERT.equals(commandType) ) {
			Class<?> entityClass = getInsertEntityClass(mappedStatementId, method);
			String tableName = tableName(entityClass);
			ExecuteSqlSource sqlSource =new ExecuteSqlSource(this.configuration, mapperMate,entityClass, tableName, commandType);
			return sqlSource;
		} else if (SqlCommandType.UPDATE.equals(commandType)) {
			Class<?> entityClass = getInsertEntityClass(mappedStatementId, method);
			String tableName= tableName(entityClass);
			ExecuteSqlSource sqlSource =new ExecuteSqlSource(this.configuration, mapperMate,entityClass, tableName, commandType);
			return sqlSource;
		} else if (SqlCommandType.DELETE.equals(commandType)) {
			Class<?> entityClass = getInsertEntityClass(mappedStatementId, method);
			String tableName= tableName(entityClass);
			ExecuteSqlSource sqlSource =new ExecuteSqlSource(this.configuration, mapperMate,entityClass, tableName, commandType);
			return sqlSource;
		}
		else {
			//如果查询语句中出现了 Path, OrderSpecifier 类型时
			LightbatisSqlSource sqlSource = null;
			sqlSource = new LightbatisSqlSource(this.configuration, mapperMate, forCountRow);
			Class<?> entityClass = getEntityClass(mappedStatementId, method);
			String tableName = tableName(entityClass);
			sqlSource.setEntityClass(entityClass);
			sqlSource.setTableName(tableName);
			return sqlSource;
		}
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
	public Class<?> getInsertEntityClass(String msId, Method method) {
		if (entityClassMap.containsKey(msId)) {
			return entityClassMap.get(msId);
		}else {
			Class<?> entityClass = null;

			Class<?> tmpClz = GenericsUtils.getClassGenericType(mapperClass);
			if (tmpClz != null) {
				entityClass = tmpClz;
			}
			if (entityClass != null) {
				if (entityClass.equals(Object.class)) {
					return null;
				}
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
			return null;
		}
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
				if (mClass.equals(Object.class)) {
					Class<?> tmpClz = GenericsUtils.getImplementInterfaceParamType(mapperClass, method, 0);
					if (tmpClz != null) {
						entityClass = tmpClz;
					} else {
						entityClass = mClass;
					}
				} else {
					entityClass = mClass;
				}

			}
			if (entityClass != null) {
				if (entityClass.equals(Object.class)) {
					return null;
				}
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
