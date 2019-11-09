/**
 * 
 */
package titan.lightbatis.mybatis;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import titan.lightbatis.result.PageList;

/**
 * 
 * @author lifei
 *
 */
public class LightMapperMethod extends MapperMethod {
	private final SqlCommand command;
	private final MethodSignature method;
	private Configuration config = null;
	public LightMapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
		super(mapperInterface, method, config);
		this.command = new SqlCommand(config, mapperInterface, method);
		this.method = new MethodSignature(config, mapperInterface, method);
		this.config = config;
	}

	public Object execute(SqlSession sqlSession, Object[] args) {
		Object result;
		switch (command.getType()) {
		case INSERT: {
			Object param = method.convertArgsToSqlCommandParam(args);
			//param = checkPrimaryKeyObject(param);
			result = rowCountResult(sqlSession.insert(command.getName(), param));
			break;
		}
		case UPDATE: {
			Object param = method.convertArgsToSqlCommandParam(args);
			result = rowCountResult(sqlSession.update(command.getName(), param));
			break;
		}
		case DELETE: {
			Object param = method.convertArgsToSqlCommandParam(args);
			result = rowCountResult(sqlSession.delete(command.getName(), param));
			break;
		}
		case SELECT:
			if (method.returnsVoid() && method.hasResultHandler()) {
				executeWithResultHandler(sqlSession, args);
				result = null;
			} else if (method.returnsMany()) {
				result = executeForMany(sqlSession, args);
			} else if (method.returnsMap()) {
				result = executeForMap(sqlSession, args);
			} else if (method.returnsCursor()) {
				result = executeForCursor(sqlSession, args);
			} else {
				Object param = method.convertArgsToSqlCommandParam(args);
				result = sqlSession.selectOne(command.getName(), param);
			}
			break;
		case FLUSH:
			result = sqlSession.flushStatements();
			break;
		default:
			throw new BindingException("Unknown execution method for: " + command.getName());
		}
		if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
			throw new BindingException("Mapper method '" + command.getName()
					+ " attempted to return null from a method with a primitive return type (" + method.getReturnType()
					+ ").");
		}
		return result;
	}

	/**
	 * 检查是否需要生成主键信息
	 * @param param
	 * @return
	 */
//	private Object checkPrimaryKeyObject(Object param) {
//		Class<?> paramClz = param.getClass();
//		Table entity = paramClz.getAnnotation(Table.class);
//		if (entity != null) {
//			String tableName = entity.name();
//			TableSchema tableSchema = ITableSchemaManager.getInstance().getTable(tableName);
//			if (tableSchema != null) {
//				String paramName = paramClz.getSimpleName();
//				if (paramName.equals(tableSchema.getEntityName())) {
//					EntityMeta entityMeta = EntityMetaManager.findEntityTable(paramClz);
//					if (entityMeta == null) {
//						entityMeta = EntityMetaManager.initEntityNameMap(paramClz, new Config(), command.getName());
//					}
//					 List<String> keys = tableSchema.getPrimaryKeys();
//					 if (keys.size() > 0 ) {
//						 for (String key: keys) {
//							 ColumnSchema col = tableSchema.getColumn(key);
//							 if (col.isPrimary()) {
//								ColumnMeta colMeta =  entityMeta.findColumnByField(col.getColumnName());
//								 if (colMeta.isId()) {
//									 Class<?> type = colMeta.getJavaType();
//									 if (Number.class.isAssignableFrom(type)) {
//										 String propertyName = col.getPropertyName();
//										 Long id = ITableSchemaManager.getInstance().nextId();
//										 config.newMetaObject(param).setValue(col.getPropertyName(), id);
//									 }
//								 }
//		
//							 }
//						 }
//					 }
//				}
//			}
//		}
//		return param;
//	}
	private Object rowCountResult(int rowCount) {
		final Object result;
		if (method.returnsVoid()) {
			result = null;
		} else if (Integer.class.equals(method.getReturnType()) || Integer.TYPE.equals(method.getReturnType())) {
			result = rowCount;
		} else if (Long.class.equals(method.getReturnType()) || Long.TYPE.equals(method.getReturnType())) {
			result = (long) rowCount;
		} else if (Boolean.class.equals(method.getReturnType()) || Boolean.TYPE.equals(method.getReturnType())) {
			result = rowCount > 0;
		} else {
			throw new BindingException("Mapper method '" + command.getName() + "' has an unsupported return type: "
					+ method.getReturnType());
		}
		return result;
	}

	private void executeWithResultHandler(SqlSession sqlSession, Object[] args) {
		MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(command.getName());
		if (void.class.equals(ms.getResultMaps().get(0).getType())) {
			throw new BindingException(
					"method " + command.getName() + " needs either a @ResultMap annotation, a @ResultType annotation,"
							+ " or a resultType attribute in XML so a ResultHandler can be used as a parameter.");
		}
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			sqlSession.select(command.getName(), param, rowBounds, method.extractResultHandler(args));
		} else {
			sqlSession.select(command.getName(), param, method.extractResultHandler(args));
		}
	}

	private <E> Object executeForMany(SqlSession sqlSession, Object[] args) {
		List<E> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			result = sqlSession.<E>selectList(command.getName(), param, rowBounds);
		} else {
			result = sqlSession.<E>selectList(command.getName(), param);
		}

		// issue #510 Collections & arrays support
		if (!method.getReturnType().isAssignableFrom(result.getClass())) {
			if (method.getReturnType().isArray()) {
				return convertToArray(result);
			} else {
				if (result instanceof PageList) {
					return result;
				}
				return convertToDeclaredCollection(sqlSession.getConfiguration(), result);
			}
		}
		return result;
	}

	private <T> Cursor<T> executeForCursor(SqlSession sqlSession, Object[] args) {
		Cursor<T> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			result = sqlSession.<T>selectCursor(command.getName(), param, rowBounds);
		} else {
			result = sqlSession.<T>selectCursor(command.getName(), param);
		}
		return result;
	}

	private <E> Object convertToDeclaredCollection(Configuration config, List<E> list) {
		Object collection = config.getObjectFactory().create(method.getReturnType());
		MetaObject metaObject = config.newMetaObject(collection);
		metaObject.addAll(list);
		return collection;
	}

	@SuppressWarnings("unchecked")
	private <E> Object convertToArray(List<E> list) {
		Class<?> arrayComponentType = method.getReturnType().getComponentType();
		Object array = Array.newInstance(arrayComponentType, list.size());
		if (arrayComponentType.isPrimitive()) {
			for (int i = 0; i < list.size(); i++) {
				Array.set(array, i, list.get(i));
			}
			return array;
		} else {
			return list.toArray((E[]) array);
		}
	}

	private <K, V> Map<K, V> executeForMap(SqlSession sqlSession, Object[] args) {
		Map<K, V> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			result = sqlSession.<K, V>selectMap(command.getName(), param, method.getMapKey(), rowBounds);
		} else {
			result = sqlSession.<K, V>selectMap(command.getName(), param, method.getMapKey());
		}
		return result;
	}
}
