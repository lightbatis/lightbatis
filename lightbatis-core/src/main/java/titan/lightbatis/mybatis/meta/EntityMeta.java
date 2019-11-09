/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import titan.lightbatis.exception.LightbatisException;

/**
 * @author lifei
 *
 */
public class EntityMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2319159476186098513L;
	/**
	 * 表名
	 */
	private String name;
    private String catalog;
    private String schema;
    private String orderByClause;
    private String baseSelect;
    //实体类 => 全部列属性
    private Set<ColumnMeta> entityClassColumns = new LinkedHashSet<>();
    //实体类 => 主键信息
    private Set<ColumnMeta> entityClassPKColumns = new LinkedHashSet<>();
    //辅助表相关的信息
    private HashMap<String,SecondTableMeta> secondTables = new HashMap<>();
    //useGenerator包含多列的时候需要用到
    private List<String> keyProperties;
    private List<String> keyColumns;
    //resultMap对象
    private ResultMap resultMap;
    //属性和列对应
    protected Map<String, ColumnMeta> propertyMap;
    //类
    private Class<?> entityClass;
    //是否是多表处理，即这个查询涉及到多个表
    private boolean multiTable = false;
    //对应于 MyBatis 的 MappedStatement 
    private String mappedStatementId = null;
    //从表的映射关系
    private List<SecondaryTable> secondaryTables = new ArrayList<>();
    
    public EntityMeta(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setTable(Table table) {
        this.name = table.name();
        this.catalog = table.catalog();
        this.schema = table.schema();
    }

    public void setKeyColumns(List<String> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public void setKeyProperties(List<String> keyProperties) {
        this.keyProperties = keyProperties;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getBaseSelect() {
        return baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public String getPrefix() {
        if (StringUtils.isNotEmpty(catalog)) {
            return catalog;
        }
        if (StringUtils.isNotEmpty(schema)) {
            return schema;
        }
        return "";
    }

    /**
     * 添加 
     * @param column
     */
    public void addColumn(ColumnMeta column) {
    		entityClassColumns.add(column);
    		String tableName = column.getTableName();
    		multiTable = !tableName.equals(this.name);
    		//除了自身主表外的其他表
    		if (multiTable) {
    			//检查这个从表的定义是否存在，如果不存在，则直接抛出异常
    			SecondaryTable st = getSecondaryTable(tableName);
    			if (st == null) {
    				throw new NullPointerException("从表 " + tableName + " 定义的与主表的关联关系不存在！需要在主表 " + getName() + " 中使用 @SecondaryTable 定义关联关系 ");
    			}
    			SecondTableMeta secondColumns = secondTables.get(tableName);
        		if (!secondTables.containsKey(tableName)) {
        			secondColumns = new SecondTableMeta();
        			secondColumns.setTableName(tableName);
        		}
        		secondColumns.addColumn(column);
        		secondTables.put(tableName, secondColumns);
    		}
    }
    /**
     * 根据列名查找定的
     * @param field
     * @return
     */
    public ColumnMeta findColumnByField(String field) {
    		return 	entityClassColumns.stream().filter( meta -> meta.getColumn().equals(field)).findFirst().get();
    }
    
    public String getMappedStatementId() {
		return mappedStatementId;
	}

	public void setMappedStatementId(String mappedStatementId) {
		this.mappedStatementId = mappedStatementId;
	}

	public boolean isMultiTable() {
		return multiTable;
	}

	public void setMultiTable(boolean multiTable) {
		this.multiTable = multiTable;
	}

	public Set<ColumnMeta> getClassColumns() {
        return entityClassColumns;
    }
//
//    public void setEntityClassColumns(Set<ColumnMeta> entityClassColumns) {
//        this.entityClassColumns = entityClassColumns;
//    }

    public Set<ColumnMeta> getEntityClassPKColumns() {
        return entityClassPKColumns;
    }

    public void setEntityClassPKColumns(Set<ColumnMeta> entityClassPKColumns) {
        this.entityClassPKColumns = entityClassPKColumns;
    }

    public String[] getKeyProperties() {
        if (keyProperties != null && keyProperties.size() > 0) {
            return keyProperties.toArray(new String[]{});
        }
        return new String[]{};
    }

    public void setKeyProperties(String keyProperty) {
        if (this.keyProperties == null) {
            this.keyProperties = new ArrayList<String>();
            this.keyProperties.add(keyProperty);
        } else {
            this.keyProperties.add(keyProperty);
        }
    }

    public String[] getKeyColumns() {
        if (keyColumns != null && keyColumns.size() > 0) {
            return keyColumns.toArray(new String[]{});
        }
        return new String[]{};
    }

    public void setKeyColumns(String keyColumn) {
        if (this.keyColumns == null) {
            this.keyColumns = new ArrayList<String>();
            this.keyColumns.add(keyColumn);
        } else {
            this.keyColumns.add(keyColumn);
        }
    }

    public List<SecondaryTable> getSecondaryTables() {
		return secondaryTables;
	}
    
    public void addSecondaryTable(SecondaryTable st) {
    		secondaryTables.add(st);
    }
    public SecondaryTable getSecondaryTable(String tableName) {
    		return secondaryTables.stream().filter( t -> t.name().equals(tableName)).findFirst().get();
    }
	public HashMap<String, SecondTableMeta> getSecondTables() {
		return secondTables;
	}

	/**
     * 生成当前实体的resultMap对象
     *
     * @param configuration
     * @return
     */
    public ResultMap getResultMap(Configuration configuration) {
        if (this.resultMap != null) {
            return this.resultMap;
        }
        if (entityClassColumns == null || entityClassColumns.size() == 0) {
            return null;
        }
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (ColumnMeta entityColumn : entityClassColumns) {

            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), entityColumn.getColumn(), entityColumn.getJavaType());
            if (entityColumn.getJdbcType() != null) {
                builder.jdbcType(entityColumn.getJdbcType());
            }
            if (entityColumn.getTypeHandler() != null) {
                try {
                    builder.typeHandler(entityColumn.getTypeHandler().newInstance());
                } catch (Exception e) {
                    throw new LightbatisException(e);
                }
            }
            List<ResultFlag> flags = new ArrayList<ResultFlag>();
            if (entityColumn.isId()) {
                flags.add(ResultFlag.ID);
            }
            builder.flags(flags);
            resultMappings.add(builder.build());
        }
        ResultMap.Builder builder = new ResultMap.Builder(configuration, "BaseMapperResultMap", this.entityClass, resultMappings, true);
        this.resultMap = builder.build();
        return this.resultMap;
    }

    /**
     * 初始化 - Example 会使用
     */
    public void initPropertyMap() {
        propertyMap = new HashMap<String, ColumnMeta>(entityClassColumns.size());
        for (ColumnMeta column : entityClassColumns) {
            propertyMap.put(column.getProperty(), column);
        }
    }

    public Map<String, ColumnMeta> getPropertyMap() {
        return propertyMap;
    }
}
