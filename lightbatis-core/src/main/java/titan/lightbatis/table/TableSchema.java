/**
 * TableSchema.java
 * Copyright(c) (北京)信息技术有限公司.
 * All right reserved.
 */

package titan.lightbatis.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *<p>Title:表的描述</p>
 *<p>Description: 表结构描述</p>
 *<p>Copyright:Copyright(c)2010</p>
 *<p>Company:</p>
 */
@Data
@ApiModel(value="表的结构信息", description="表的结构信息")
public class TableSchema implements Serializable {

	/** 对这个字段的描述 */

	private static final long serialVersionUID = 8030961550505000098L;
	@ApiModelProperty(value = "主键列")
	private List<String> primaryKeys = new ArrayList<String>();
	@ApiModelProperty(value = "该表所有的列")
	private List<ColumnSchema> columns = new ArrayList<ColumnSchema>();
	private String dbSchema = null;
	@ApiModelProperty("表名")
	private String tableName = null;
	private String ds = null;
	@ApiModelProperty("注释")
	private String common = null;
	private String engine = null;
	@ApiModelProperty(value = "该表对应实体名称")
	private String entityName;
	public void addPrimaryKey(String key){
		primaryKeys.add(key);
	}

	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	/**
	 *对方法功能的描述
	 *@param columnName
	 *@param clazz
	 */

	public void addColumn(String columnName, Class<?> clazz, String typeName) {
		columns.add(new ColumnSchema(columnName, clazz, typeName));
	}
	public void addColumn(ColumnSchema col){
		columns.add(col);
		if (col.isPrimary()){
			primaryKeys.add(col.getColumnName());
		}
	}
	public List<ColumnSchema> getColumns() {
		return columns;
	}

	/**
	 *对方法功能的描述
	 *@param key
	 *@return
	 */

	public ColumnSchema getColumn(String key) {
		for (ColumnSchema c:columns){
			if (c.getColumnName().equals(key)){
				return c;
			}
		}
		return null;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the ds
	 */
	public String getDs() {
		return ds;
	}

	/**
	 * @param ds the ds to set
	 */
	public void setDs(String ds) {
		this.ds = ds;
	}

	/**
	 * 获取一行的数据长度。
	 * @return
	 */
	public int getRowLength(){
		int length = 0;
		for (ColumnSchema col:columns){
			length = length + col.getTypeLength();
		}

		return length;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TableSchema [ds=" + ds + ", dbSchema=" + dbSchema
				+ ", tableName=" + tableName + ", primaryKeys=" + primaryKeys
				+ ", columns=" + columns + "]";
	}
	/**
	 * 添加主键字段
	 * @param field
	 */
	public void setPrimaryField(String field, boolean primary) {
		for (ColumnSchema col:columns){
			if (col.getColumnName().equals(field)) {
				col.setPrimary(primary);
			}
		}
		
	}

}
