/**
 * 
 */
package titan.lightbatis.mapper;

import java.io.Serializable;

import lombok.Data;

/**
 * @author lifei114@126.com
 *
 */
@Data
public class MapperMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972593122879430943L;

	private Class<?> resultClz = null;
}
