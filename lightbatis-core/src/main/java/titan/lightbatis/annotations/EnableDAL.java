
package titan.lightbatis.annotations;

import org.springframework.context.annotation.Import;
import titan.lightbatis.configuration.LightbatisConfiguration;

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
@Import({LightbatisConfiguration.class})
public @interface EnableDAL {

}
