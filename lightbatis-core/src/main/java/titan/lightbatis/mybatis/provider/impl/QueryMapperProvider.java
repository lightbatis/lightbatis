/**
 * 
 */
package titan.lightbatis.mybatis.provider.impl;

import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.provider.MapperProvider;

/**
 * @author lifei114@126.com
 *
 */
public class QueryMapperProvider extends MapperProvider {

	public QueryMapperProvider(Class<?> mapperClass, MapperBuilder mapperBuilder) {
		super(mapperClass, mapperBuilder);
	}

}
