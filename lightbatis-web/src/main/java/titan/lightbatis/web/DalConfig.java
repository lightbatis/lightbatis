/**
 * 
 */
package titan.lightbatis.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author lifei
 *
 */
@ConfigurationProperties(prefix = "dal")
@Configurable
@Data
@Component
public class DalConfig {

	//当前的启动类
	public static String startupClass = "";
	//当前的启动包
	public static String startupPackage = "";
	//当前启动程序的包
	public static String applicationPackage = "";
	
	private String basePackage = "";
}
