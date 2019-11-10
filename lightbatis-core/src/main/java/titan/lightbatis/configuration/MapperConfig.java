/**
 * 
 */
package titan.lightbatis.configuration;

import java.io.Serializable;

import lombok.Data;

/**
 * @author lifei114@126.com
 *
 */
@Data
public class MapperConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -414549696302263399L;
    public static final String PREFIX = "mapper";
    /**
     * 是否支持方法上的注解，默认false
     */
    private boolean enableMethodAnnotation;
  //使用简单类型
    private boolean useSimpleType    = true;
    //表名的默认前缀
    private String prefix = "";
    /**
     * 对于一般的getAllIfColumnNode，是否判断!=''，默认不判断
     */
    private boolean notEmpty;
}
