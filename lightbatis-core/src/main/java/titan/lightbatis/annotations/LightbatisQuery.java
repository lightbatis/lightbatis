/**
 * 
 */
package titan.lightbatis.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * 查询的注释, 极简极轻的查询注释接口，有这个接口的注释将被 QueryDSL 代替
 * @author lifei
 *
 */
public @interface LightbatisQuery {

}
