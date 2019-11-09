package titan.lightbatis.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = {
		"titan.lightbatis",
	    "titan.lightbatis.web"
	})
@EnableSwagger2
//@MapperScan("titan.lightbatis.web.mapper")
@ConditionalOnWebApplication
public class DalWebConfig {
}
