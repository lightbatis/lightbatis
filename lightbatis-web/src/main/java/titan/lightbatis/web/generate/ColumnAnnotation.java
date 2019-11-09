/**
 * 
 */
package titan.lightbatis.web.generate;
import java.lang.annotation.Annotation;

import javax.persistence.Column;

import titan.lightbatis.table.ColumnSchema;
/**
 * @author lifei
 *
 */
public class ColumnAnnotation implements Column{
	private ColumnSchema col = null;
	
	/**
	 * @param col
	 */
	public ColumnAnnotation(ColumnSchema col) {
		super();
		this.col = col;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}

	@Override
	public String name() {
		return col.getColumnName();
	}

	@Override
	public boolean unique() {
		return false;
	}

	@Override
	public boolean nullable() {
		return col.getNullable() == 1;
	}

	@Override
	public boolean insertable() {
		return true;
	}

	@Override
	public boolean updatable() {
		return true;
	}

	@Override
	public String columnDefinition() {
		return null;
	}

	@Override
	public String table() {
		return null;
	}

	@Override
	public int length() {
		return col.getLength();
	}

	@Override
	public int precision() {
		return 0;
	}

	@Override
	public int scale() {
		return 0;
	}

}
