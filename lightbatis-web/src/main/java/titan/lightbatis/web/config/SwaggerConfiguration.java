/**
 * 
 */
package titan.lightbatis.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lifei
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean("dalApis")
	public Docket dalApis() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("统一数据层管理").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/dal.*")).build().apiInfo(apiInfo()).enable(true);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("数据统一层接口文档").description("基于 MyBatis 的数据层统一管理").termsOfServiceUrl("")
				.version("1.0").build();
	}

}
