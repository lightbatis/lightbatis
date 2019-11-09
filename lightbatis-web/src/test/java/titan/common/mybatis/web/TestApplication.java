/**
 * 
 */
package titan.common.mybatis.web;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import titan.lightbatis.LightbatisApplication;
import titan.lightbatis.web.TitanDalApplication;

/**
 * @author lifei
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LightbatisApplication.class, TitanDalApplication.class})
public class TestApplication {
//
//	@Resource
//	private MemberMapper memberMapper = null;
//	@Test
//	public void testGet() {
//		System.out.println("================");
//	}
//
//	
//	@Test
//	public void testWebGetMember() {
//		Member member = memberMapper.get(639533578002104320L);
//		//Assert.assertNull(member);
//		System.out.println( "member is = " + member );
//	}
//	
//	@Test
//	public void testInsertMember() {
//		Member member = new Member();
//		member.setMemberName("存在慧贸");
//		memberMapper.insert(member);
//	}
}
