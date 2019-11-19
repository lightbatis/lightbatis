/**
 * 
 */
package titan.lightbatis.web.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import titan.lightbatis.web.config.DalWebConfig;

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
