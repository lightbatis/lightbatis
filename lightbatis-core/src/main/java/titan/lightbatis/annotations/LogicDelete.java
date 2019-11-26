/**
 *
 */
package titan.lightbatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * 逻辑删除
 */
public @interface LogicDelete {

    /**
     * 删除
     * @return
     */
    int isDeleted() default 1;

    /**
     * 没有删除
     * @return
     */
    int noDeleted() default 0;
}
