/**
 * 
 */
package titan.lightbatis.web.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import titan.lightbatis.table.TableSchema;

/**
 * @author lifei
 *
 */
@Data
@ApiModel(value="生成设置")
public class GenerateSettingVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7925686463332982944L;

	@ApiModelProperty("输出的配置信息")
	private OutputSetting setting = null;
	@ApiModelProperty("输出的表")
	private TableEntitySchema tableSchema = null;
}
