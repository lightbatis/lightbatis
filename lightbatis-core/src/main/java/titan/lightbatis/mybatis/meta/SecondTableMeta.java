/**
 * 
 */
package titan.lightbatis.mybatis.meta;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;



/**
 * 从表的一些信息
 * @author lifei114@126.com
 *
 */
@Data
public class SecondTableMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2875566279521156886L;
	//表名
	private String tableName = null;
	//该表涉及到的列
	private Set<ColumnMeta> columns = new HashSet<>();
	//普通类型的表
	private Set<ColumnMeta> primitiveColumns = new HashSet<>();
	// List 类型的表
	private ColumnMeta listColumn = null;
	
	public void addColumn(ColumnMeta col) {
		columns.add(col);
		Class<?>  colClz = col.getJavaType();
		//返回结果是集合的类型
		if (List.class.isAssignableFrom(colClz)) {
			//listColumns.add(col);
			if (listColumn == null) {
				listColumn = col;
			} else {
				throw new IllegalArgumentException("主表关联从表只能有一个 List 类型的字段");
			}
		}  else  {
			//普通的类型
			primitiveColumns.add(col);
		}
	}
	
	
}
