package titan.lightbatis.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * 乐观锁
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Revision {
}
