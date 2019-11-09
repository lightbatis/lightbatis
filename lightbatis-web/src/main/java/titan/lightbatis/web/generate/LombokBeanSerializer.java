/**
 * 
 */
package titan.lightbatis.web.generate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;

import lombok.Data;

/**
 * @author lifei
 *
 */
public class LombokBeanSerializer implements Serializer {
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
	 * @param propertyAnnotations
	 * @param javadocSuffix
	 */
	public LombokBeanSerializer(boolean propertyAnnotations, String javadocSuffix) {
		super();
		this.propertyAnnotations = propertyAnnotations;
		this.javadocSuffix = javadocSuffix;
	}

	@Override
	public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
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
		importedClasses.add(Data.class.getName());
		if (addToString && model.hasArrays()) {
			importedClasses.add(Arrays.class.getName());
		}
		writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));

		// javadoc
		writer.javadoc(simpleName + javadocSuffix);

		// header
		for (Annotation annotation : model.getAnnotations()) {
			writer.annotation(annotation);
		}

		writer.line("@Generated(\"", getClass().getName(), "\")");
		writer.line("@Data");
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
//		for (Property property : model.getProperties()) {
//			String propertyName = property.getEscapedName();
//			// getter
//			writer.beginPublicMethod(property.getType(), "get" + BeanUtils.capitalize(propertyName));
//			writer.line("return ", propertyName, ";");
//			writer.end();
//			// setter
//			Parameter parameter = new Parameter(propertyName, property.getType());
//			writer.beginPublicMethod(Types.VOID, "set" + BeanUtils.capitalize(propertyName), parameter);
//			writer.line("this.", propertyName, " = ", propertyName, ";");
//			writer.end();
//		}

		if (addToString) {
			addToString(model, writer);
		}

		bodyEnd(model, writer);

		writer.end();
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

	protected void bodyStart(EntityType model, CodeWriter writer) throws IOException {
		// template method
	}

	protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
		// template method
	}
    public void setAddFullConstructor(boolean addFullConstructor) {
        this.addFullConstructor = addFullConstructor;
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
}
