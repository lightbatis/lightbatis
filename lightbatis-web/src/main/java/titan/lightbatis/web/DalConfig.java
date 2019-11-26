/**
 * 
 */
package titan.lightbatis.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class DalConfig implements ApplicationContextAware {

	//当前的启动类
	private String startupClass = "";
	//当前的启动包
	private  String startupPackage = "";
	//当前启动程序的包
	private  String applicationPackage = "";

	private String basePackage = "";

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		StackTraceElement[] elements = new RuntimeException().getStackTrace();
		for (StackTraceElement element : elements) {
			if ("main".equals(element.getMethodName())) {
				String clzName = element.getClassName();
				String basePackage = StringUtils.substring(clzName, 0, StringUtils.lastIndexOf(clzName, "."));
				this.startupClass = clzName;
				this.applicationPackage = basePackage;
				this.startupPackage = basePackage;
				this.basePackage = basePackage;
				System.out.println("==================== applicationPackage");
			}
		}
	}
}
