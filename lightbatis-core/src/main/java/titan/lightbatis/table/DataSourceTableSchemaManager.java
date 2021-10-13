/**
 *
 */
package titan.lightbatis.table;

import com.google.common.primitives.Primitives;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.types.Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * 获取当前 DataSource 下相关的所有的表结构
 * 
 * @author lifei
 *
 */

@Service("tableSchemaManager")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DataSourceTableSchemaManager implements ITableSchemaManager, InitializingBean, ApplicationContextAware {

	@Value("${snowflake.workerId:1}")
	private long workerId;
	@Value("${snowflake.datacenterId:1}")
	private long datacenterId;
	@Value("${dal.startup.loadtable:true}")
	private boolean startupLoadTable = true;

	private static final int CATALOG_NAME = 1;
	private static final int SCHEMA_NAME = 2;
	private static final int TABLE_NAME = 3;
	private static final int PK_COLUMN_NAME = 4;
	// private static final int PK_NAME = 6;
	private static final int COLUMN_NAME = 4;
	private static final int COLUMN_TYPE = 5;
	private static final int COLUMN_SIZE = 7;
	private static final int DECIMAL_DIGITS = 9;

	private SnowflakeIdWorker idWorker = null;
	
	private DefaultNamingStrategy namingStrategy = new DefaultNamingStrategy();
	private final JDBCTypeMapping jdbcTypeMapping = new JDBCTypeMapping();
	private final JavaTypeMapping javaTypeMapping = new JavaTypeMapping();

	private static final Map<Class<?>, String> class2type = new HashMap<Class<?>, String>();

	static {
		for (Class<?> cl : new Class[] { Boolean.class, Byte.class, Double.class, Float.class, Integer.class,
				Long.class, Short.class, String.class }) {
			class2type.put(cl, cl.getSimpleName().toLowerCase(Locale.ENGLISH));
		}

		class2type.put(Boolean.class, "bit");
		class2type.put(Byte.class, "tinyint");
		class2type.put(Long.class, "bigint");
		class2type.put(Short.class, "smallint");
		class2type.put(String.class, "varchar");
		class2type.put(java.sql.Date.class, "date");
		class2type.put(java.sql.Time.class, "time");
		class2type.put(java.sql.Timestamp.class, "timestamp");
		class2type.put(BigDecimal.class, "decimal");
	}

	public static final String getTypeForClass(Class<?> cl) {
		Class<?> clazz = Primitives.wrap(cl);
		if (class2type.containsKey(clazz)) {
			return class2type.get(clazz);
		} else {
			throw new IllegalArgumentException("Got not type for " + clazz.getName());
		}
	}

	// @Autowired
	// private DataSource dataSource = null;

	private ApplicationContext applicationContext = null;

	private HashSet<String> tableSet = new HashSet<String>();

	private List<TableSchema> tables = new ArrayList<>();

	private boolean loadedAllTable = false;
	static DataSourceTableSchemaManager manager = null;

	// private ITableSchemaSQLBuilder tableSchemaSQLBuilder = new
	// MySqlTableSchemaSQLBuilder();

	/*
	 * （非 Javadoc）
	 * 
	 * @see titan.lightbatis.table.ITableSchemaManager#getTable(java.lang.String)
	 */
	@Override
	public TableSchema getTable(String tableName) {
		if (loadedAllTable) {
			Optional<TableSchema> found = tables.stream()
					.filter(schema -> schema.getTableName().equalsIgnoreCase(tableName)).findFirst();
			if (found.isPresent()) {
				return found.get();
			}
		} else {
			Optional<TableSchema> found = tables.stream()
					.filter(schema -> schema.getTableName().equalsIgnoreCase(tableName)).findFirst();
			if (found.isPresent()) {
				return found.get();
			}else {
				try {
					TableSchema schema = loadTableSchema(tableName);
					return schema;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 */
	public DataSourceTableSchemaManager() {
		super();
		idWorker = new SnowflakeIdWorker(workerId, datacenterId);
		log.debug("Table Schema Manager 构建中 ..... ");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see titan.lightbatis.table.ITableSchemaManager#listTables()
	 */
	@Override
	public List<TableSchema> listTables() {
		if (!loadedAllTable) {
			try{
				loadAllTables();
			}catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
			loadedAllTable = true;
		}
		List<TableSchema> list = Collections.unmodifiableList(tables);
		return list;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		String[] names = applicationContext.getBeanNamesForType(DataSource.class);
//		for (String name : names) {
//			DataSource dataSource = (DataSource) applicationContext.getBean(name);
//			try {
//				long start = System.currentTimeMillis();
//				//loadTables(dataSource, name);
//				long end = System.currentTimeMillis();
//				long els = (end - start) / 1000;
//				System.err.println("加载表结构启动用时: " + (els));
//			} catch (Exception ex) {
//				ex.printStackTrace(System.err);
//				throw ex;
//			}
//
//		}
		if (startupLoadTable) {
			listTables();
		}

		DataSourceTableSchemaManager.manager = this;
	}

	public Class<?> getJavaType(int sqlType, String tableName, String columnName) {
		Type<?> type = javaTypeMapping.getType(tableName, columnName);
		if (type != null) {
			return type.getReturnedClass();
		}
		return null;
		// else {
		// return jdbcTypeMapping.get(sqlType);
		// }
	}

	private ITableSchemaSQLBuilder getSchemaSQLBuilder(DataSource ds) throws Exception {
		ITableSchemaSQLBuilder tableSchemaSQLBuilder = new MySqlTableSchemaSQLBuilder();
//		Connection conn = ds.getConnection();
//
//		DatabaseMetaData dmd = conn.getMetaData();
//		String dbtype = dmd.getDatabaseProductName();
//		System.out.println("dbtype = " + dbtype);

		return tableSchemaSQLBuilder;
	}
	private void loadAllTables() throws Exception{
		String[] names = applicationContext.getBeanNamesForType(DataSource.class);
		for (String name : names) {
			DataSource dataSource = (DataSource) applicationContext.getBean(name);
			try {
				long start = System.currentTimeMillis();
				loadTables(dataSource, name);
				long end = System.currentTimeMillis();
				long els = (end - start) / 1000;
				System.err.println("加载表结构启动用时: " + (els));
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
				throw ex;
			}
		}
	}
	private TableSchema loadTableSchema(String table) throws Exception{
		String[] names = applicationContext.getBeanNamesForType(DataSource.class);
		ResultSet rs = null;
		Statement stmt = null;
		Connection conn = null;
		for (String name : names) {
			DataSource dataSource = (DataSource) applicationContext.getBean(name);
			try {
				conn = dataSource.getConnection();
				DatabaseMetaData metaData = conn.getMetaData();
				String catalog = conn.getCatalog();
				rs = metaData.getTables(catalog, null, table, new String[] { "TABLE"});
				while (rs.next()) {
					String tableType = rs.getString("TABLE_TYPE");
					if (!tableType.equals("TABLE")) {
						continue;
					}
					TableSchema schema = loadTableSchema(rs, conn, name, metaData);
					if (schema != null) {
						tables.add(schema);
						return schema;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
				throw ex;
			} finally {
				if (stmt != null){
					stmt.close();
				}
				if (rs != null){
					rs.close();
				}
				if (conn != null){
					conn.close();
				}

			}
		}
		return null;
	}
	private void loadTables(DataSource dataSource, String dsname) throws Exception {

		Connection conn = dataSource.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			String dbschema = null;

			DatabaseMetaData metaData = conn.getMetaData();
			String catalog = conn.getCatalog();
			//String dbtype = metaData.getDatabaseProductName();
			//if (StringUtils.equalsIgnoreCase(dbtype, "PostgreSQL")) {
				// 获取所有的表名
				rs = metaData.getTables(catalog, dbschema, null, new String[] { "TABLE"});
				while (rs.next()) {
					String tableType = rs.getString("TABLE_TYPE");
					if (!tableType.equals("TABLE")) {
						continue;
					}
					TableSchema schema = loadTableSchema(rs, conn, dsname, metaData);
					tables.add(schema);
				}
//			} else {
//				ITableSchemaSQLBuilder tableSchemaSQLBuilder = getSchemaSQLBuilder(dataSource);
//				String queryTableSql = tableSchemaSQLBuilder.buildTableSQL();
//				// String queryTableColumns = tableSchemaSQLBuilder.buildColumsSQL();
//				stmt = conn.createStatement();
//				rs = stmt.executeQuery(queryTableSql);
//				while (rs.next()) {
//					TableSchema schema = new TableSchema();
//					schema.setDs(dsname);
//					String tableName = rs.getString("name");
//					String schemaName = "";
//
//					String tmpName = dsname + tableName.toLowerCase();
//					if (tableSet.contains(tmpName)) {
//						String msg = dsname + "系统中已经存在一个以上表名为 " + tmpName + "的数据表，但又没有为这个表指定数据的读写路由规则，请确认.";
//						System.err.println(msg);
//						throw new RuntimeException(msg);
//					}
//					tableSet.add(tmpName);
//					String common = rs.getString("Comment");
//					schema.setCommon(common);
//					schema.setDbSchema(schemaName);
//					schema.setTableName(tableName);
//					String clzName = namingStrategy.getClassName(tableName);
//					schema.setEntityName(clzName);
//
//
//					///////////////////////////// 加载Column 的信息
//					ResultSet colRs = metaData.getColumns(null, "%", tableName, "%");
//					while (colRs.next()) {
//
//						String field = colRs.getString("COLUMN_NAME");
//						int dataType = colRs.getInt("DATA_TYPE");
//						String type = colRs.getString("TYPE_NAME");
//						int nullable = colRs.getInt("NULLABLE");
//						int length = colRs.getInt("COLUMN_SIZE");
//						// String key = rs.getString("Key");
//						String comment = colRs.getString("REMARKS");
//
//						ColumnSchema col = new ColumnSchema(field);
//						col.setCommon(comment);
//						col.setNullable(nullable);
//						col.setLength(length);
//						col.setPropertyName(namingStrategy.getPropertyName(field, null));
//						// col.setPrimary(key.equalsIgnoreCase("PRI"));
//						col.setType(dataType);
//						col.setTypeName(type);
//						col.setColumnClz(JDBCTypeMapping.defaultTypes.get(dataType));
//						schema.addColumn(col);
//					}
//					colRs.close();
//					// 获取表的主键字段
//					ResultSet keyRes = null;
//					try{
//						 keyRes = metaData.getPrimaryKeys(null, null, tableName);
//						while (keyRes.next()) {
//							String field = keyRes.getString("COLUMN_NAME");
//							schema.addPrimaryKey(field);
//							schema.setPrimaryField(field, true);
//						}
//
//					} catch (Exception ex) {
//
//					} finally {
//						if (keyRes != null)
//							keyRes.close();
//					}
//
//					//loadTableColumns(schema, conn, tableName, tableSchemaSQLBuilder);
//					tables.add(schema);
//				}
//			}

		} finally
		{
			if (stmt != null){
				stmt.close();
			}
			if (rs != null){
				rs.close();
			}
			if (conn != null){
				conn.close();
			}
		}
	}

	private TableSchema loadTableSchema(ResultSet rs, Connection conn, String dsname, DatabaseMetaData metaData) throws SQLException{

		TableSchema schema = new TableSchema();
		schema.setDs(dsname);
		String catalog = conn.getCatalog();
		String tableName = rs.getString("TABLE_NAME");
		//System.out.println("=========== tableName = " + tableName);
		String clzName = namingStrategy.getClassName(tableName);
		String schemaName = conn.getSchema();
		schema.setEntityName(clzName);

		String tmpName = dsname + tableName.toLowerCase();
		if (tableSet.contains(tmpName)) {
			String msg = dsname + "系统中已经存在一个以上表名为 " + tmpName + "的数据表，但又没有为这个表指定数据的读写路由规则，请确认.";
			System.err.println(msg);
			//throw new RuntimeException(msg);
		}
		tableSet.add(tmpName);
		String common = rs.getString("REMARKS");
		schema.setCommon(common);
		schema.setDbSchema(schemaName);
		schema.setTableName(tableName);


		///////////////////////////// 加载Column 的信息
		ResultSet colRs = metaData.getColumns(catalog, schemaName, tableName, "%");
		while (colRs.next()) {

			String field = colRs.getString("COLUMN_NAME");
			int dataType = colRs.getInt("DATA_TYPE");
			String type = colRs.getString("TYPE_NAME");
			int nullable = colRs.getInt("NULLABLE");
			int length = colRs.getInt("COLUMN_SIZE");
			// String key = rs.getString("Key");
			String comment = colRs.getString("REMARKS");

			ColumnSchema col = new ColumnSchema(field);
			col.setCommon(comment);
			col.setNullable(nullable);
			col.setLength(length);
			col.setPropertyName(namingStrategy.getPropertyName(field, null));
			// col.setPrimary(key.equalsIgnoreCase("PRI"));
			col.setType(dataType);
			col.setTypeName(type);
			col.setColumnClz(JDBCTypeMapping.defaultTypes.get(dataType));
			schema.addColumn(col);
		}
		colRs.close();
		// 获取表的主键字段
		ResultSet keyRes = metaData.getPrimaryKeys(catalog, null, tableName);
		while (keyRes.next()) {
			String field = keyRes.getString("COLUMN_NAME");
			schema.addPrimaryKey(field);
			schema.setPrimaryField(field, true);
		}
		keyRes.close();
		return schema;
	}

	private void loadTableColumns(TableSchema schema, Connection conn, String tableName,
			ITableSchemaSQLBuilder tableSchemaSQLBuilder) throws SQLException {
		String sql = tableSchemaSQLBuilder.buildColumsSQL(tableName);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String field = rs.getString("Field");
				String type = rs.getString("Type");
				String nullStr = rs.getString("Null");
				String key = rs.getString("Key");
				String comment = rs.getString("comment");

				int length = rs.getInt("COLUMN_SIZE");

				ColumnSchema col = new ColumnSchema(field);
				col.setCommon(comment);
				col.setLength(length);
				col.setNullable(nullStr.equalsIgnoreCase("YES") ? 1 : 0);
				col.setPrimary(key.equalsIgnoreCase("PRI"));
				col.setTypeName(type);
				col.setPropertyName(namingStrategy.getPropertyName(field, null));

				String columnClz = col.getTypeName().split("\\(")[0].toUpperCase();
				if (columnClz.equals("DATETIME")) {
					columnClz = "TIMESTAMP";
				}
				if (columnClz.equals("TEXT")) {
					columnClz = "VARCHAR";
				}
				if (columnClz.equals("INT")) {
					columnClz = "INTEGER";
				}
				Field fiel = Types.class.getDeclaredField(columnClz);
				int id = fiel.getInt(Types.class);
				col.setColumnClz(JDBCTypeMapping.defaultTypes.get(id));
				schema.addColumn(col);
			}
		} catch (Exception e) {
			System.err.println("=============== tableName = " + tableName);
			e.printStackTrace(System.err);
		} finally {
			stmt.close();
			rs.close();
		}
	}

	/**
	 * Set the ApplicationContext that this object runs in. Normally this call will
	 * be used to initialize the object.
	 * <p>
	 * Invoked after population of normal bean properties but before an init
	 * callback such as {@link InitializingBean#afterPropertiesSet()} or a custom
	 * init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
	 * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
	 * {@link MessageSourceAware}, if applicable.
	 *
	 * @param applicationContext the ApplicationContext object to be used by this
	 *                           object
	 * @throws ApplicationContextException in case of context initialization errors
	 * @throws BeansException              if thrown by application context methods
	 * @see BeanInitializationException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Long nextId() {
		return idWorker.nextId();
	}

	@Override
	public Long[] nextIds(int size) {
		Long[] ids = new Long[size];
		for (int i=0; i < size; i++) {
			ids[i] = nextId();
		}
		return ids;
	}

}
