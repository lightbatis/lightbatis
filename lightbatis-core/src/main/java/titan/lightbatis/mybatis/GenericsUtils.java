package titan.lightbatis.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class GenericsUtils {

    public static Class getClassGenericType(Class clazz) {
        Type[] types= clazz.getGenericInterfaces();
        if (types.length > 0) {
            ParameterizedType type = (ParameterizedType) types[0];
            Type[] argTypes = type.getActualTypeArguments();
            if (argTypes.length > 0) {
                Class<?> eType = (Class<?>) argTypes[0];
                return eType;
            }
        }

        return null;
    }
    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     *
     * @param clz
     * @param method
     * @return
     */
    public static Class getImplementInterfaceParamType(Class clz, Method method, int index) {
         Type[] types = clz.getGenericInterfaces();
         for(Type type: types) {
             if ((type instanceof ParameterizedType)) {
                 ParameterizedType ptype = (ParameterizedType)type;
                 //System.out.println(ptype.getRawType());
                 if (ptype.getRawType().equals(method.getDeclaringClass())) {
                     Type[] argTypes = ptype.getActualTypeArguments();
                     if (index < argTypes.length) {
                         return (Class)argTypes[index];
                     }
                 }
             }
         }
        return null;
    }
}
