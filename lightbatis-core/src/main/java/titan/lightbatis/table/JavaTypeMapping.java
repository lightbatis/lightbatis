package titan.lightbatis.table;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.primitives.Primitives;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.sql.types.*;

public class JavaTypeMapping {

    private static final Type<Object> DEFAULT = new ObjectType();

    private static final Map<Class<?>,Type<?>> defaultTypes = new HashMap<Class<?>,Type<?>>();

    static{
        registerDefault(new BigIntegerType());
        registerDefault(new BigDecimalType());
        registerDefault(new BlobType());
        registerDefault(new BooleanType());
        registerDefault(new BytesType());
        registerDefault(new ByteType());
        registerDefault(new CharacterType());
        registerDefault(new CalendarType());
        registerDefault(new ClobType());
        registerDefault(new CurrencyType());
        registerDefault(new DateType());
        registerDefault(new DoubleType());
        registerDefault(new FloatType());
        registerDefault(new IntegerType());
        registerDefault(new LocaleType());
        registerDefault(new LongType());
        registerDefault(new ObjectType());
        registerDefault(new ShortType());
        registerDefault(new StringType());
        registerDefault(new TimestampType());
        registerDefault(new TimeType());
        registerDefault(new URLType());
        registerDefault(new UtilDateType());
        registerDefault(new UtilUUIDType(false));

        // Joda time types
        registerDefault(new DateTimeType());
        registerDefault(new LocalDateTimeType());
        registerDefault(new LocalDateType());
        registerDefault(new LocalTimeType());

        // initialize java time api (JSR 310) converters only if java 8 is available
        try {
            Class.forName("java.time.Instant");
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310InstantType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalDateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalDateType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310OffsetDateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310OffsetTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310ZonedDateTimeType").newInstance());
        } catch (ClassNotFoundException e) {
            // converters for JSR 310 are not loaded
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static void registerDefault(Type<?> type) {
        defaultTypes.put(type.getReturnedClass(), type);
        Class<?> primitive = Primitives.unwrap(type.getReturnedClass());
        if (primitive != null) {
            defaultTypes.put(primitive, type);
        }
    }

    private final Map<Class<?>,Type<?>> typeByClass = new HashMap<Class<?>,Type<?>>();

    private final Map<Class<?>,Type<?>> resolvedTypesByClass = new HashMap<Class<?>,Type<?>>();

    private final Map<String, Map<String,Type<?>>> typeByColumn = new HashMap<String,Map<String,Type<?>>>();

    @Nullable
    public Type<?> getType(String table, String column) {
        Map<String,Type<?>> columns = typeByColumn.get(table);
        if (columns != null) {
            return columns.get(column);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Type<T> getType(Class<T> clazz) {
        Type<?> resolvedType = resolvedTypesByClass.get(clazz);
        if (resolvedType == null) {
            resolvedType = findType(clazz);
            if (resolvedType != null) {
                resolvedTypesByClass.put(clazz, resolvedType);
            } else {
                return (Type)DEFAULT;
            }
        }
        return (Type<T>) resolvedType;
    }

    @Nullable
    private Type<?> findType(Class<?> clazz) {
        //Look for a registered type in the class hierarchy
        Class<?> cl = clazz;
        do{
            if (typeByClass.containsKey(cl)) {
                return typeByClass.get(cl);
            } else if (defaultTypes.containsKey(cl)) {
                return defaultTypes.get(cl);
            }
            cl = cl.getSuperclass();
        } while(!cl.equals(Object.class));

        //Look for a registered type in any implemented interfaces
        Set<Class<?>> interfaces = ReflectionUtils.getImplementedInterfaces(clazz);
        for (Class<?> itf : interfaces) {
            if (typeByClass.containsKey(itf)) {
                return typeByClass.get(itf);
            } else if (defaultTypes.containsKey(itf)) {
                return defaultTypes.get(itf);
            }
        }
        return null;
    }

    public void register(Type<?> type) {
        typeByClass.put(type.getReturnedClass(), type);
        Class<?> primitive = Primitives.unwrap(type.getReturnedClass());
        if (primitive != null) {
            typeByClass.put(primitive, type);
        }
        // Clear previous resolved types, so they won't impact future lookups
        resolvedTypesByClass.clear();
    }

    public void setType(String table, String column, Type<?> type) {
        Map<String,Type<?>> columns = typeByColumn.get(table);
        if (columns == null) {
            columns = new HashMap<String, Type<?>>();
            typeByColumn.put(table, columns);
        }
        columns.put(column, type);
    }
}
