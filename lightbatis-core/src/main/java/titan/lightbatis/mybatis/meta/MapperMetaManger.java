/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import titan.lightbatis.result.Page;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来管理和记录 使用 QueryMapper 进行的查询
 * 
 * @author lifei
 *
 */
@Slf4j
public class MapperMetaManger {

	public static final Map<String, Class<?>> queryMapper = new HashMap<>();
	/**
	 * Key: mapperId,
	 * Value: MapperMeta
	 */
	public static final Map<String, MapperMeta> metaMapper = new HashMap<>();

	public static void addMapper(String mapperId, Class<?> mapperClz) {
		queryMapper.put(mapperId, mapperClz);
	}

	public static boolean isQueryMapper(String mapperId) {
		return queryMapper.containsKey(mapperId);
	}

	public static void addMeta(String mapperId, MapperMeta meta) {
		metaMapper.put(mapperId, meta);
	}

	public static MapperMeta getMeta(String mapperId) {
		return metaMapper.get(mapperId);
	}

	public static MapperMeta parse( Method method) {
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
		return meta;
	}
}
