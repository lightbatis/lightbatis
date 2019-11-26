/**
 * 
 */
package titan.lightbatis.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(TYPE)
/**
 * 统一数据层生成的实体类
 * @author lifei
 *
 */
public @interface DalEntity {
    /** The name of an entity. Defaults to the unqualified 
     * name of the entity class. This name is used to
     * refer to the entity in queries. The name must not be 
     * a reserved literal in the Java Persistence query language.  */
    String name() default "";
    
    String table() default "";
}
