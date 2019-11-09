/**
 * 
 */
package titan.lightbatis.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来管理和记录 使用 QueryMapper 进行的查询
 * 
 * @author lifei
 *
 */
public class QueryMapperManger {

	public static final Map<String, Class<?>> queryMapper = new HashMap<>();
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
}
