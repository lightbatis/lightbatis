/**
 * 
 */
package titan.lightbatis.sample;

import java.lang.reflect.Method;

import titan.lightbatis.mybatis.meta.MapperMetaManger;
import titan.lightbatis.sample.mapper.MemberMapper;

/**
 * @author lifei114@126.com
 *
 */
public class MapperMetaMangerMain {

	/**
	 * 
	 */
	public MapperMetaMangerMain() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (Method method: MemberMapper.class.getMethods()) {
			if (method.getName().equals("listMembers")) {
				//MapperMetaManger.parse("", method);
			}
		}
	}

}
