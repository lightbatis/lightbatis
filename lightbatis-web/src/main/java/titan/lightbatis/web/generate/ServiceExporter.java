/**
 * 
 */
package titan.lightbatis.web.generate;

import com.google.common.base.Function;
import com.google.common.io.Files;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.*;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.SQLCodegenModule;
import com.querydsl.sql.codegen.SpatialSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.web.entity.TableEntitySchema;
import titan.lightbatis.web.generate.mapper.MethodMeta;
import titan.lightbatis.web.service.CrudService;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author lifei
 *
 */
public class ServiceExporter {

	private static final Logger logger = LoggerFactory.getLogger(TableSchemaExporter.class);

	// private final SQLTemplatesRegistry sqlTemplatesRegistry = new
	// SQLTemplatesRegistry();

	private final SQLCodegenModule module = new SQLCodegenModule();

	private final Set<String> classes = new HashSet<String>();

	private File targetFolder;

	private File beansTargetFolder;

	@Nullable
	private String beanPackageName;

	@Nullable
	private ServiceBeanSerializer beanSerializer;

	@Nullable
	private String beanClassName;

	private boolean createScalaSources = false;

	private final Map<EntityType, Type> entityToWrapped = new HashMap<EntityType, Type>();

	private TypeMappings typeMappings;

	private QueryTypeFactory queryTypeFactory;

	private NamingStrategy namingStrategy = new DefaultNamingStrategy();

	private boolean schemaToPackage = false;

	// 列是否使用 @ApiModelProperty 属性
	private boolean columnPropertyAnnotations = true;

	private String sourceEncoding = "UTF-8";

	private boolean lowerCase = false;

	private boolean spatial = false;

	private List<MethodMeta> methods = new ArrayList<>();
	@Nullable
	private String tableTypesToExport;

	public ServiceExporter() {
	}

	protected EntityType createServiceType(SchemaAndTable schemaAndTable, final String className) {
		EntityType classModel;

		String beanPackage = normalizePackage(beanPackageName, schemaAndTable);
		String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
		Type classTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackage + "." + simpleName, beanPackage,
				simpleName, false, false);
		classModel = new EntityType(classTypeModel,
				module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

		Type mappedType = queryTypeFactory.create(classModel);
		entityToWrapped.put(classModel, mappedType);
		typeMappings.register(classModel, mappedType);

		return classModel;
	}
	protected EntityType createEntityType(TableEntitySchema tableSchema, SchemaAndTable schemaAndTable, final String className) {
		EntityType classModel;

		String beanPackage = normalizePackage(tableSchema.getEntityPackageName(), schemaAndTable);
		String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
		Type classTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackage + "." + simpleName, beanPackage,
				simpleName, false, false);
		classModel = new EntityType(classTypeModel,
				module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

		Type mappedType =classModel;// queryTypeFactory.create(classModel);
		entityToWrapped.put(classModel, mappedType);
		typeMappings.register(classModel, mappedType);

		return classModel;
	}
	private EntityType createMapperType(TableEntitySchema tableSchema, SchemaAndTable schemaAndTable, final String className) {
		EntityType classModel;

		String beanPackage = normalizePackage(tableSchema.getMapperPackageName(), schemaAndTable);
		String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
		Type classTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackage + "." + simpleName, beanPackage,
				simpleName, false, false);
		classModel = new EntityType(classTypeModel,
				module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

		TypeCategory category;
		Class clazz;
		Type mappedType = classModel;
		entityToWrapped.put(classModel, mappedType);
		typeMappings.register(classModel, mappedType);

		return classModel;
	}

	private String normalizePackage(String packageName, SchemaAndTable schemaAndTable) {
		String rval = packageName;
		if (schemaToPackage) {
			rval = namingStrategy.getPackage(rval, schemaAndTable);
		}
		return rval;
	}

	protected Property createProperty(EntityType classModel, String normalizedColumnName, String propertyName,
			Type typeModel) {
		return new Property(classModel, propertyName, propertyName, typeModel, Collections.<String>emptyList(), false);
	}

	public void export(TableEntitySchema tableSchema) throws Throwable {
		if (beanPackageName == null) {
			beanPackageName = module.getPackageName();
		}
		if (beansTargetFolder == null) {
			beansTargetFolder = targetFolder;
		}
		if (spatial) {
			SpatialSupport.addSupport(module);
		}

		classes.clear();
		typeMappings = module.get(TypeMappings.class);
		queryTypeFactory = module.get(QueryTypeFactory.class);
		namingStrategy = module.get(NamingStrategy.class);
		String tableName = tableSchema.getTableName();

		String normalizedSchemaName = namingStrategy.normalizeSchemaName(tableSchema.getDbSchema());
		String normalizedTableName = namingStrategy.normalizeTableName(tableName);

		SchemaAndTable schemaAndTable = new SchemaAndTable(normalizedSchemaName, normalizedTableName);

		if (!namingStrategy.shouldGenerateClass(schemaAndTable)) {
			return;
		}

		String className = tableSchema.getServiceClzName();// namingStrategy.getClassName(schemaAndTable);
		EntityType classModel = createEntityType(tableSchema, schemaAndTable, className);

		// serialize model
		String fileSuffix = createScalaSources ? ".scala" : ".java";

		String packageName = normalizePackage(beanPackageName, schemaAndTable);
		String path = packageName.replace('.', '/') + "/" + classModel.getSimpleName() + fileSuffix;

		File targetFile = new File(beansTargetFolder, path);
		if (!classes.add(targetFile.getPath())) {
			throw new IllegalStateException("Attempted to write multiple times to " + targetFile.getPath()
					+ ", please check your configuration");
		}
		StringWriter w = exportCode(tableSchema);
		// conditional creation
		boolean generate = true;
		byte[] bytes = w.toString().getBytes(sourceEncoding);
		if (targetFile.exists() && targetFile.length() == bytes.length) {
			String str = Files.toString(targetFile, Charset.forName(sourceEncoding));
			if (str.equals(w.toString())) {
				generate = false;
			}
		} else {
			targetFile.getParentFile().mkdirs();
		}

		if (generate) {
			Files.write(bytes, targetFile);
		}
	}

	public StringWriter exportCode(TableEntitySchema tableSchema) throws Throwable {
		if (beanPackageName == null) {
			beanPackageName = module.getPackageName();
		}
		if (beansTargetFolder == null) {
			beansTargetFolder = targetFolder;
		}
		module.bind(SQLCodegenModule.BEAN_PACKAGE_NAME, beanPackageName);

		if (spatial) {
			SpatialSupport.addSupport(module);
		}

		classes.clear();
		typeMappings = module.get(TypeMappings.class);
		queryTypeFactory = module.get(QueryTypeFactory.class);
		namingStrategy = module.get(NamingStrategy.class);
		// configuration = module.get(Configuration.class);

		String tableName = tableSchema.getTableName();

		String normalizedSchemaName = namingStrategy.normalizeSchemaName(tableSchema.getDbSchema());
		String normalizedTableName = namingStrategy.normalizeTableName(tableName);

		SchemaAndTable schemaAndTable = new SchemaAndTable(normalizedSchemaName, normalizedTableName);

		if (!namingStrategy.shouldGenerateClass(schemaAndTable)) {
			return null;
		}

		String className = beanClassName;
		if (className == null)
			className = tableSchema.getMapperClzName();// namingStrategy.getClassName(schemaAndTable);
		EntityType classModel = createServiceType(schemaAndTable, className);
		
//		String beanPackage = tableSchema.getMapperPackageName();
//		String simpleName = tableSchema.getMapperClzName();
//		Type proxyClassTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackage + "." + simpleName, beanPackage,
//				simpleName, false, false);
		
		// serialize model
		//beanSerializer.addInterface(LightbatisMapper.class, getEntityType(tableSchema));
		Type pkType = getPrimaryKeyType(tableSchema);

		if (pkType != null){
			String mapperClzName = tableSchema.getMapperClzName();// namingStrategy.getClassName(schemaAndTable);
			EntityType mapperClassModel = createMapperType(tableSchema, schemaAndTable, mapperClzName);
			EntityType entityType = createEntityType(tableSchema, schemaAndTable, tableSchema.getEntityName());
			ClassType clzType = new ClassType(CrudService.class,mapperClassModel,entityType,pkType);
			Supertype supertype = new Supertype(clzType);
			classModel.addSupertype(supertype);

			//classModel.addSupertype();
			//beanSerializer.addInterface(CrudService.class, getEntityType(tableSchema), getPrimaryKeyType(tableSchema));
		}

		beanSerializer.addMethods(methods);
		//beanSerializer.setProxyType(proxyClassTypeModel);
		
		StringWriter w = write(beanSerializer, classModel);
		return w;

	}
	private Type getPrimaryKeyType(TableEntitySchema tableSchema) {
		List<ColumnSchema> columnSchemas = tableSchema.getColumns();
		for (ColumnSchema columnSchema: columnSchemas) {
			if (columnSchema.isPrimary()) {
				Class clz = columnSchema.getColumnClz();
				Type clzType = new ClassType(clz);
				return clzType;
			}
		}
		return null;
	}

	Set<String> getClasses() {
		return classes;
	}

	private Type getEntityType(TableEntitySchema tableSchema) {
		String packageName = tableSchema.getEntityPackageName();
		String simpleName = tableSchema.getEntityName();
		Type classTypeModel = new SimpleType(TypeCategory.ENTITY, packageName + "." + simpleName, packageName,
				simpleName, false, false);
		return classTypeModel;
	}

	private StringWriter write(Serializer serializer, EntityType type) throws IOException {

		StringWriter w = new StringWriter();
		CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
		serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);

		return w;
	}

	/**
	 * Override the configuration
	 *
	 * @param configuration override configuration for custom type mappings etc
	 */
	public void setConfiguration(Configuration configuration) {
		module.bind(Configuration.class, configuration);
	}

	/**
	 * Set true to create Scala sources instead of Java sources
	 *
	 * @param createScalaSources whether to create Scala sources (default: false)
	 */
	public void setCreateScalaSources(boolean createScalaSources) {
		this.createScalaSources = createScalaSources;
	}

	/**
	 * Set the target folder
	 *
	 * @param targetFolder target source folder to create the sources into (e.g.
	 *                     target/generated-sources/java)
	 */
	public void setTargetFolder(File targetFolder) {
		this.targetFolder = targetFolder;
	}

	/**
	 * Set the target folder for beans
	 *
	 * <p>
	 * defaults to the targetFolder value
	 * </p>
	 *
	 * @param targetFolder target source folder to create the bean sources into
	 */
	public void setBeansTargetFolder(File targetFolder) {
		this.beansTargetFolder = targetFolder;
	}

	/**
	 * Set the package name
	 *
	 * @param packageName package name for sources
	 */
	public void setPackageName(String packageName) {
		module.bind(SQLCodegenModule.PACKAGE_NAME, packageName);
	}

	/**
	 * Override the bean package name (default: packageName)
	 *
	 * @param beanPackageName package name for bean sources
	 */
	public void setBeanPackageName(@Nullable String beanPackageName) {
		this.beanPackageName = beanPackageName;
	}

	/**
	 * Override the name prefix for the classes (default: Q)
	 *
	 * @param namePrefix name prefix for querydsl-types (default: Q)
	 */
	public void setNamePrefix(String namePrefix) {
		module.bind(CodegenModule.PREFIX, namePrefix);
	}

	/**
	 * Override the name suffix for the classes (default: "")
	 *
	 * @param nameSuffix name suffix for querydsl-types (default: "")
	 */
	public void setNameSuffix(String nameSuffix) {
		module.bind(CodegenModule.SUFFIX, nameSuffix);
	}

	/**
	 * Override the bean prefix for the classes (default: "")
	 *
	 * @param beanPrefix bean prefix for bean-types (default: "")
	 */
	public void setBeanPrefix(String beanPrefix) {
		module.bind(SQLCodegenModule.BEAN_PREFIX, beanPrefix);
	}

	/**
	 * Override the bean suffix for the classes (default: "")
	 *
	 * @param beanSuffix bean suffix for bean-types (default: "")
	 */
	public void setBeanSuffix(String beanSuffix) {
		module.bind(SQLCodegenModule.BEAN_SUFFIX, beanSuffix);
	}

	/**
	 * Override the NamingStrategy (default: new DefaultNamingStrategy())
	 *
	 * @param namingStrategy naming strategy to override (default: new
	 *                       DefaultNamingStrategy())
	 */
	public void setNamingStrategy(NamingStrategy namingStrategy) {
		module.bind(NamingStrategy.class, namingStrategy);
	}

	/**
	 * Set the Bean serializer to create bean types as well
	 *
	 * @param beanSerializer serializer for JavaBeans (default: null)
	 */
	public void setBeanSerializer(@Nullable ServiceBeanSerializer beanSerializer) {
		module.bind(SQLCodegenModule.BEAN_SERIALIZER, beanSerializer);
		this.beanSerializer = beanSerializer;
		this.beanSerializer.setPrintSupertype(true);
	}

	/**
	 * Set the Bean serializer class to create bean types as well
	 *
	 * @param beanSerializerClass serializer for JavaBeans (default: null)
	 */
	public void setBeanSerializerClass(Class<? extends Serializer> beanSerializerClass) {
		module.bind(SQLCodegenModule.BEAN_SERIALIZER, beanSerializerClass);
	}

	/**
	 * Set whether inner classes should be created for keys
	 *
	 * @param innerClassesForKeys
	 */
	public void setInnerClassesForKeys(boolean innerClassesForKeys) {
		module.bind(SQLCodegenModule.INNER_CLASSES_FOR_KEYS, innerClassesForKeys);
	}

	/**
	 * Set the column comparator class
	 *
	 * @param columnComparatorClass
	 */
	public void setColumnComparatorClass(Class<? extends Comparator<Property>> columnComparatorClass) {
		module.bind(SQLCodegenModule.COLUMN_COMPARATOR, columnComparatorClass);
	}

	/**
	 * Set the serializer class
	 *
	 * @param serializerClass
	 */
	public void setSerializerClass(Class<? extends Serializer> serializerClass) {
		module.bind(Serializer.class, serializerClass);
	}

	/**
	 * Set the type mappings to use
	 *
	 * @param typeMappings
	 */
	public void setTypeMappings(TypeMappings typeMappings) {
		module.bind(TypeMappings.class, typeMappings);
	}

	/**
	 * Set the source encoding
	 *
	 * @param sourceEncoding
	 */
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	/**
	 * Set whether schema names should be appended to the package name.
	 *
	 * <p>
	 * <b>!!! Important !!!</b><i>
	 * {@link NamingStrategy#getPackage(String, SchemaAndTable)} will be invoked
	 * only if <code>schemaToPackage</code> is set to <code>true</code>.</i>
	 * </p>
	 *
	 * @deprecated This flag will not be necessary in the future because the
	 *             generated package name can be controlled in method
	 *             {@link NamingStrategy#getPackage(String, SchemaAndTable)}.
	 *
	 * @param schemaToPackage
	 */
	@Deprecated
	public void setSchemaToPackage(boolean schemaToPackage) {
		this.schemaToPackage = schemaToPackage;
		module.bind(SQLCodegenModule.SCHEMA_TO_PACKAGE, schemaToPackage);
	}

	/**
	 * Set whether names should be normalized to lowercase
	 *
	 * @param lowerCase
	 */
	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	/**
	 * Set the java imports
	 *
	 * @param imports java imports array
	 */
	public void setImports(String[] imports) {
		module.bind(CodegenModule.IMPORTS, new HashSet<String>(Arrays.asList(imports)));
	}

	/**
	 * Set whether spatial type support should be used
	 *
	 * @param spatial
	 */
	public void setSpatial(boolean spatial) {
		this.spatial = spatial;
	}

	/**
	 * Set the table types to export as a comma separated string
	 *
	 * @param tableTypesToExport
	 */
	public void setTableTypesToExport(String tableTypesToExport) {
		this.tableTypesToExport = tableTypesToExport;
	}

	public boolean isColumnPropertyAnnotations() {
		return columnPropertyAnnotations;
	}

	public void setColumnPropertyAnnotations(boolean columnPropertyAnnotations) {
		this.columnPropertyAnnotations = columnPropertyAnnotations;
	}

	public void setMethods(List<MethodMeta> methods) {
		this.methods = methods;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

}
