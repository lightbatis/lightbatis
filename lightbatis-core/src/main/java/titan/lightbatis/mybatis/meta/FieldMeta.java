/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import lombok.Data;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lifei114@126.com
 *
 */
@Data
public class FieldMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7234604201577249693L;
    private String name;
    private Field field;
    private Class<?> javaType;
    private Method setter;
    private Method getter;
    

    /**
     * 构造方法
     *
     * @param field              字段
     * @param propertyDescriptor 字段name对应的property
     */
    public FieldMeta(Field field, PropertyDescriptor propertyDescriptor) {
        if (field != null) {
            this.field = field;
            this.name = field.getName();
            this.javaType = field.getType();
        }
        if (propertyDescriptor != null) {
            this.name = propertyDescriptor.getName();
            this.setter = propertyDescriptor.getWriteMethod();
            this.getter = propertyDescriptor.getReadMethod();
            this.javaType = propertyDescriptor.getPropertyType();
        }
    }

    /**
     * 先创建field，然后可以通过该方法获取property等属性
     *
     * @param other
     */
    public void copyFromPropertyDescriptor(FieldMeta other) {
        this.setter = other.setter;
        this.getter = other.getter;
        this.javaType = other.javaType;
        this.name = other.name;
    }

    /**
     * 是否有该注解
     *
     * @param annotationClass
     * @return
     */
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        boolean result = false;
        if (field != null) {
            result = field.isAnnotationPresent(annotationClass);
        }
        if (!result && setter != null) {
            result = setter.isAnnotationPresent(annotationClass);
        }
        if (!result && getter != null) {
            result = getter.isAnnotationPresent(annotationClass);
        }
        return result;
    }

    /**
     * 获取指定的注解
     *
     * @param annotationClass
     * @param <T>
     * @return
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T result = null;
        if (field != null) {
            result = field.getAnnotation(annotationClass);
        }
        if (result == null && setter != null) {
            result = setter.getAnnotation(annotationClass);
        }
        if (result == null && getter != null) {
            result = getter.getAnnotation(annotationClass);
        }
        return result;
    }

    /**
     * 反射获取值
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object getValue(Object object) throws IllegalAccessException, InvocationTargetException {
        Object result = null;
        if (getter != null) {
            result = getter.invoke(object);
        } else if (field != null) {
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            result = field.get(object);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMeta that = (FieldMeta) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    /**
     * 获取javaType
     *
     * @return
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * 设置javaType
     *
     * @param javaType
     */
    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * 字段属性名
     *
     * @return
     */
    public String getName() {
        return name;
    }
    
}
