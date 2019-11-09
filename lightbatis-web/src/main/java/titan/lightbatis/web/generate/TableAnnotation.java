/**
 * 
 */
package titan.lightbatis.web.generate;
import java.lang.annotation.Annotation;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import titan.lightbatis.table.TableSchema;
/**
 * @author lifei
 *
 */
public class TableAnnotation implements Table{
	private TableSchema table = null;

	/**
	 * @param table
	 */
	public TableAnnotation(TableSchema table) {
		super();
		this.table = table;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}

	@Override
	public String name() {
		return table.getTableName();
	}

	@Override
	public String catalog() {
		return null;
	}

	@Override
	public String schema() {
		return table.getDbSchema();
	}

	@Override
	public UniqueConstraint[] uniqueConstraints() {
		return null ;
	}

}
