/**
 * 
 */
package titan.lightbatis.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lifei
 *
 */
@Data
@ApiModel(value="输出的设置")
public class OutputSetting {

	 @ApiModelProperty(value = "输出文件夹地址")
	private String outDir = null;	 
	@ApiModelProperty("输出的包名")
	private String outPackage = "";
	@ApiModelProperty("生成 Swagger 注释")
	private boolean useSwagger = true;
	@ApiModelProperty("是否使用 Lombok 语法")
	private boolean useLombok = true;
	@ApiModelProperty("是否使用 QueryDSL")
	private boolean useQueryDSL = true;
	@ApiModelProperty("如果文件存在是否覆盖")
	private boolean overwrite = false;
	
	
}
