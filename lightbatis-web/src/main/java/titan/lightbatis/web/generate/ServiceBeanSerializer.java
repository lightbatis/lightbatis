/**
 * 
 */
package titan.lightbatis.web.generate;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.*;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import titan.lightbatis.web.generate.mapper.MethodMeta;

import javax.annotation.Generated;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

import static com.mysema.codegen.Symbols.COMMA;

/**
 * @author lifei
 *
 */
public class ServiceBeanSerializer implements Serializer {

	private Type proxyType = null;
	private final List<MethodMeta> methods = Lists.newArrayList();
    private static final Function<Property, Parameter> propertyToParameter = new Function<Property, Parameter>() {
        @Override
        public Parameter apply(Property input) {
            return new Parameter(input.getName(), input.getType());
        }
    };

    private final boolean propertyAnnotations;

    private final List<Type> interfaces = Lists.newArrayList();

    private final String javadocSuffix;

    private boolean addToString, addFullConstructor;

    private boolean printSupertype = false;

    /**
     * Create a new BeanSerializer
     */
    public ServiceBeanSerializer() {
        this(true, " is a Querydsl bean type");
    }

    /**
     * Create a new BeanSerializer with the given javadoc suffix
     *
     * @param javadocSuffix suffix to be used after the simple name in class level javadoc
     */
    public ServiceBeanSerializer(String javadocSuffix) {
        this(true, javadocSuffix);
    }

    /**
     * Create a new BeanSerializer
     *
     * @param propertyAnnotations true, to serialize property annotations
     */
    public ServiceBeanSerializer(boolean propertyAnnotations) {
        this(propertyAnnotations, " is a Querydsl bean type");
    }

    /**
     * Create a new BeanSerializer
     *
     * @param propertyAnnotations true, to serialize property annotations
     * @param javadocSuffix suffix to be used after the simple name in class level javadoc
     */
    public ServiceBeanSerializer(boolean propertyAnnotations, String javadocSuffix) {
        this.propertyAnnotations = propertyAnnotations;
        this.javadocSuffix = javadocSuffix;
    }
    
	public void addMethods(List<MethodMeta> mlist) {
		this.methods.addAll(mlist);
	}

    @Override
    public void serialize(EntityType model, SerializerConfig serializerConfig,
            CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();

        // package
        if (!model.getPackageName().isEmpty()) {
            writer.packageDecl(model.getPackageName());
        }

        // imports
        Set<String> importedClasses = getAnnotationTypes(model);
        for (Type iface : interfaces) {
            importedClasses.add(iface.getFullName());
        }
        importedClasses.add(Generated.class.getName());
        if (model.hasLists()) {
            importedClasses.add(List.class.getName());
        }
        if (model.hasCollections()) {
            importedClasses.add(Collection.class.getName());
        }
        if (model.hasSets()) {
            importedClasses.add(Set.class.getName());
        }
        if (model.hasMaps()) {
            importedClasses.add(Map.class.getName());
        }
        if (addToString && model.hasArrays()) {
            importedClasses.add(Arrays.class.getName());
        }
        if (printSupertype && model.getSuperType() != null) {
            importedClasses.add(model.getSuperType().getType().getFullName());
            ClassType clzType = (ClassType) model.getSuperType().getType();
            List<Type> types = clzType.getParameters();
            for (Type type:types) {
                importedClasses.add(type.getFullName());
            }
        }
//		for (MethodMeta meta : methods) {
//			Type returnType = meta.getReturnType();
//			if (!StringUtils.startsWith(returnType.getFullName(), "java.lang")) {
//				importedClasses.add(returnType.getFullName());
//			}
//
//			Parameter[] parameters = meta.getParameters();
//			for (Parameter p : parameters) {
//				if (!StringUtils.startsWith(p.getType().getFullName(), "java.lang")) {
//					importedClasses.add(p.getType().getFullName());
//				}
//
//			}
//		}
		
		if (proxyType != null) {
			importedClasses.add(proxyType.getFullName());
			importedClasses.add(Autowired.class.getName());
		}
		importedClasses.add(Service.class.getName());
        writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));

        // javadoc
        writer.javadoc(simpleName + javadocSuffix);

        // header
        for (Annotation annotation : model.getAnnotations()) {
            writer.annotation(annotation);
        }

        writer.line("@Generated(\"", getClass().getName(), "\")");
        writer.line("@Service");
        if (!interfaces.isEmpty()) {
            Type superType = null;
            if (printSupertype && model.getSuperType() != null) {
                superType = model.getSuperType().getType();
            }
            Type[] ifaces = interfaces.toArray(new Type[interfaces.size()]);
            writer.beginClass(model, superType, ifaces);
        } else if (printSupertype && model.getSuperType() != null) {
            writer.beginClass(model, model.getSuperType().getType());
        } else {
            writer.beginClass(model);
        }


        bodyStart(model, writer);

        if (addFullConstructor) {
            addFullConstructor(model, writer);
        }
        String proxyPropertyName = null;
        if (proxyType != null) {
        	proxyPropertyName = proxyType.getSimpleName();
        	proxyPropertyName = BeanUtils.uncapitalize(proxyPropertyName);
        	writer.annotation(new Autowired() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return Autowired.class;
				}
				
				@Override
				public boolean required() {
					return true;
				}
			});
        	 writer.privateField(proxyType, proxyPropertyName);
        }
        
        // fields
        for (Property property : model.getProperties()) {
            if (propertyAnnotations) {
                for (Annotation annotation : property.getAnnotations()) {
                    writer.annotation(annotation);
                }
            }
            writer.privateField(property.getType(), property.getEscapedName());
        }

        
        
        // accessors
        for (Property property : model.getProperties()) {
            String propertyName = property.getEscapedName();
            // getter
            writer.beginPublicMethod(property.getType(), "get" + BeanUtils.capitalize(propertyName));
            writer.line("return ", propertyName, ";");
            writer.end();
            // setter
            Parameter parameter = new Parameter(propertyName, property.getType());
            writer.beginPublicMethod(Types.VOID, "set" + BeanUtils.capitalize(propertyName), parameter);
            writer.line("this.", propertyName, " = ", propertyName, ";");
            writer.end();
        }

        
		// 开始创建所有的方法。
		for (MethodMeta meta : methods) {
			Type returnType = meta.getReturnType();
			List<String> commonLines = new ArrayList<>();
			commonLines.add(meta.getCommon());
			commonLines.addAll(meta.getParamCommons());
			String[] lines = commonLines.toArray(new String[commonLines.size()]);
			writer.javadoc(lines);
			writer.beginPublicMethod(returnType, meta.getMethodName(), meta.getParameters());
			
			writer.beginLine("return ",proxyPropertyName, ".", meta.getMethodName(), "(");
			Parameter[] params = meta.getParameters();
	        for (int i = 0; i < params.length; i++) {
	            if (i > 0) {
	            	writer.append(COMMA);
	            }
	            writer.append(params[i].getName());
	        }
			writer.append(");");
			writer.nl();
			writer.end();
		}
		
        if (addToString) {
            addToString(model, writer);
        }

        bodyEnd(model, writer);

        writer.end();
    }

    protected void addFullConstructor(EntityType model, CodeWriter writer) throws IOException {
        // public empty constructor
        writer.beginConstructor();
        writer.end();

        // full constructor
        writer.beginConstructor(model.getProperties(), propertyToParameter);
        for (Property property : model.getProperties()) {
            writer.line("this.", property.getEscapedName(), " = ", property.getEscapedName(), ";");
        }
        writer.end();
    }

    protected void addToString(EntityType model, CodeWriter writer) throws IOException {
        writer.line("@Override");
        writer.beginPublicMethod(Types.STRING, "toString");
        StringBuilder builder = new StringBuilder();
        for (Property property : model.getProperties()) {
            String propertyName = property.getEscapedName();
            if (builder.length() > 0) {
                builder.append(" + \", ");
            } else {
                builder.append("\"");
            }
            builder.append(propertyName + " = \" + ");
            if (property.getType().getCategory() == TypeCategory.ARRAY) {
                builder.append("Arrays.toString(" + propertyName + ")");
            } else {
                builder.append(propertyName);
            }
        }
        writer.line(" return ", builder.toString(), ";");
        writer.end();
    }

    protected void bodyStart(EntityType model, CodeWriter writer) throws IOException {
        // template method
    }

    protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
        // template method
    }

    private Set<String> getAnnotationTypes(EntityType model) {
        Set<String> imports = new HashSet<String>();
        for (Annotation annotation : model.getAnnotations()) {
            imports.add(annotation.annotationType().getName());
        }
        if (propertyAnnotations) {
            for (Property property : model.getProperties()) {
                for (Annotation annotation : property.getAnnotations()) {
                    imports.add(annotation.annotationType().getName());
                }
            }
        }
        return imports;
    }

    public void addInterface(Class<?> iface) {
        interfaces.add(new ClassType(iface));
    }

    public void addInterface(Type type) {
        interfaces.add(type);
    }

    public void setAddToString(boolean addToString) {
        this.addToString = addToString;
    }

    public void setAddFullConstructor(boolean addFullConstructor) {
        this.addFullConstructor = addFullConstructor;
    }

    public void setPrintSupertype(boolean printSupertype) {
        this.printSupertype = printSupertype;
    }


	public void setProxyType(Type proxyType) {
		this.proxyType = proxyType;
	}
	
	
}
