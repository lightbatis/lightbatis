/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.ObjectTypeHandler;
import titan.lightbatis.annotations.AutoGenerated;
import titan.lightbatis.annotations.LogicDelete;
import titan.lightbatis.annotations.Revision;
import titan.lightbatis.configuration.MapperConfig;
import titan.lightbatis.exception.LightbatisException;
import titan.lightbatis.generator.GeneratedValueType;
import titan.lightbatis.mybatis.handler.JacksonTypeHandler;
import titan.lightbatis.table.ColumnSchema;
import titan.lightbatis.table.ITableSchemaManager;
import titan.lightbatis.table.TableSchema;
import titan.lightbatis.utils.FieldUtils;
import static  titan.lightbatis.utils.NameUtils.*;

import javax.persistence.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实体类相关的辅助类
 * 
 * @author lifei
 *
 */
@Slf4j
public class EntityMetaManager {

	//private static final Logger log = LoggerFactory.getLogger(EntityMetaManager.class);
	/**
	 * 实体类 => 表对象
	 */
	private static final Map<Class<?>, EntityMeta> entityTableMap = new HashMap<Class<?>, EntityMeta>();
	private static final Map<String, EntityMeta> entityMetas = new HashMap<>();

	/**
	 * 实体类 -> Q 类
	 */
	private static final Map<Class<?>, QEntity> entityQuery = new HashMap<>();
	/**
	 * 表名到实体类型的映射。
	 */
	private static final Map<String, EntityMeta>  tableMetas = new HashMap<>();

	/**
	 * 获取表对象
	 * @TODO 需要修改，存在一个类中如果有多个返回的实体类是一样的，以 实体类为Key 会出现，后面的类覆盖前面的类。
	 * @param entityClass
	 * @return
	 */
	public static EntityMeta getEntityMeta(Class<?> entityClass) {
		EntityMeta entityTable = entityTableMap.get(entityClass);
		if (entityTable == null) {
			throw new LightbatisException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
		}
		return entityTable;
	}
//	public static EntityMeta findEntityMeta(Class<?> entityClass) {
//		EntityMeta entityTable = entityTableMap.get(entityClass);
//		return entityTable;
//	}

	/**
	 * 根据 TableName 来获取对应表 EntityMeta 的信息。
	 * @param tableName
	 * @return
	 */
	public static EntityMeta getEntityMetaByTable (String tableName){
		if (tableMetas.containsKey(tableName)) {
			EntityMeta entityMeta = tableMetas.get(tableName);
			return entityMeta;
		}
		return null;
	}

	public static EntityMeta getEntityMeta(String mapperStatementId) {
//		Optional<EntityMeta> opt = entityTableMap.values().stream()
//				.filter(meta -> meta.getMappedStatementId().equals(mapperStatementId)).findFirst();
//		if (opt.isPresent()) {
//			return opt.get();
//		}
		if (entityMetas.containsKey(mapperStatementId)) {
			return entityMetas.get(mapperStatementId);
		}
		return null;
	}
	public static QEntity getQueryEntity(Class<?> entityClass) {
		if (entityQuery.containsKey(entityClass)) {
			return entityQuery.get(entityClass);
		} else {
			if (entityTableMap.containsKey(entityClass)) {
				EntityMeta entityTable = entityTableMap.get(entityClass);
				QEntity queryEntity = new QEntity(entityTable);
				entityQuery.put(entityClass, queryEntity);
				return queryEntity;
			}
		}
		return null;
	}
	public static Set<ColumnMeta> getColumns(Class<?> entityClass, String[] names) {
		Set<ColumnMeta> columns = getColumns(entityClass);
		Set<ColumnMeta> selectedColumns = new HashSet<>();
		for (String name : names) {
			Optional<ColumnMeta> option = columns.stream()
					.filter(column -> column.getColumn().equals(name) || column.getProperty().equals(name)).findFirst();
			ColumnMeta entityColumn = null;
			if (option.isPresent()) {
				entityColumn = option.get();
				selectedColumns.add(entityColumn);
			}

		}
		return selectedColumns;
	}
	public static Set<ColumnMeta> getOrderbyColumns(Class<?> entityClass) {
		EntityMeta table = getEntityMeta(entityClass);
		Set<ColumnMeta> orderByColumns = new HashSet<>();
		for (ColumnMeta column : table.getClassColumns()) {
			if (column.getOrderBy() != null) {
				orderByColumns.add(column);
			}
		}
		return orderByColumns;
	}
	/**
	 * 获取默认的orderby语句
	 *
	 * @param entityClass
	 * @return
	 */
	public static String getOrderByClause(Class<?> entityClass) {
		EntityMeta table = getEntityMeta(entityClass);
		if (table.getOrderByClause() != null) {
			return table.getOrderByClause();
		}
		StringBuilder orderBy = new StringBuilder();
		for (ColumnMeta column : table.getClassColumns()) {
			if (column.getOrderBy() != null) {
				if (orderBy.length() != 0) {
					orderBy.append(",");
				}
				orderBy.append(column.getColumn()).append(" ").append(column.getOrderBy());
			}
		}
		table.setOrderByClause(orderBy.toString());
		return table.getOrderByClause();
	}


	/**
	 * 获取全部列
	 *
	 * @param entityClass
	 * @return
	 */
	public static Set<ColumnMeta> getColumns(Class<?> entityClass) {
		return getEntityMeta(entityClass).getClassColumns();
	}


	/**
	 * 获取主键信息
	 *
	 * @param entityClass
	 * @return
	 */
	public static Set<ColumnMeta> getPKColumns(Class<?> entityClass) {
		return getEntityMeta(entityClass).getEntityClassPKColumns();
	}


	/**
	 * 初始化实体属性
	 *
	 * @param entityClass
	 * @param config
	 * @throws Exception 
	 */
	public static EntityMeta initEntityNameMap(Class<?> entityClass, MapperConfig config, String msId) throws Exception {
		if (entityMetas.containsKey(msId) ) {
			return entityMetas.get(msId);
		}

		// 创建并缓存EntityTable
		EntityMeta entityTable = processEntity(entityClass,config);
		if (entityTable == null) {
			return null;
		}
		entityTable.setMappedStatementId(msId);
		entityMetas.put(msId, entityTable);
		entityTableMap.put(entityClass, entityTable);
		if (tableMetas.containsKey(entityTable.getName())) {
			tableMetas.put(entityTable.getName(), entityTable);
		} else {
			//log.warn( entityTable.getName() + " 表已经存在！");
		}
		return entityTable;
	}


	private static EntityMeta processEntity(Class<?> entityClass, MapperConfig config) throws Exception{
		// 创建并缓存EntityTable
		EntityMeta entityMeta = null;

		if (entityClass.isAnnotationPresent(Table.class)) {
			Table table = entityClass.getAnnotation(Table.class);
			if (!table.name().equals("")) {
				entityMeta = new EntityMeta(entityClass);
				entityMeta.setTable(table);
			}
		}
		
		if (entityMeta == null) {
			entityMeta = new EntityMeta(entityClass);
			// 可以通过stye控制
			//entityTable.setName(StringUtil.convertByStyle(entityClass.getSimpleName(), style));
			entityMeta.setName(entityClass.getSimpleName());
		}
		if (entityClass.isAnnotationPresent(SecondaryTable.class)) {
			SecondaryTable st = entityClass.getAnnotation(SecondaryTable.class);
			entityMeta.addSecondaryTable(st);
		}
		if (entityClass.isAnnotationPresent(SecondaryTables.class)) {
			SecondaryTables tables = entityClass.getAnnotation(SecondaryTables.class);
			SecondaryTable tbls[] = tables.value();
			for (SecondaryTable secondaryTable : tbls) {
				entityMeta.addSecondaryTable(secondaryTable);
			}
		}
		TableSchema tableSchema = ITableSchemaManager.getInstance().getTable(entityMeta.getName());
		if (tableSchema == null) {
			System.err.println("table is null " + entityMeta.getName());
			log.error("table is null " + entityMeta.getName());
			//throw new LightbatisException(entityMeta.getName() + " 没有找到！");
			return null;
		}
		// 处理所有列
		List<FieldMeta> fields = null;
		if (config.isEnableMethodAnnotation()) {
			fields = FieldUtils.getAll(entityClass);
		} else {
			fields = FieldUtils.getFields(entityClass);
		}
		Map<String, Field> fieldMap = new HashMap<>();
		Field[] myfields = entityClass.getDeclaredFields();
		for (Field f : myfields) {
			String name = f.getName();
			fieldMap.put(name, f);
		}
		for (FieldMeta field : fields) {
			// 如果启用了简单类型，就做简单类型校验，如果不是简单类型，直接跳过
			//if (config.isUseSimpleType() && !SimpleTypeRegistry.isSimpleType(field.getJavaType())) {
			//	continue;
			//}
			try {
				ColumnMeta colMeta = processField(entityMeta, tableSchema , field);
				if (colMeta == null) {
					log.warn(field.getName() + " 为 Null, 已经跳过！");
					continue;
				}
				if (List.class.isAssignableFrom(colMeta.getJavaType())) {
					Field colField = fieldMap.get(colMeta.getProperty());
					Type fieldType = colField.getGenericType();
					Class<?> fieldClz = getReturnType(fieldType);
					//分析元素的类型
					EntityMeta innerEntity = processEntity(fieldClz, config);
					colMeta.setCollectionBaseType(innerEntity);
					innerEntity.setMappedStatementId("");
					entityTableMap.put(fieldClz, innerEntity);
				}
			}catch (Exception ex) {
				log.error(field.getName() + " 发生了异常", ex);
				throw ex;
			}
		}
		// 当pk.size=0的时候使用所有列作为主键
		if (entityMeta.getEntityClassPKColumns().size() == 0) {
			entityMeta.setEntityClassPKColumns(entityMeta.getClassColumns());
		}
		entityMeta.initPropertyMap();
		QEntity entity = new QEntity(entityMeta);
		entityQuery.put(entityClass, entity);
		return entityMeta;
	}

	/**
	 * 处理一列
	 *
	 * @param entityMeta
	 * @param field
	 */
	private static ColumnMeta processField(EntityMeta entityMeta, TableSchema tableSchema, FieldMeta field) throws Exception{
		// 排除字段
		if (field.isAnnotationPresent(Transient.class)) {
			return null;
		}
		// Id 是否是ID 字段
		ColumnMeta entityColumn = new ColumnMeta();
		if (field.isAnnotationPresent(Id.class)) {
			entityColumn.setId(true);
			entityColumn.setIdentity(true);
		}
		// Column
		String columnName = null;
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			columnName = column.name();
			entityColumn.setUpdatable(column.updatable());
			entityColumn.setInsertable(column.insertable());
			String tableName = column.table();
			if (tableName != null) {
				entityColumn.setTableName(tableName);
			} else {
				entityColumn.setTableName(entityMeta.getName());
			}
			
		}
		
		//如果没有 Column 的注释，通过驼峰命名去推测。
		if (StringUtils.isEmpty(columnName)) {
			columnName =camelhumpToUnderline(field.getName() ); 
		}
		if (StringUtils.isEmpty(columnName)) {
			log.warn(field.getName() + " 没有找到对应的列名，请按 @Column 注释添加！");
		}
		if (StringUtils.isEmpty(entityColumn.getTableName())) {
			entityColumn.setTableName(entityMeta.getName());
		}
		if (tableSchema != null) {
			ColumnSchema colSchema = tableSchema.getColumn(columnName);
			if (colSchema != null) {
				entityColumn.setJdbcType(JdbcType.forCode(colSchema.getType()));
			} else {
				log.error(entityMeta.getEntityClass() + " 没有找到与实体表 " + tableSchema.getTableName() + " 与之对应的列  " + columnName);
				//throw new NullPointerException(entityMeta.getEntityClass() + " 没有找到与实体表 " + tableSchema.getTableName() + " 与之对应的列  " + columnName);
				return null;
			}
		}


		entityColumn.setProperty(field.getName());
		entityColumn.setColumn(columnName);
		entityColumn.setJavaType(field.getJavaType());
		// OrderBy
		if (field.isAnnotationPresent(OrderBy.class)) {
			OrderBy orderBy = field.getAnnotation(OrderBy.class);
			if (orderBy.value().equals("")) {
				entityColumn.setOrderBy("ASC");
			} else {
				entityColumn.setOrderBy(orderBy.value());
			}
		}
		if (JSONObject.class.isAssignableFrom(entityColumn.getJavaType()) && entityColumn.getTypeHandler() == null ) {
			entityColumn.setTypeHandler(JacksonTypeHandler.class);
			//System.err.println("+++++++++================="+ JacksonTypeHandler.class.getCanonicalName());
		}
		// 如果类型是 List, 这里需要查找到元素的基类的类型
		if (List.class.isAssignableFrom(entityColumn.getJavaType()) && entityColumn.getTypeHandler() == null) {
			entityColumn.setTypeHandler(ObjectTypeHandler.class);
		}
		// 主键策略 - Oracle序列，MySql自动增长，UUID
		if (field.isAnnotationPresent(SequenceGenerator.class)) {
			SequenceGenerator sequenceGenerator = field.getAnnotation(SequenceGenerator.class);
			if (sequenceGenerator.sequenceName().equals("")) {
				throw new LightbatisException(entityMeta.getEntityClass() + "字段" + field.getName()
						+ "的注解@SequenceGenerator未指定sequenceName!");
			}
			entityColumn.setSequenceName(sequenceGenerator.sequenceName());
		} else if (field.isAnnotationPresent(GeneratedValue.class)) {
			GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
			if (generatedValue.generator().equals("UUID")) {
				entityColumn.setUuid(true);
			} else if (generatedValue.generator().equals("JDBC")) {
				entityColumn.setIdentity(true);
				entityColumn.setGenerator("JDBC");
				entityMeta.setKeyProperties(entityColumn.getProperty());
				entityMeta.setKeyColumns(entityColumn.getColumn());
			} else if (generatedValue.generator().equals(GeneratedValueType.SNOWFLAKE)) {
				entityColumn.setIdentity(true);
				entityColumn.setGenerator(GeneratedValueType.SNOWFLAKE);
				entityMeta.setKeyProperties(entityColumn.getProperty());
				entityMeta.setKeyColumns(entityColumn.getColumn());
			}
			else {
				// 允许通过generator来设置获取id的sql,例如mysql=CALL IDENTITY(),hsqldb=SELECT
				// SCOPE_IDENTITY()
				// 允许通过拦截器参数设置公共的generator
				if (generatedValue.strategy() == GenerationType.IDENTITY) {
					// mysql的自动增长
					entityColumn.setIdentity(true);
//					if (!generatedValue.generator().equals("")) {
//						String generator = null;
//						IdentityDialect identityDialect = IdentityDialect
//								.getDatabaseDialect(generatedValue.generator());
//						if (identityDialect != null) {
//							generator = identityDialect.getIdentityRetrievalStatement();
//						} else {
//							generator = generatedValue.generator();
//						}
//						entityColumn.setGenerator(generator);
//					}

				} else {
					throw new LightbatisException(field.getName() + " - 该字段@GeneratedValue配置只允许以下几种形式:"
							+ "\n1.全部数据库通用的@GeneratedValue(generator=\"UUID\")"
							+ "\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"" + GeneratedValueType.SNOWFLAKE + "\\\")  "
							+ "\n3.雪花算法 useGeneratedKeys的@GeneratedValue(generator=\\\"Snowflake\\\")  "
							+ "\n4.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,generator=\"Mysql\"])"
							);
				}
			}
		} else if (entityColumn.isIdentity()) {
			//如果是ID,又没有指定生成方式，默认是 SNOWFLAKE 算法
			entityColumn.setGenerator(GeneratedValueType.SNOWFLAKE);
		} 
		
		if (field.isAnnotationPresent(AutoGenerated.class)) {
			//自动生成值
			AutoGenerated generated = field.getAnnotation(AutoGenerated.class);
			entityColumn.setAutoGenerated(true);
			entityColumn.setGeneratedEvent(generated.event().name());
			entityColumn.setGenerator(generated.value());
			entityColumn.setInsertable(true);
			entityMeta.setExistGeneratedColumn(true);
		}
		if (field.isAnnotationPresent(LogicDelete.class)) {
			entityColumn.setLogicDelete(true);
		}
		if (field.isAnnotationPresent(Revision.class)) {
			entityColumn.setRevision(true);
		}
		entityMeta.addColumn(entityColumn);
		if (entityColumn.isId()) {
			entityMeta.getEntityClassPKColumns().add(entityColumn);
		}
		return entityColumn;
	}

	private static Class<?> getReturnType(Type type) {
		Class<?> returnType = null;
		Type resolvedReturnType = type;
		if (resolvedReturnType instanceof Class) {
			returnType = (Class<?>) resolvedReturnType;
			if (returnType.isArray()) {
				returnType = returnType.getComponentType();
			}

		} else if (resolvedReturnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) resolvedReturnType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			if (Collection.class.isAssignableFrom(rawType) || Cursor.class.isAssignableFrom(rawType)) {
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length == 1) {
					Type returnTypeParameter = actualTypeArguments[0];
					if (returnTypeParameter instanceof Class<?>) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						// (gcode issue #443) actual type can be a also a parameterized type
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
					} else if (returnTypeParameter instanceof GenericArrayType) {
						Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
								.getGenericComponentType();
						// (gcode issue #525) support List<byte[]>
						returnType = Array.newInstance(componentType, 0).getClass();
					}
				}
			}
		}

		return returnType;
	}



}
