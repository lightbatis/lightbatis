/**
 * 
 */
package titan.lightbatis.web.generate;

import com.google.common.base.Function;
import com.mysema.codegen.AbstractCodeWriter;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.mysema.codegen.Symbols.COMMA;
import static com.mysema.codegen.Symbols.SPACE;

/**
 * @author lifei
 *
 */
public class JavaInterfaceCodeWriter extends AbstractCodeWriter<JavaInterfaceCodeWriter> {
    private static final String PUBLIC = "public ";

    private static final String PUBLIC_CLASS = "public class ";

    private static final String PUBLIC_FINAL = "public final ";

    private static final String PUBLIC_INTERFACE = "public interface ";

    private static final String PUBLIC_STATIC = "public static ";

    private static final String PUBLIC_STATIC_FINAL = "public static final ";
	private final Set<String> packages = new HashSet<String>();
	private final Set<String> classes = new HashSet<String>();
	private JavaWriter javaWriter = null;

	public JavaInterfaceCodeWriter(Appendable appendable) {
		super(appendable, 4);
		javaWriter = new JavaWriter(appendable);
		this.packages.add("java.lang");
	}

	public JavaInterfaceCodeWriter append(char c) throws IOException {
		javaWriter.append(c);
		return this;
	}

	public int hashCode() {
		return javaWriter.hashCode();
	}

	public JavaInterfaceCodeWriter append(CharSequence csq) throws IOException {
		javaWriter.append(csq);
		return this;
	}

	public JavaInterfaceCodeWriter append(CharSequence csq, int start, int end) throws IOException {
		javaWriter.append(csq, start, end);
		return this;
	}

	public JavaInterfaceCodeWriter beginLine(String... segments) throws IOException {
		javaWriter.beginLine(segments);
		return this;
	}

	public JavaInterfaceCodeWriter line(String... segments) throws IOException {
		javaWriter.line(segments);
		return this;
	}

	public JavaInterfaceCodeWriter nl() throws IOException {
		javaWriter.nl();
		return this;
	}

	public JavaInterfaceCodeWriter annotation(Annotation annotation) throws IOException {
		javaWriter.annotation(annotation);
		return this;
	}

	public boolean equals(Object obj) {
		return javaWriter.equals(obj);
	}

	public JavaInterfaceCodeWriter annotation(Class<? extends Annotation> annotation) throws IOException {
		javaWriter.annotation(annotation);
		return this;
	}

	public JavaInterfaceCodeWriter beginClass(Type type) throws IOException {
		//javaWriter.beginClass(type);
		return this;
	}

	public JavaInterfaceCodeWriter beginClass(Type type, Type superClass, Type... interfaces) throws IOException {
		javaWriter.beginClass(type, superClass, interfaces);
		packages.add(type.getPackageName());
		return this;
	}

	public <T> JavaInterfaceCodeWriter beginConstructor(Collection<T> parameters, Function<T, Parameter> transformer)
			throws IOException {
		javaWriter.beginConstructor(parameters, transformer);
		return this;
	}

	public JavaInterfaceCodeWriter beginConstructor(Parameter... parameters) throws IOException {
		javaWriter.beginConstructor(parameters);
		return this;
	}

	public JavaInterfaceCodeWriter beginInterface(Type type, Type... interfaces) throws IOException {
		javaWriter.beginInterface(type, interfaces);
		packages.add(type.getPackageName());
		return this;
	}

	public <T> JavaInterfaceCodeWriter beginPublicMethod(Type returnType, String methodName, Collection<T> parameters,
			Function<T, Parameter> transformer) throws IOException {
		javaWriter.beginPublicMethod(returnType, methodName, parameters, transformer);
		return this;
	}

	public JavaInterfaceCodeWriter beginPublicMethod(Type returnType, String methodName, Parameter... args)
			throws IOException {
		//javaWriter.beginPublicMethod(returnType, methodName, args);
		beginMethod(PUBLIC, returnType, methodName, args);
		return this;
	}

	public <T> JavaInterfaceCodeWriter beginStaticMethod(Type returnType, String methodName, Collection<T> parameters,
			Function<T, Parameter> transformer) throws IOException {
		javaWriter.beginStaticMethod(returnType, methodName, parameters, transformer);
		return this;
	}

	public JavaInterfaceCodeWriter beginStaticMethod(Type returnType, String methodName, Parameter... args)
			throws IOException {
		javaWriter.beginStaticMethod(returnType, methodName, args);
		return this;
	}

	public JavaInterfaceCodeWriter end() throws IOException {
		javaWriter.end();
		return this;
	}

	public JavaInterfaceCodeWriter field(Type type, String name) throws IOException {
		javaWriter.field(type, name);
		return this;
	}

	public String getClassConstant(String className) {
		return javaWriter.getClassConstant(className);
	}

	public String getGenericName(boolean asArgType, Type type) {
		return javaWriter.getGenericName(asArgType, type);
	}

	public String getRawName(Type type) {
		return javaWriter.getRawName(type);
	}

	public JavaInterfaceCodeWriter imports(Class<?>... imports) throws IOException {
		javaWriter.imports(imports);
		for (Class<?> cl : imports) {
			classes.add(cl.getName());
		}
		return this;
	}

	public JavaInterfaceCodeWriter imports(Package... imports) throws IOException {
		javaWriter.imports(imports);
		for (Package p : imports) {
			packages.add(p.getName());
		}
		return this;
	}

	public JavaInterfaceCodeWriter importClasses(String... imports) throws IOException {
		javaWriter.importClasses(imports);
		for (String cl : imports) {
			classes.add(cl);
		}
		return this;
	}

	public JavaInterfaceCodeWriter importPackages(String... imports) throws IOException {
		javaWriter.importPackages(imports);
		for (String p : imports) {
			packages.add(p);
		}
		return this;
	}

	public JavaInterfaceCodeWriter javadoc(String... lines) throws IOException {
		javaWriter.javadoc(lines);
		return this;
	}

	public JavaInterfaceCodeWriter packageDecl(String packageName) throws IOException {
		javaWriter.packageDecl(packageName);
		packages.add(packageName);
		return this;
	}

	public JavaInterfaceCodeWriter privateField(Type type, String name) throws IOException {
		javaWriter.privateField(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter privateFinal(Type type, String name) throws IOException {
		javaWriter.privateFinal(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter privateFinal(Type type, String name, String value) throws IOException {
		javaWriter.privateFinal(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter privateStaticFinal(Type type, String name, String value) throws IOException {
		javaWriter.privateStaticFinal(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter protectedField(Type type, String name) throws IOException {
		javaWriter.protectedField(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter protectedFinal(Type type, String name) throws IOException {
		javaWriter.protectedFinal(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter protectedFinal(Type type, String name, String value) throws IOException {
		javaWriter.protectedFinal(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter publicField(Type type, String name) throws IOException {
		javaWriter.publicField(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter publicField(Type type, String name, String value) throws IOException {
		javaWriter.publicField(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter publicFinal(Type type, String name) throws IOException {
		javaWriter.publicFinal(type, name);
		return this;
	}

	public JavaInterfaceCodeWriter publicFinal(Type type, String name, String value) throws IOException {
		javaWriter.publicFinal(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter publicStaticFinal(Type type, String name, String value) throws IOException {
		javaWriter.publicStaticFinal(type, name, value);
		return this;
	}

	public JavaInterfaceCodeWriter staticimports(Class<?>... imports) throws IOException {
		javaWriter.staticimports(imports);
		return this;
	}

	public JavaInterfaceCodeWriter suppressWarnings(String type) throws IOException {
		javaWriter.suppressWarnings(type);
		return this;
	}

	public CodeWriter suppressWarnings(String... types) throws IOException {
		return javaWriter.suppressWarnings(types);
	}

	private JavaInterfaceCodeWriter beginMethod(String modifiers, Type returnType, String methodName, Parameter... args)
			throws IOException {
		beginLine(modifiers, returnType.getGenericName(true, packages, classes), SPACE, methodName).params(args)
				.append(";").nl();
		return goIn();
	}

	private <T> JavaInterfaceCodeWriter params(Collection<T> parameters, Function<T, Parameter> transformer)
			throws IOException {
		append("(");
		boolean first = true;
		for (T param : parameters) {
			if (!first) {
				append(COMMA);
			}
			param(transformer.apply(param));
			first = false;
		}
		append(")");
		return this;
	}

	private JavaInterfaceCodeWriter params(Parameter... params) throws IOException {
		append("(");
		for (int i = 0; i < params.length; i++) {
			if (i > 0) {
				append(COMMA);
			}
			param(params[i]);
		}
		append(")");
		return this;
	}

	private JavaInterfaceCodeWriter param(Parameter parameter) throws IOException {
		append(parameter.getType().getGenericName(true, packages, classes));
		append(" ");
		append(parameter.getName());
		return this;
	}

}
