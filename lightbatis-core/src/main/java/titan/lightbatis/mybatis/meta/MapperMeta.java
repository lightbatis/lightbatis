/**
 *
 */
package titan.lightbatis.mybatis.meta;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用来记录 Mapper 的基本信息，仅记录 Mapper 的信息。
 * @author lifei114@126.com
 *
 */
@Data
public class MapperMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972593122879430943L;

	private Class<?> resultClz = null;
	
	/**
	 * 查询时的字段
	 */
	private Set<ParamMeta> projectionParams = new HashSet<>();
	
	/**
	 * 排序字段
	 */
	private Set<ParamMeta> orders = new HashSet<>();
	
	/**
	 * 条件字段。
	 */
	private Set<ParamMeta> predicateParams = new HashSet<> ();


	/**
	 * 本次使用中是否包括了 Path, OrderSpecifier 等类型 @see Path
	 */

	private boolean dynamicSQL = false;

	/**
	 * 该访问的 StatementID
	 */
	private String mappedStatementId = null;

	/**
	 * 参数的个数
	 */
	private int paramCount = 0;
	/**
	 * 是否可以分页
	 */
	private boolean pageable = false;

	/**
	 * 是否统计总数
	 */
	private boolean coutable = false;
	/**
	 * 添加查询字段
	 * @param meta
	 */
	public void addProjection(ParamMeta meta) {
		projectionParams.add(meta);
	}

	public void addOrder(ParamMeta meta) { orders.add(meta);}

	public void addPredicate(ParamMeta meta) { predicateParams.add(meta);}

}
