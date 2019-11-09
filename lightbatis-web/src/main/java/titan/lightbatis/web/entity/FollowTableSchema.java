package titan.lightbatis.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import titan.lightbatis.table.TableSchema;

@ApiModel(value="从表的结构信息", description="从表的结构信息")
@Data
public class FollowTableSchema extends TableSchema {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8241192713373514312L;
	@ApiModelProperty(value = "主表关联字段")
    private String mainTableColumn;
    @ApiModelProperty(value = "重表关联字段")
    private String followTableColumn;
    @ApiModelProperty(value = "关联关系 1：一对一  2：一对多 重表传此值")
    private String  incidenceRelation;
    @ApiModelProperty(value = "一对多关联名称 重表传此值")
    private String associatedName;
    @ApiModelProperty(value = "一对多实体类地址 格式： com.xxx.xxx.User")
    private String classPath;


}
