/**
 * 
 */
package titan.lightbatis.result;

import org.apache.ibatis.session.RowBounds;

/**
 * @author lifei
 *
 */
public class Page extends RowBounds {

	// 每页的条数
	private int pageSize = 10;
	// 当前的页
	private int pageNo = 1;

	/**
	 * 
	 */
	public Page(int pageSize, int pageNo) {
		super(((pageNo - 1) < 0 ? 0 : (pageNo - 1)) * pageSize, pageSize);
	}

}
