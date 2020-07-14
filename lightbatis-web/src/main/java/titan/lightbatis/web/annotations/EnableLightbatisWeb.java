/**
 * 
 */
package titan.lightbatis.web.annotations;

import org.springframework.context.annotation.Import;
import titan.lightbatis.web.config.DalWebConfig;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author lifei
 *
 */
@Documented
@Import({DalWebConfig.class})
public @interface EnableLightbatisWeb {

}
