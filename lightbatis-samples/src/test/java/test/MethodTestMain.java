package test;

import titan.lightbatis.mybatis.GenericsUtils;
import titan.lightbatis.sample.mapper.MemberCrudMapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

public class MethodTestMain {
    public static void main(String[] args) {
        Class<MemberCrudMapper> clz = MemberCrudMapper.class;
        Method[] ms = clz.getMethods();
        for (Method m: ms) {
            //System.out.println(m);

            if (m.getName().equals("get")) {
                GenericsUtils.getImplementInterfaceParamType(MemberCrudMapper.class, m, 0);
//                Type returnType = m.getGenericReturnType();
//                System.out.println(returnType);
//                if (returnType instanceof ParameterizedType) {
//                    ParameterizedType pType = (ParameterizedType)returnType;
//                    System.out.print("返回泛型类型:");
//                    Stream.of(pType.getActualTypeArguments()).forEach(System.out::print);
//                    System.out.println();
//                }
//                System.out.println(clz.getGenericInterfaces().length);
//                System.out.println(m.getDeclaringClass());
//               Type[] types = clz.getGenericInterfaces();
//                for (Type type: types) {
//                    System.out.println(type);
//                    if (type instanceof ParameterizedType) {
//                        ParameterizedType pType = (ParameterizedType)type;
//                        System.out.println("返回泛型类型:");
//                        Stream.of(pType.getActualTypeArguments()).forEach(type1 -> {
//                            System.out.println(type1.getTypeName());
//                        });
//                        System.out.println();
//                    }
//                }
            }
        }
        TypeVariable[] tValue = MemberCrudMapper.class.getTypeParameters();
        System.out.println(tValue.length);
    }
}
