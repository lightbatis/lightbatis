/**
 * 
 */
package titan.lightbatis.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.mybatis.LightbatisFactoryBean;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author lifei114@126.com
 *
 */
@Import(LightbatisScannerRegistrar.class)
public @interface Lightbatis {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise
     * annotation declarations e.g.:
     * {@code @EnableMyBatisMapperScanner("org.my.pkg")} instead of {@code
     * @EnableMyBatisMapperScanner(basePackages= "org.my.pkg"})}.
     */
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages
     * to scan for annotated components. The package of each class specified will be scanned.
     * <p>Consider creating a special no-op marker class or interface in each package
     * that serves no purpose other than being referenced by this attribute.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components
     * within the Spring container.
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified interface class as a parent.
     * <p>
     * Note this can be combined with annotationClass.
     */
    Class<?> markerInterface() default LightbatisMapper.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is
     * more than one in the spring context. Usually this is only needed when you
     * have more than one datasource.
     */
    String sqlSessionTemplateRef() default "";

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is
     * more than one in the spring context. Usually this is only needed when you
     * have more than one datasource.
     */
    String sqlSessionFactoryRef() default "";

    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     */
    Class<? extends LightbatisFactoryBean> factoryBean() default LightbatisFactoryBean.class;

    /**
     * 通用 Mapper 的配置，一行一个配置
     *
     * @return
     */
    String[] properties() default {};

    /**
     * 还可以直接配置一个 MapperHelper bean
     *
     * @return
     */
    String mapperHelperRef() default "";
}
