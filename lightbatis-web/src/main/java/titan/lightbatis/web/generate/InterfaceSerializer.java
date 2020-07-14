/**
 * 
 */
package titan.lightbatis.web.generate;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;
import org.apache.commons.lang3.StringUtils;
import titan.lightbatis.web.generate.mapper.MethodMeta;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 生成接口工具
 * 
 * @author lifei
 *
 */
public class InterfaceSerializer implements Serializer {
	private static final Function<Property, Parameter> propertyToParameter = new Function<Property, Parameter>() {
		@Override
		public Parameter apply(Property input) {
			return new Parameter(input.getName(), input.getType());
		}
	};

	private final boolean propertyAnnotations;

	private final List<Type> interfaces = Lists.newArrayList();
	private final List<MethodMeta> methods = Lists.newArrayList();
	private final String javadocSuffix;

	private boolean addToString, addFullConstructor;

	private boolean printSupertype = false;

	/**
	 * @param propertyAnnotations
	 * @param javadocSuffix
	 */
	public InterfaceSerializer(boolean propertyAnnotations, String javadocSuffix) {
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
			List<Type> params = iface.getParameters();
			for (Type ptype : params) {
				importedClasses.add(ptype.getFullName());
			}
		}
		for (MethodMeta meta : methods) {
			Type returnType = meta.getReturnType();
			if (!StringUtils.startsWith(returnType.getFullName(), "java.lang")) {
				importedClasses.add(returnType.getFullName());
			}

			Parameter[] parameters = meta.getParameters();
			for (Parameter p : parameters) {
				if (!StringUtils.startsWith(p.getType().getFullName(), "java.lang")) {
					importedClasses.add(p.getType().getFullName());
				}

			}
		}
		// importedClasses.add(Generated.class.getName());
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
		if (model.hasArrays()) {
			importedClasses.add(Arrays.class.getName());
		}
		writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));

		// javadoc
		writer.javadoc(simpleName + javadocSuffix);

		// header
		for (Annotation annotation : model.getAnnotations()) {
			writer.annotation(annotation);
		}

//		writer.line("@Generated(\"", getClass().getName(), "\")");
//		writer.line("@Data");
//		if (!interfaces.isEmpty()) {
//			Type superType = null;
//			if (printSupertype && model.getSuperType() != null) {
//				superType = model.getSuperType().getType();
//			}
//			Type[] ifaces = interfaces.toArray(new Type[interfaces.size()]);
//			writer.beginClass(model, superType, ifaces);
//		} else if (printSupertype && model.getSuperType() != null) {
//			writer.beginClass(model, model.getSuperType().getType());
//		} else {
//			writer.beginClass(model);
//		}
		Type[] ifaces = interfaces.toArray(new Type[interfaces.size()]);
		writer.beginInterface(model, ifaces);

		bodyStart(model, writer);

		// 开始创建所有的方法。
		for (MethodMeta meta : methods) {
			Type returnType = meta.getReturnType();
			List<String> commonLines = new ArrayList<>();
			commonLines.add(meta.getCommon());
			commonLines.addAll(meta.getParamCommons());
			String[] lines = commonLines.toArray(new String[commonLines.size()]);
			writer.javadoc(lines);
			writer.beginPublicMethod(returnType, meta.getMethodName(), meta.getParameters());
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

	public void addInterface(Class<?> iface, Type... params) {
//    	ClassType[] types = new ClassType[params.length];
//    	for (int i=0;i<params.length; i++) {
//    		types[i] = new ClassType(params[i]);
//    	}
//        interfaces.add(new ClassType(iface, types));
		interfaces.add(new ClassType(iface, params));
	}

	public void addMethods(List<MethodMeta> mlist) {
		this.methods.addAll(mlist);
	}

}
