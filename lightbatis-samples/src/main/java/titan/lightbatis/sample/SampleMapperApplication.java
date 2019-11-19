package titan.lightbatis.sample;

import java.util.List;

import com.querydsl.core.types.Predicate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import titan.lightbatis.annotations.Lightbatis;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;
import titan.lightbatis.sample.domain.Member;
import titan.lightbatis.sample.domain.QMember;
import titan.lightbatis.sample.mapper.MemberMapper;
import titan.lightbatis.web.annotations.EnableLightbatisWeb;

@Lightbatis()//basePackages = "titan.lightbatis.sample.mapper"
@EnableLightbatisWeb()
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
		//queryMember();
		//listMembers();
		//listAllMembers();
		//listPredicatesMembers();
		listMemberFields();
	}

	private void queryMember() {
		QMember query = QMember.member;
		List<Member> members = memberMapper.query(query.kindId.eq(new Short((short) 1)));
		for (Member member : members) {
			System.out.println(member);
		}
	}
	private void selectMember() {
		QMember member = QMember.member;
		memberMapper.listMembers(member.id, member.memberName, (short)1, member.id.asc(),new Page(1,10));
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

	private void listMembers() {
		QMember member = QMember.member;
		List<Member> members =memberMapper.listMembers(member.id, member.memberName, (short)2, member.id.asc(), new Page(1,10));
		for (Member m : members) {
			System.out.println(m);
		}
	}

	private void listAllMembers() {
		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listAllMembers((short)1, member.id.gt(1L).and(member.memberName.like("%慧慧慧%")), member.id.asc(),new Page(5,1));
		for (Member m : members) {
			System.out.println(m);
		}

		System.out.println("============= total size = " + members.getTotalSize());
	}

	private  void listPredicatesMembers() {

		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listPredicatesMembers(member.id.asc(), member.memberName.like("%慧慧慧%"), member.kindId.eq((short)1));//
		printMembers(members);
	}

	private void listMemberFields() {
		QMember member = QMember.member;
		List<Member> members = memberMapper.listMemberFields(member.id, member.memberName, member.kindId);
		printMembers(members);
	}

	private void printMembers(List<Member> members) {
		for (Member m : members) {
			System.out.println(m);
		}
		if (members instanceof  PageList) {
			PageList pageList = (PageList)members;
			System.out.println("=========== total size = " + pageList.getTotalSize());
		}
	}
}
