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
	public static final Integer DEFAULT_PAGE_SIZE = 10;
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

	public static Page newPage( int pageNo) {
		return  new Page(DEFAULT_PAGE_SIZE, pageNo);
	}
}
