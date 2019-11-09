/**
 * 
 */
package titan.lightbatis.web;

import java.io.Serializable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lifei
 *
 */
@Data
@Api("实体类的输出描述")
public class EntityMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4458716388903187377L;

	@ApiModelProperty("表名")
	private String table = null;
	@ApiModelProperty("对应的 Entity 类名")
	private String beanClzName = null;
	@ApiModelProperty("该表的 Mapper 类名")
	private String mapperClzName = null;
	
}
