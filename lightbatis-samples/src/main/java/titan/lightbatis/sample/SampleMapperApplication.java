package titan.lightbatis.sample;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import titan.lightbatis.annotations.Lightbatis;
import titan.lightbatis.sample.domain.Member;
import titan.lightbatis.sample.domain.QMember;
import titan.lightbatis.sample.mapper.MemberMapper;

@Lightbatis()//basePackages = "titan.lightbatis.sample.mapper"
@SpringBootApplication
public class SampleMapperApplication implements CommandLineRunner {

	@Autowired
	private MemberMapper memberMapper = null;

	public static void main(String[] args) {
		SpringApplication.run(SampleMapperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//insertMember();
		//updateMember();
		//listMember();
		//insertMemberWithId();
		//Member member = getMember();
		//deleteMember();
		queryMember();
	}

	private void queryMember() {
		QMember query = QMember.member;
		List<Member> members = memberMapper.query(query.kindId.eq(new Short((short) 1)));
		for (Member member : members) {
			System.out.println(member);
		}
	}
	private void insertMember() {
		Member member = new Member();
		int count = 1;
		for (int i=0;i < count; i++ ){
			//member.setId(new Long(250 + i));
			member.setMemberName("慧 20191110 " + i);
			memberMapper.insert(member);
			System.out.println(" insert id " + member.getId());
		}
	}
	
	private void insertMemberWithId() {
		Long id = 643173508322426880L;
		Member member = memberMapper.getMember(id);
		if (member == null) {
			member = new Member();
		} else {
			return;
		}
		
		member.setId(id);
		member.setMemberName("INSERT 慧 20191110 AT " + System.currentTimeMillis());
		
		memberMapper.insert(member);
	}

	private void updateMember() {
		Long id = 643173508322426880L;
		Member member = new Member();
		member.setId(id);
		member.setMemberName("修改 慧 20191110 AT " + System.currentTimeMillis());
		
		memberMapper.updateByPrimaryKey(member);
	}
	
	private Member getMember() {
		Long id = 643173508322426880L;
		Member member = memberMapper.getMember(id);
		return member;
	}
	
	private void deleteMember () {
		Long id = 643173508322426880L;
		Member member = new Member();
		member.setId(id);
		int delCount = memberMapper.deleteByPrimaryKey(member);
		System.out.println("delete count = " + delCount);
	}
	
	private void listMember () {
		List<Member> members = memberMapper.listMember();
		for (Member member : members) {
			System.out.println(member);
		}
	}

}
