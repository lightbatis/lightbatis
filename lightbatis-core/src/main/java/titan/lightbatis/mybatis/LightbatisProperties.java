package titan.lightbatis.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = LightbatisProperties.LIGHTBATIS_PREFIX)
public class LightbatisProperties {
	public static final String LIGHTBATIS_PREFIX = "lightbatis";

	private List<Class> mappers = new ArrayList<Class>();
	@NestedConfigurationProperty
	private Configuration configuration;

}
