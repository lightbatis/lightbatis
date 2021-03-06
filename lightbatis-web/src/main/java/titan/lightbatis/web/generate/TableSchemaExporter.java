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
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.codegen.KeyDataFactory;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.SQLCodegenModule;
import com.querydsl.sql.codegen.SpatialSupport;
import com.querydsl.sql.codegen.support.NotNullImpl;
import com.querydsl.sql.codegen.support.SizeImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import titan.lightbatis.annotations.*;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.web.entity.TableEntitySchema;

import javax.annotation.Nullable;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 将 TableSchema 转换成 JavaBean
 * 
 * @author lifei
 *
 */
public class TableSchemaExporter {

	private static final Logger logger = LoggerFactory.getLogger(TableSchemaExporter.class);

	//private final SQLTemplatesRegistry sqlTemplatesRegistry = new SQLTemplatesRegistry();

	private final SQLCodegenModule module = new SQLCodegenModule();

	private final Set<String> classes = new HashSet<String>();

	private File targetFolder;

	private File beansTargetFolder;

	@Nullable
	private String beanPackageName;

	@Nullable
	private String schemaPattern, tableNamePattern;

	@Nullable
	private Serializer beanSerializer;

	private boolean createScalaSources = false;

	private final Map<EntityType, Type> entityToWrapped = new HashMap<EntityType, Type>();

	private Serializer serializer;

	private TypeMappings typeMappings;

	private QueryTypeFactory queryTypeFactory;

	private NamingStrategy namingStrategy;

	private Configuration configuration;

	private KeyDataFactory keyDataFactory;

	private boolean columnAnnotations = false;

	private boolean validationAnnotations = false;

	private boolean schemaToPackage = false;

	// 列是否使用 @ApiModelProperty 属性
	private boolean columnPropertyAnnotations = true;

	private String sourceEncoding = "UTF-8";

	private boolean lowerCase = false;



	private boolean exportPrimaryKeys = true;

	private boolean exportForeignKeys = true;



	private boolean spatial = false;

	@Nullable
	private String tableTypesToExport;

	public TableSchemaExporter() {
	}

	protected EntityType createEntityType(SchemaAndTable schemaAndTable, final String className) {
		EntityType classModel;

		if (beanSerializer == null) {
			String packageName = normalizePackage(module.getPackageName(), schemaAndTable);
			String simpleName = module.getPrefix() + className + module.getSuffix();
			Type classTypeModel = new SimpleType(TypeCategory.ENTITY, packageName + "." + simpleName, packageName,
					simpleName, false, false);
			classModel = new EntityType(classTypeModel,
					module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));
			typeMappings.register(classModel, classModel);

		} else {
			String beanPackage = normalizePackage(beanPackageName, schemaAndTable);
			String simpleName = module.getBeanPrefix() + className + module.getBeanSuffix();
			Type classTypeModel = new SimpleType(TypeCategory.ENTITY, beanPackage + "." + simpleName, beanPackage,
					simpleName, false, false);
			classModel = new EntityType(classTypeModel,
					module.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

			Type mappedType = queryTypeFactory.create(classModel);
			entityToWrapped.put(classModel, mappedType);
			typeMappings.register(classModel, mappedType);
		}

		classModel.getData().put("schema", schemaAndTable.getSchema());
		classModel.getData().put("table", schemaAndTable.getTable());
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

	/**
	 * Export the tables based on the given database metadata
	 *
	 * @param tableSchema database metadata
	 * @throws SQLException
	 */
	public void export(TableEntitySchema tableSchema) throws SQLException {
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
		if (serializer == null)
			serializer = module.get(Serializer.class);
		beanSerializer = module.get(Serializer.class, SQLCodegenModule.BEAN_SERIALIZER);
		namingStrategy = module.get(NamingStrategy.class);
		configuration = module.get(Configuration.class);

		if (beanSerializer == null) {
			keyDataFactory = new KeyDataFactory(namingStrategy, module.getPackageName(), module.getPrefix(),
					module.getSuffix(), schemaToPackage);
		} else {
			keyDataFactory = new KeyDataFactory(namingStrategy, beanPackageName, module.getBeanPrefix(),
					module.getBeanSuffix(), schemaToPackage);
		}

		handleTable(tableSchema);

	}


	Set<String> getClasses() {
		return classes;
	}

	private void handleTable(TableEntitySchema schema) {
		String tableName = schema.getTableName();

		String normalizedSchemaName = namingStrategy.normalizeSchemaName(schema.getDbSchema());
		String normalizedTableName = namingStrategy.normalizeTableName(tableName);

		SchemaAndTable schemaAndTable = new SchemaAndTable(normalizedSchemaName, normalizedTableName);

		if (!namingStrategy.shouldGenerateClass(schemaAndTable)) {
			return;
		}

		String className = namingStrategy.getClassName(schemaAndTable);
		EntityType classModel = createEntityType(schemaAndTable, className);
		classModel.addAnnotation(new TableAnnotation(schema));
		classModel.addAnnotation(new EntityAnnotation(schema.getTableName()));
		classModel.addAnnotation(new DalEntity() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return DalEntity.class;
			}
			
			@Override
			public String table() {
				return schema.getTableName();
			}
			
			@Override
			public String name() {
				return schema.getEntityName();
			}
		});
		classModel.addAnnotation(new ApiModel() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return ApiModel.class;
			}
			
			@Override
			public String value() {
				return schema.getCommon();
			}
			
			@Override
			public Class<?>[] subTypes() {
				return null;
			}
			
			@Override
			public String reference() {
				return null;
			}
			
			@Override
			public Class<?> parent() {
				return null;
			}
			
			@Override
			public String discriminator() {
				return null;
			}
			
			@Override
			public String description() {
				return schema.getCommon();
			}
		});
		if (exportPrimaryKeys) {
			// collect primary keys

//            Map<String,PrimaryKeyData> primaryKeyData = keyDataFactory
//                    .getPrimaryKeys(md, catalog, schema, tableName);
//            if (!primaryKeyData.isEmpty()) {
//                classModel.getData().put(PrimaryKeyData.class, primaryKeyData.values());
//            }
		}

		if (exportForeignKeys) {
//            if (exportDirectForeignKeys) {
//                // collect foreign keys
//                Map<String,ForeignKeyData> foreignKeyData = keyDataFactory
//                        .getImportedKeys(md, catalog, schema, tableName);
//                if (!foreignKeyData.isEmpty()) {
//                    Collection<ForeignKeyData> foreignKeysToGenerate = new HashSet<ForeignKeyData>();
//                    for (ForeignKeyData fkd : foreignKeyData.values()) {
//                        if (namingStrategy.shouldGenerateForeignKey(schemaAndTable, fkd)) {
//                            foreignKeysToGenerate.add(fkd);
//                        }
//                    }
//
//                    if (!foreignKeysToGenerate.isEmpty()) {
//                        classModel.getData().put(ForeignKeyData.class, foreignKeysToGenerate);
//                    }
//                }
//            }
//
//            if (exportInverseForeignKeys) {
//                // collect inverse foreign keys
//                Map<String,InverseForeignKeyData> inverseForeignKeyData = keyDataFactory
//                        .getExportedKeys(md, catalog, schema, tableName);
//                if (!inverseForeignKeyData.isEmpty()) {
//                    classModel.getData().put(InverseForeignKeyData.class, inverseForeignKeyData.values());
//                }
//            }
		}

		// collect columns
//        ResultSet columns = md.getColumns(catalog, schema, tableName.replace("/", "//"), null);
//        try {
//            while (columns.next()) {
//                handleColumn(classModel, tableName, columns);
//            }
//        } finally {
//            columns.close();
//        }
		for (ColumnSchema columnSchema : schema.getColumns()) {
			handleColumn(classModel, schema, columnSchema);
		}

		// serialize model
		serialize(classModel, schemaAndTable);

		logger.info("Exported " + tableName + " successfully");
	}

	private void handleColumn(EntityType classModel, TableEntitySchema tableSchema, ColumnSchema columnSchema) {
		String columnName = normalize(columnSchema.getColumnName());
		String normalizedColumnName = namingStrategy.normalizeColumnName(columnName);
		String tableName = tableSchema.getTableName();
		int columnType = columnSchema.getType();
		String typeName = columnSchema.getTypeName();
		Number columnSize = columnSchema.getLength();
		Number columnDigits = columnSchema.getColumnDigits();
		int columnIndex = columnSchema.getColumnIndex();
		int nullable = columnSchema.getNullable();

		String propertyName = namingStrategy.getPropertyName(normalizedColumnName, classModel);
		Class<?> clazz = columnSchema.getColumnClz();// configuration.getJavaType(columnType, typeName, columnSize != null ? columnSize.intValue() : 0,
				//columnDigits != null ? columnDigits.intValue() : 0, tableName, columnName);
		if (clazz == null) {
			clazz = Object.class;
		}
		TypeCategory fieldType = TypeCategory.get(clazz.getName());
		if (Number.class.isAssignableFrom(clazz)) {
			fieldType = TypeCategory.NUMERIC;
		} else if (Enum.class.isAssignableFrom(clazz)) {
			fieldType = TypeCategory.ENUM;
		}
		Type typeModel = new ClassType(fieldType, clazz);
		Property property = createProperty(classModel, normalizedColumnName, propertyName, typeModel);
		ColumnMetadata column = ColumnMetadata.named(normalizedColumnName).ofType(columnType).withIndex(columnIndex);
		if (nullable == DatabaseMetaData.columnNoNulls) {
			column = column.notNull();
		}
		if (columnSize != null) {
			column = column.withSize(columnSize.intValue());
		}
		if (columnDigits != null) {
			column = column.withDigits(columnDigits.intValue());
		}
		property.getData().put("COLUMN", column);

		if (columnAnnotations) {
			//property.addAnnotation(new ColumnImpl(normalizedColumnName));
			property.addAnnotation(new ColumnAnnotation(columnSchema));
		}
		if (columnSchema.isPrimary()) {
			property.addAnnotation(new Id() {

				@Override
				public Class<? extends Annotation> annotationType() {
					return Id.class;
				}});
		}
		if (tableSchema.getLogicDelteColumn().equalsIgnoreCase(columnName)) {
			property.addAnnotation(new LogicDelete(){
				@Override
				public int isDeleted() {
					return 1;
				}

				@Override
				public int noDeleted() {
					return 0;
				}

				@Override
				public Class<? extends Annotation> annotationType() {
					return LogicDelete.class;
				}
			});
		}
		if (tableSchema.getRevisionColumn().equalsIgnoreCase(columnName)) {
			property.addAnnotation(new Revision(){
				@Override
				public Class<? extends Annotation> annotationType() {
					return Revision.class;
				}
			});
		}
		if (tableSchema.getUpdatedTimeColumn().equalsIgnoreCase(columnName)) {
			property.addAnnotation(new AutoGenerated(){

				@Override
				public Class<? extends Annotation> annotationType() {
					return AutoGenerated.class;
				}

				@Override
				public GeneratedEvent event() {
					return GeneratedEvent.Update;
				}

				@Override
				public String value() {
					return "";
				}
			});
		}
		if (tableSchema.getCreatedTimeColumn().equalsIgnoreCase(columnName)) {
			property.addAnnotation(new AutoGenerated(){

				@Override
				public Class<? extends Annotation> annotationType() {
					return AutoGenerated.class;
				}

				@Override
				public GeneratedEvent event() {
					return GeneratedEvent.Insert;
				}

				@Override
				public String value() {
					return "";
				}
			});
		}
		if (columnPropertyAnnotations && (columnSchema.getCommon() != null && columnSchema.getColumnName() != null)) {
			property.addAnnotation(new ApiModelProperty() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return ApiModelProperty.class;
				}
				
				@Override
				public String value() {
					String note = columnSchema.getCommon();
					if (StringUtils.isEmpty(note)) {
						note = columnSchema.getColumnName();
					}
					return note;
				}
				
				@Override
				public boolean required() {
					return false;
				}
				
				@Override
				public String reference() {
					return "";
				}
				
				@Override
				public boolean readOnly() {
					return false;
				}
				
				@Override
				public int position() {
					return 0;
				}
				
				@Override
				public String notes() {
					return "";
				}
				
				@Override
				public String name() {
					return "";
				}
				
				@Override
				public boolean hidden() {
					return false;
				}
				
				@Override
				public String example() {
					return "";
				}
				
				@Override
				public String dataType() {
					return "";
				}
				
				@Override
				public String allowableValues() {
					return "";
				}
				
				@Override
				public String access() {
					return "";
				}

				@Override
				public AccessMode accessMode() {
					return AccessMode.AUTO;
				}

				@Override
				public boolean allowEmptyValue() {
					return true;
				}

				@Override
				public Extension[] extensions() {
					return null;
				}
			});
		}

		if (validationAnnotations) {
			if (nullable == DatabaseMetaData.columnNoNulls) {
				property.addAnnotation(new NotNullImpl());
			}
			int size = columnSize.intValue();
			if (size > 0 && clazz.equals(String.class)) {
				property.addAnnotation(new SizeImpl(0, size));
			}
		}
		classModel.addProperty(property);
	}





	private String normalize(String str) {
		if (lowerCase && str != null) {
			return str.toLowerCase();
		} else {
			return str;
		}
	}

	private void serialize(EntityType type, SchemaAndTable schemaAndTable) {
		try {
			String fileSuffix = createScalaSources ? ".scala" : ".java";

			if (beanSerializer != null) {

				String packageName = normalizePackage(beanPackageName, schemaAndTable);
				String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
				write(beanSerializer, new File(beansTargetFolder, path), type);

				String otherPath = entityToWrapped.get(type).getFullName().replace('.', '/') + fileSuffix;
				write(serializer, new File(targetFolder, otherPath), type);
			} else {
				String packageName = normalizePackage(module.getPackageName(), schemaAndTable);
				String path = packageName.replace('.', '/') + "/" + type.getSimpleName() + fileSuffix;
				write(serializer, new File(targetFolder, path), type);
			}

		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void write(Serializer serializer, File targetFile, EntityType type) throws IOException {
		if (!classes.add(targetFile.getPath())) {
			throw new IllegalStateException("Attempted to write multiple times to " + targetFile.getPath()
					+ ", please check your configuration");
		}
		StringWriter w = new StringWriter();
		CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
		serializer.serialize(type, SimpleSerializerConfig.DEFAULT, writer);

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

	/**
	 * Set the schema pattern filter to be used
	 *
	 * @param schemaPattern a schema name pattern; must match the schema name as it
	 *                      is stored in the database; "" retrieves those without a
	 *                      schema; {@code null} means that the schema name should
	 *                      not be used to narrow the search (default: null)
	 */
	public void setSchemaPattern(@Nullable String schemaPattern) {
		this.schemaPattern = schemaPattern;
	}

	/**
	 * Set the table name pattern filter to be used
	 *
	 * @param tableNamePattern a table name pattern; must match the table name as it
	 *                         is stored in the database (default: null)
	 */
	public void setTableNamePattern(@Nullable String tableNamePattern) {
		this.tableNamePattern = tableNamePattern;
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
	public void setBeanSerializer(@Nullable Serializer beanSerializer) {
		module.bind(SQLCodegenModule.BEAN_SERIALIZER, beanSerializer);
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
	 * Set whether column annotations should be created
	 *
	 * @param columnAnnotations
	 */
	public void setColumnAnnotations(boolean columnAnnotations) {
		this.columnAnnotations = columnAnnotations;
	}

	/**
	 * Set whether validation annotations should be created
	 *
	 * @param validationAnnotations
	 */
	public void setValidationAnnotations(boolean validationAnnotations) {
		this.validationAnnotations = validationAnnotations;
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
	 * Set whether primary keys should be exported
	 *
	 * @param exportPrimaryKeys
	 */
	public void setExportPrimaryKeys(boolean exportPrimaryKeys) {
		this.exportPrimaryKeys = exportPrimaryKeys;
	}

	/**
	 * Set whether foreign keys should be exported
	 *
	 * @param exportForeignKeys
	 */
	public void setExportForeignKeys(boolean exportForeignKeys) {
		this.exportForeignKeys = exportForeignKeys;
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

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

}
