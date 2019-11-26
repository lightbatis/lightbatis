/**
 * 
 */
package titan.lightbatis.sample;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.ibatis.mapping.MappedStatement;
import titan.lightbatis.configuration.MapperConfig;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.meta.MapperMetaManger;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.sample.domain.Member;
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


	public void testUpdateByPrimaryKey () throws IOException {
		MapperConfig config = new MapperConfig();
		EntityMetaManager.initEntityNameMap(Member.class,config,"");
		MapperBuilder mapperBuilder = new MapperBuilder();
		BaseMapperProvider provider = new BaseMapperProvider(MemberMapper.class, mapperBuilder);
		String sql= provider.updateEntityByPrimaryKey(Member.class);
		System.out.println("sql = ========");
		System.out.println(sql);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		MapperMetaMangerMain manager = new MapperMetaMangerMain();
		manager.testUpdateByPrimaryKey();
	}

}
