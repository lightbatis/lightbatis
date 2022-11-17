/**
 * ColumnSchema.java
 * Copyright(c) (北京)信息技术有限公司.
 * All right reserved.
 */

package titan.lightbatis.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="列的结构信息")
public class ColumnSchema implements Serializable {

	/** 对这个字段的描述 */

	private static final long serialVersionUID = 9018320467620847323L;
	@ApiModelProperty("列名")
	private String columnName = null;
	@ApiModelProperty("列的类型")
	private Class<?> columnClz = null;
	@ApiModelProperty("列的类型名称")
	private String typeName = null;
	private Integer length = null;
	@ApiModelProperty("是否是主键")
	private boolean primary = false;
	@ApiModelProperty("列的注释")
	private String common = null;
	@ApiModelProperty("JDBC Type")
	private int type = 0;
	private Integer columnDigits = null;
	private int columnIndex = 0;
	private int nullable = 0;
	//实体Bean 中的属性名称，类的字段名称
	@ApiModelProperty("实体Bean 中的属性名称，类的字段名称")
	private String propertyName;

	private boolean writable = true;

	public ColumnSchema() {
		super();
	}
	
	public ColumnSchema(String columnName) {
		super();
		this.columnName = columnName;
	}

	public ColumnSchema(String columnName, Class<?> columnClz, String typeName) {
		super();
		this.columnName = columnName;
		this.columnClz = columnClz;
		this.typeName = typeName;
	}

	public ColumnSchema(String columnName, String typeName, Integer length,
			boolean primary, String common) {
		super();
		this.columnName = columnName;
		this.typeName = typeName;
		this.length = length;
		this.primary = primary;
		this.common = common;
	}

	public ColumnSchema(String columnName, String typeName, Integer length,
			boolean primary, String common, Class<?> columnClz) {
		super();
		this.columnName = columnName;
		this.typeName = typeName;
		this.length = length;
		this.primary = primary;
		this.common = common;
		this.columnClz = columnClz;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Class<?> getColumnClz() {
		return columnClz;
	}

	public void setColumnClz(Class<?> columnClz) {
		this.columnClz = columnClz;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	/**
	 * 这一列，按数据类型实际占用的长度
	 *
	 * @return
	 */
	public int getTypeLength() {
		int typeLength = 0;
		switch (typeName) {
		case "varchar":
			// UTF 的编码时占用三个字节
			typeLength = length * 3;
			break;
		case "long":
			typeLength = 8;
			break;
		case "float":
			typeLength = 4;
			break;
		case "double":
			typeLength = 8;
			break;
		case "int":
			typeLength = 4;
			break;
		case "date":
			typeLength = 3;
			break;
		case "datetime":
			typeLength = 8;
			break;
		case "timestamp":
			typeLength = 4;
			break;
		}
		return typeLength;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	@Override
	public String toString() {
		return "ColumnSchema [columnName=" + columnName + ", columnClz="
				+ columnClz + ", typeName=" + typeName + ", length=" + length
				+ ", primary=" + primary + ", common=" + common + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnName == null) ? 0 : columnName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnSchema other = (ColumnSchema) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		return true;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getColumnDigits() {
		return columnDigits;
	}

	public void setColumnDigits(Integer columnDigits) {
		this.columnDigits = columnDigits;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}


}
