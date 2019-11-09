/**
 * 
 */
package titan.lightbatis.mybatis.configuration;

import org.mybatis.spring.mapper.MapperFactoryBean;

/**
 * @author lifei114@126.com
 *
 */
public class LightbatisMapperFactoryBean<T> extends MapperFactoryBean<T> {

	/**
	 * 
	 */
	public LightbatisMapperFactoryBean() {
		super();
	}

	/**
	 * @param mapperInterface
	 */
	public LightbatisMapperFactoryBean(Class<T> mapperInterface) {
		super(mapperInterface);
	}

}
