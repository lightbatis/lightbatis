/**
 * 
 */
package titan.lightbatis.web.generate.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生成的Mapper 的方法名称
 * @author lifei
 *
 */
@Data
@ApiModel(value="Mapper的方法描述", description="Mapper的方法描述")
public class MethodMeta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4918495585636178460L;
	@ApiModelProperty("方法描述")
	private String common = null;
	@ApiModelProperty("方法名称")
	private String methodName = null;
	@ApiModelProperty("方法返回值")
	private Type returnType = null;
	@ApiModelProperty("方法参数")
	private Parameter[] parameters = new Parameter[0];

	private List<String> paramCommons = new ArrayList<>();
	/**
	 * @param common
	 * @param methodName
	 * @param returnType
	 * @param parameters
	 */
	public MethodMeta(String common, String methodName, Type returnType, Parameter[] parameters) {
		super();
		this.common = common;
		this.methodName = methodName;
		this.returnType = returnType;
		this.parameters = parameters;
	}
	public void addParameterCommon(String common) {
		paramCommons.add(common);
	}
	
}
