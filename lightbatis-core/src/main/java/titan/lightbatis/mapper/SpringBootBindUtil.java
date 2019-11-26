/**
 * 
 */
package titan.lightbatis.mapper;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author lifei114@126.com
 *
 */
public class SpringBootBindUtil {

    public static final IBind BIND;

    static {
        IBind bind;
        try {
            //boot2
            Class.forName("org.springframework.boot.context.properties.bind.Binder");
            bind = new SpringBoot2Bind();
        } catch (Exception e) {
            //boot1
            bind = new SpringBoot1Bind();
        }
        BIND = bind;
    }

    public static <T> T bind(Environment environment, Class<T> targetClass, String prefix) {
        return BIND.bind(environment, targetClass, prefix);
    }

    public interface IBind {
        <T> T bind(Environment environment, Class<T> targetClass, String prefix);
    }

    /**
     * 使用 Spring Boot 1.x 方式绑定
     */
    public static class SpringBoot1Bind implements IBind {
        @Override
        public <T> T bind(Environment environment, Class<T> targetClass, String prefix) {
            /**
             为了方便以后直接依赖 Spring Boot 2.x 时不需要改动代码，这里也使用反射
             try {
             RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
             Map<String, Object> properties = resolver.getSubProperties("");
             T target = targetClass.newInstance();
             RelaxedDataBinder binder = new RelaxedDataBinder(target, prefix);
             binder.bind(new MutablePropertyValues(properties));
             return target;
             } catch (Exception e) {
             throw new RuntimeException(e);
             }
             下面是这段代码的反射实现
             */
            try {
                //反射提取配置信息
                Class<?> resolverClass = Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
                Constructor<?> resolverConstructor = resolverClass.getDeclaredConstructor(PropertyResolver.class);
                Method getSubPropertiesMethod = resolverClass.getDeclaredMethod("getSubProperties", String.class);
                Object resolver = resolverConstructor.newInstance(environment);
                Map<String, Object> properties = (Map<String, Object>) getSubPropertiesMethod.invoke(resolver, "");
                //创建结果类
                T target = targetClass.newInstance();
                //反射使用 org.springframework.boot.bind.RelaxedDataBinder
                Class<?> binderClass = Class.forName("org.springframework.boot.bind.RelaxedDataBinder");
                Constructor<?> binderConstructor = binderClass.getDeclaredConstructor(Object.class, String.class);
                Method bindMethod = binderClass.getMethod("bind", PropertyValues.class);
                //创建 binder 并绑定数据
                Object binder = binderConstructor.newInstance(target, prefix);
                bindMethod.invoke(binder, new MutablePropertyValues(properties));
                return target;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 使用 Spring Boot 2.x 方式绑定
     */
    public static class SpringBoot2Bind implements IBind {
        @Override
        public <T> T bind(Environment environment, Class<T> targetClass, String prefix) {
            /**
             由于不能同时依赖不同的两个版本，所以使用反射实现下面的代码
             Binder binder = Binder.get(environment);
             return binder.bind(prefix, targetClass).get();
             下面是这两行代码的完全反射版本
             */
            try {
                Class<?> bindClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
                Method getMethod = bindClass.getDeclaredMethod("get", Environment.class);
                Method bindMethod = bindClass.getDeclaredMethod("bind", String.class, Class.class);
                Object bind = getMethod.invoke(null, environment);
                Object bindResult = bindMethod.invoke(bind, prefix, targetClass);
                Method resultGetMethod = bindResult.getClass().getDeclaredMethod("get");
                Method isBoundMethod = bindResult.getClass().getDeclaredMethod("isBound");
                if ((Boolean) isBoundMethod.invoke(bindResult)) {
                    return (T) resultGetMethod.invoke(bindResult);
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
