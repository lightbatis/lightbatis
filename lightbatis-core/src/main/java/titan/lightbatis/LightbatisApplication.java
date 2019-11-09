package titan.lightbatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import titan.lightbatis.mybatis.interceptor.PageInterceptor;
/**
 * 基于 MyBatis 扩展的数据访问统一层
 */
/**
 * @author lifei114@126.com
 *
 */
@SpringBootApplication
public class LightbatisApplication {
    //@Autowired
    //private DataSource datasource = null;

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(LightbatisApplication.class, args);
    }
}
