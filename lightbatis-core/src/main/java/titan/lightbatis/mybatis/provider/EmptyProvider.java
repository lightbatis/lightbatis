/**
 * 
 */
package titan.lightbatis.mybatis.provider;

import titan.lightbatis.mybatis.MapperBuilder;

/**
 * @author lifei114@126.com
 *
 */
public class EmptyProvider extends MapperProvider {

	public EmptyProvider(Class<?> mapperClass, MapperBuilder mapperHelper) {
		super(mapperClass, mapperHelper);
	}

}
