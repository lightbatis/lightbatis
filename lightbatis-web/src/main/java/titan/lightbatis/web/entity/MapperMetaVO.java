/**
 * 
 */
package titan.lightbatis.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import titan.lightbatis.web.generate.mapper.MethodMeta;

import java.util.List;
/**
 * @author lifei
 *
 */
@Data
@ApiModel(value="Mapper 类的信息")
public class MapperMetaVO {

	@ApiModelProperty("Mapper类生成的脚本")
	private String mapperScript = null;
	@ApiModelProperty("Mapper类")
	private String mapperClass = null;
	@ApiModelProperty("Service 类")
	private String serviceClass = null;
	@ApiModelProperty("Service类生成的脚本")
	private String serviceScript = null;
	@ApiModelProperty("对应的表名")
	private String tableName = null;
	//private List<MethodMeta> methods = null;
	/**
	 * 
	 */
	public MapperMetaVO() {
	}

}
