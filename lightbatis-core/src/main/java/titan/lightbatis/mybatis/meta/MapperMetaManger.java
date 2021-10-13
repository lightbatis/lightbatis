/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.core.DefaultParameterNameDiscoverer;
import titan.lightbatis.annotations.LightDelete;
import titan.lightbatis.annotations.LightSave;
import titan.lightbatis.annotations.LightUpdate;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用来管理和记录 使用 QueryMapper 进行的查询
 * 
 * @author lifei
 *
 */
@Slf4j
public class MapperMetaManger {
	public static final Set<Class<? extends Annotation>> sqlInsertAnnotationTypes = new HashSet<Class<? extends Annotation>>();
	public static final Map<String, Class<?>> queryMapper = new HashMap<>();
	static {
		sqlInsertAnnotationTypes.add(LightSave.class);
		sqlInsertAnnotationTypes.add(LightUpdate.class);
		sqlInsertAnnotationTypes.add(LightDelete.class);
	}
	/**
	 * Key: mapperId,
	 * Value: MapperMeta
	 */
	public static final Map<String, MapperMeta> metaMapper = new HashMap<>();

//	public static void addMapper(String mapperId, Class<?> mapperClz) {
//		queryMapper.put(mapperId, mapperClz);
//	}
//
//	public static boolean isQueryMapper(String mapperId) {
//		return queryMapper.containsKey(mapperId);
//	}

	public static void addMeta(String mapperId, MapperMeta meta) {
		metaMapper.put(mapperId, meta);
	}

	public static MapperMeta getMeta(String mapperId) {
		return metaMapper.get(mapperId);
	}

	public static MapperMeta parse( Method method) {
		SqlCommandType type = guessSqlCommandType(method);
		if (SqlCommandType.UPDATE.equals(type)) {
			return parseUpdate(method);
		} else if (SqlCommandType.INSERT.equals(type)) {
			return parseDelete(method);
		}else {
			return parseSelect(method);
		}
	}

	private static  MapperMeta parseDelete(Method method) {
		MapperMeta meta = new MapperMeta();
		DefaultParameterNameDiscoverer nameDiscover = new DefaultParameterNameDiscoverer();
		final Class<?>[] paramTypes = method.getParameterTypes();
		final String[] codeNames = nameDiscover.getParameterNames(method);
		int paramCount = codeNames.length;
		meta.setParamCount(paramCount);

		for (int i=0; i< paramCount; i++ ){
			ParamMeta param = new ParamMeta(i, paramTypes[i], codeNames[i]);
			meta.addPredicate(param);
		}
		return meta;
	}

	private static MapperMeta parseUpdate(Method method) {
		MapperMeta meta = new MapperMeta();
		DefaultParameterNameDiscoverer nameDiscover = new DefaultParameterNameDiscoverer();
		final Class<?>[] paramTypes = method.getParameterTypes();
		final String[] codeNames = nameDiscover.getParameterNames(method);
		int paramCount = codeNames.length;
		meta.setParamCount(paramCount);

		for (int i=0; i< paramCount; i++ ){
			ParamMeta param = new ParamMeta(i, paramTypes[i], codeNames[i]);
			if (Predicate.class.isAssignableFrom(param.getType()) || Predicate[].class.isAssignableFrom(param.getType())) {
				meta.addPredicate(param);
			}else {
				meta.addUpdate(param);
			}
		}
		return meta;
	}

	private static MapperMeta parseSelect(Method method) {
		MapperMeta meta = new MapperMeta();
		DefaultParameterNameDiscoverer nameDiscover = new DefaultParameterNameDiscoverer();
		final Class<?>[] paramTypes = method.getParameterTypes();
		final String[] codeNames = nameDiscover.getParameterNames(method);
		int paramCount = codeNames.length;
		meta.setParamCount(paramCount);
		for (int i=0; i< paramCount; i++ ){
			ParamMeta param = new ParamMeta(i, paramTypes[i], codeNames[i]);
			//查询返回的字段
			if (Path.class.isAssignableFrom(param.getType()) || Path[].class.isAssignableFrom(param.getType())) {
				meta.addProjection(param);
				meta.setDynamicSQL(true);
				log.debug("要查询的字段 " + param.getName());
				//排序字段
			} else if (OrderSpecifier.class.isAssignableFrom(param.getType())) {
				meta.addOrder(param);
				meta.setDynamicSQL(true);
			} else if (Page.class.isAssignableFrom(param.getType())){
				meta.setPageable(true);
			}
			else {
				log.debug("条件字段 ");
				//条件字段
				meta.addPredicate(param);
				if (Predicate.class.isAssignableFrom(param.getType()) || Predicate[].class.isAssignableFrom(param.getType())) {
					meta.setDynamicSQL(true);
				}
			}
		}
		Class<?> returnType = method.getReturnType();
		if (PageList.class.isAssignableFrom(returnType)) {
			meta.setCoutable(true);
		}
		return meta;
	}

	public static  SqlCommandType guessSqlCommandType(Method method) {
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
	private static Class<? extends Annotation> chooseAnnotationType(Method method, Set<Class<? extends Annotation>> types) {
		for (Class<? extends Annotation> type : types) {
			Annotation annotation = method.getAnnotation(type);
			if (annotation != null) {
				return type;
			}
		}
		return null;
	}
}
