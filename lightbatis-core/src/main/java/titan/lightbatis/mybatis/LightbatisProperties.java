package titan.lightbatis.mybatis;

import lombok.Data;
import org.apache.ibatis.session.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = LightbatisProperties.LIGHTBATIS_PREFIX)
public class LightbatisProperties {
	public static final String LIGHTBATIS_PREFIX = "lightbatis";

	private List<Class> mappers = new ArrayList<Class>();
	@NestedConfigurationProperty
	private Configuration configuration;

}
