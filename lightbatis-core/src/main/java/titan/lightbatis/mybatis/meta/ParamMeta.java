package titan.lightbatis.mybatis.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 方法参数的基本信息
 * @author lifei114@126.com
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7858624465536073182L;
	
	private int index = 0;
	private Class<?> type = null;
	private String name = null;
}
