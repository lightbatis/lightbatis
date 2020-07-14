/**
 * 
 */
package titan.common.mybatis.web;

import com.querydsl.sql.codegen.DefaultNamingStrategy;

/**
 * @author lifei
 *
 */
public class EntityNameTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultNamingStrategy namingStrategy = new DefaultNamingStrategy();
		String clzName = namingStrategy.getClassName("order_sheet");
		System.out.println(clzName);
		
		String propertyName = namingStrategy.getPropertyName("ID", null);
		System.out.println(propertyName);
	}

}
