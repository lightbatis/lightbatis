/**
 * 
 */
package titan.lightbatis.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import titan.lightbatis.exception.LightbatisException;
import titan.lightbatis.mybatis.meta.FieldMeta;


/**
 * @author lifei114@126.com
 *
 */
public class FieldUtils {

    private static final IFieldUtils fieldHelper;

    static {
        String version = System.getProperty("java.version");
        if (version.contains("1.6.") || version.contains("1.7.")) {
            fieldHelper = new Jdk6_7FieldHelper();
        } else {
            fieldHelper = new Jdk8FieldHelper();
        }
    }

    /**
     * 获取全部的Field
     *
     * @param entityClass
     * @return
     */
    public static List<FieldMeta> getFields(Class<?> entityClass) {
        return fieldHelper.getFields(entityClass);
    }

    /**
     * 获取全部的属性，通过方法名获取
     *
     * @param entityClass
     * @return
     */
    public static List<FieldMeta> getProperties(Class<?> entityClass) {
        return fieldHelper.getProperties(entityClass);
    }

    /**
     * 获取全部的属性，包含字段和方法
     *
     * @param entityClass
     * @return
     * @throws IntrospectionException
     */
    public static List<FieldMeta> getAll(Class<?> entityClass) {
        List<FieldMeta> fields = fieldHelper.getFields(entityClass);
        List<FieldMeta> properties = fieldHelper.getProperties(entityClass);
        //拼到一起，名字相同的合并
        List<FieldMeta> all = new ArrayList<FieldMeta>();
        Set<FieldMeta> usedSet = new HashSet<FieldMeta>();
        for (FieldMeta field : fields) {
            for (FieldMeta property : properties) {
                if (!usedSet.contains(property) && field.getName().equals(property.getName())) {
                    field.copyFromPropertyDescriptor(property);
                    usedSet.add(property);
                    break;
                }
            }
            all.add(field);
        }
        for (FieldMeta property : properties) {
            if (!usedSet.contains(property)) {
                all.add(property);
            }
        }
        return all;
    }

    /**
     * Field接口
     */
    interface IFieldUtils {
        /**
         * 获取全部的Field
         *
         * @param entityClass
         * @return
         */
        List<FieldMeta> getFields(Class<?> entityClass);

        /**
         * 获取全部的属性，通过方法名获取
         *
         * @param entityClass
         * @return
         */
        List<FieldMeta> getProperties(Class<?> entityClass);
    }

    /**
     * 支持jdk8
     */
    static class Jdk8FieldHelper implements IFieldUtils {
        /**
         * 获取全部的Field
         *
         * @param entityClass
         * @return
         */
        @Override
        public List<FieldMeta> getFields(Class<?> entityClass) {
            List<FieldMeta> fields = _getFields(entityClass, null, null);
            List<FieldMeta> properties = getProperties(entityClass);
            Set<FieldMeta> usedSet = new HashSet<FieldMeta>();
            for (FieldMeta field : fields) {
                for (FieldMeta property : properties) {
                    if (!usedSet.contains(property) && field.getName().equals(property.getName())) {
                        //泛型的情况下通过属性可以得到实际的类型
                        field.setJavaType(property.getJavaType());
                        break;
                    }
                }
            }
            return fields;
        }

        /**
         * 获取全部的Field，仅仅通过Field获取
         *
         * @param entityClass
         * @param fieldList
         * @param level
         * @return
         */
        private List<FieldMeta> _getFields(Class<?> entityClass, List<FieldMeta> fieldList, Integer level) {
            if (fieldList == null) {
                fieldList = new ArrayList<FieldMeta>();
            }
            if (level == null) {
                level = 0;
            }
            if (entityClass.equals(Object.class)) {
                return fieldList;
            }
            Field[] fields = entityClass.getDeclaredFields();
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //排除静态字段，解决bug#2
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                    if (level.intValue() != 0) {
                        //将父类的字段放在前面
                        fieldList.add(index, new FieldMeta(field, null));
                        index++;
                    } else {
                        fieldList.add(new FieldMeta(field, null));
                    }
                }
            }
            Class<?> superClass = entityClass.getSuperclass();
            if (superClass != null
                    && !superClass.equals(Object.class)
                    && (superClass.isAnnotationPresent(Entity.class)
                    || (!Map.class.isAssignableFrom(superClass)
                    && !Collection.class.isAssignableFrom(superClass)))) {
                return _getFields(entityClass.getSuperclass(), fieldList, ++level);
            }
            return fieldList;
        }

        /**
         * 通过方法获取属性
         *
         * @param entityClass
         * @return
         */
        @Override
        public List<FieldMeta> getProperties(Class<?> entityClass) {
            List<FieldMeta> entityFields = new ArrayList<FieldMeta>();
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(entityClass);
            } catch (IntrospectionException e) {
                throw new LightbatisException(e);
            }
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor desc : descriptors) {
                if (!"class".equals(desc.getName())) {
                    entityFields.add(new FieldMeta(null, desc));
                }
            }
            return entityFields;
        }
    }

    /**
     * 支持jdk6,7
     */
    static class Jdk6_7FieldHelper implements IFieldUtils {

        @Override
        public List<FieldMeta> getFields(Class<?> entityClass) {
            List<FieldMeta> fieldList = new ArrayList<FieldMeta>();
            _getFields(entityClass, fieldList, _getGenericTypeMap(entityClass), null);
            return fieldList;
        }

        /**
         * 通过方法获取属性
         *
         * @param entityClass
         * @return
         */
        @Override
        public List<FieldMeta> getProperties(Class<?> entityClass) {
            Map<String, Class<?>> genericMap = _getGenericTypeMap(entityClass);
            List<FieldMeta> entityFields = new ArrayList<FieldMeta>();
            BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(entityClass);
            } catch (IntrospectionException e) {
                throw new LightbatisException(e);
            }
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor desc : descriptors) {
                if (desc != null && !"class".equals(desc.getName())) {
                    FieldMeta entityField = new FieldMeta(null, desc);
                    if (desc.getReadMethod() != null
                            && desc.getReadMethod().getGenericReturnType() != null
                            && desc.getReadMethod().getGenericReturnType() instanceof TypeVariable) {
                        entityField.setJavaType(genericMap.get(((TypeVariable) desc.getReadMethod().getGenericReturnType()).getName()));
                    } else if (desc.getWriteMethod() != null
                            && desc.getWriteMethod().getGenericParameterTypes() != null
                            && desc.getWriteMethod().getGenericParameterTypes().length == 1
                            && desc.getWriteMethod().getGenericParameterTypes()[0] instanceof TypeVariable) {
                        entityField.setJavaType(genericMap.get(((TypeVariable) desc.getWriteMethod().getGenericParameterTypes()[0]).getName()));
                    }
                    entityFields.add(entityField);
                }
            }
            return entityFields;
        }

        /**
         * @param entityClass
         * @param fieldList
         * @param genericMap
         * @param level
         */
        private void _getFields(Class<?> entityClass, List<FieldMeta> fieldList, Map<String, Class<?>> genericMap, Integer level) {
            if (fieldList == null) {
                throw new NullPointerException("fieldList参数不能为空!");
            }
            if (level == null) {
                level = 0;
            }
            if (entityClass == Object.class) {
                return;
            }
            Field[] fields = entityClass.getDeclaredFields();
            int index = 0;
            for (Field field : fields) {
                //忽略static和transient字段#106
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                    FieldMeta entityField = new FieldMeta(field, null);
                    if (field.getGenericType() != null && field.getGenericType() instanceof TypeVariable) {
                        if (genericMap == null || !genericMap.containsKey(((TypeVariable) field.getGenericType()).getName())) {
                            throw new LightbatisException(entityClass + "字段" + field.getName() + "的泛型类型无法获取!");
                        } else {
                            entityField.setJavaType(genericMap.get(((TypeVariable) field.getGenericType()).getName()));
                        }
                    } else {
                        entityField.setJavaType(field.getType());
                    }
                    if (level.intValue() != 0) {
                        //将父类的字段放在前面
                        fieldList.add(index, entityField);
                        index++;
                    } else {
                        fieldList.add(entityField);
                    }
                }
            }
            //获取父类和泛型信息
            Class<?> superClass = entityClass.getSuperclass();
            //判断superClass
            if (superClass != null
                    && !superClass.equals(Object.class)
                    && (superClass.isAnnotationPresent(Entity.class)
                    || (!Map.class.isAssignableFrom(superClass)
                    && !Collection.class.isAssignableFrom(superClass)))) {
                level++;
                _getFields(superClass, fieldList, genericMap, level);
            }
        }

        /**
         * 获取所有泛型类型映射
         *
         * @param entityClass
         */
        private Map<String, Class<?>> _getGenericTypeMap(Class<?> entityClass) {
            Map<String, Class<?>> genericMap = new HashMap<String, Class<?>>();
            if (entityClass == Object.class) {
                return genericMap;
            }
            //获取父类和泛型信息
            Class<?> superClass = entityClass.getSuperclass();
            //判断superClass
            if (superClass != null
                    && !superClass.equals(Object.class)
                    && (superClass.isAnnotationPresent(Entity.class)
                    || (!Map.class.isAssignableFrom(superClass)
                    && !Collection.class.isAssignableFrom(superClass)))) {
                if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
                    TypeVariable[] typeVariables = superClass.getTypeParameters();
                    if (typeVariables.length > 0) {
                        for (int i = 0; i < typeVariables.length; i++) {
                            if (types[i] instanceof Class) {
                                genericMap.put(typeVariables[i].getName(), (Class<?>) types[i]);
                            }
                        }
                    }
                }
                genericMap.putAll(_getGenericTypeMap(superClass));
            }
            return genericMap;
        }
    }
}
