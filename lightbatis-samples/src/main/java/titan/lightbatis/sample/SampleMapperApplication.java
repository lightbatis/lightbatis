package titan.lightbatis.sample;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

//import com.github.pagehelper.PageInterceptor;
import com.querydsl.core.types.Predicate;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import titan.lightbatis.annotations.Lightbatis;
import titan.lightbatis.configuration.MapperConfig;
import titan.lightbatis.mybatis.MapperBuilder;
import titan.lightbatis.mybatis.meta.EntityMetaManager;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;
import titan.lightbatis.sample.model.entity.Member;
import titan.lightbatis.sample.model.entity.MemberName;
import titan.lightbatis.sample.model.entity.query.QMember;
import titan.lightbatis.sample.mapper.MemberMapper;
import titan.lightbatis.sample.service.MemberCrudService;
import titan.lightbatis.web.annotations.EnableLightbatisWeb;

@Lightbatis(basePackages = {"titan.lightbatis.web.mapper","titan.lightbatis.sample.mapper"})
@EnableLightbatisWeb()
@SpringBootApplication
public class SampleMapperApplication implements CommandLineRunner {

	@Autowired
	private MemberMapper memberMapper = null;

	@Autowired
	private MemberCrudService memberCrudService = null;

	public static void main(String[] args) {
		SpringApplication.run(SampleMapperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//PageInterceptor pageInterceptor = null;
		testMain();
		//testUser();
	}
	private void testUser() {
		System.out.println("test user");
	}
	private void testMain() {
//		deleteMemberById();
//		deleteMemberBy();
//		insertMember();
//		updateMemberWhere();
//		insertMemberWithId();
//		updateMember();
//		listMember();
//
//		Member member = getMember();
//		deleteMember();
//		queryMember();
		listMembers();
//		listAllMembers();
//		listPredicatesMembers();
//		listMemberFields();
//		listMemberByKindId();
//		listMembersWithName();
//		listMemberNames();
//		listAllMemberNames();
//		getMember();
//		listMemberByPredicate();
//		listMemberByPredicates();
//		listMembersWithIn();
//		testService();
//		selectMember();
	}
	private void testService () {
		System.out.println("============>>> " + memberCrudService);
		Member member = memberCrudService.get(732258481750409216L);
		System.out.println(member);

		PageList<Member> members = memberCrudService.list(new Page(10,1));
		printMembers(members);

		member = new Member();
		member.setId(732258481750409216L);
		List<Member> memberList = memberCrudService.findList(member);
		printMembers(memberList);

		Member m2 = memberCrudService.getByEntity(member);
		System.out.println(m2);

		Member member2 = new Member();
		member2.setKindId(1);
		PageList<Member> memberPage = memberCrudService.findPage(new Page(10,1), member2);
		printMembers(memberPage);
	}
	private void listMemberByKindId() {
		List<Member> memberList = memberMapper.listMemberByKindId(null, Page.newPage(1));
		printMembers(memberList);
	}
	private void queryMember() {
		QMember query = QMember.member;
		List<Member> members = memberMapper.query(query.kindId.eq(1));
		for (Member member : members) {
			System.out.println(member);
		}
	}
	private void selectMember() {
		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listMembers(member.id, member.memberName, 1, member.id.asc(),new Page(1,10));
		for (Member m : members) {
			System.out.println(m);
		}
	}
	private void insertMember() {

		int count = 10;
		for (int i=0;i < count; i++ ){
			Member member = new Member();
			//member.setId(new Long(250 + i));
			member.setMemberName("慧 20191110 " + i);
			member.setKindId(1);
			memberMapper.insert(member);
			System.out.println(" insert id " + member.getId());

		}
	}

	private void updateMemberWhere() {
		QMember member = QMember.member;
		memberMapper.updateMemberName("UPdate Where ", member.id.eq(1L));
	}
	private void deleteMemberById() {
		int count = memberMapper.deleteMemberById(1L);
		System.out.println("count = " + count);
	}
	private void deleteMemberBy() {
		QMember member = QMember.member;
		int count = memberMapper.delete(member.id.gt(2));
		System.out.println("count = " + count);
	}

	private void insertMemberWithId() {
		Long id = 643173508322426880L;
		Member member = memberMapper.get(id);
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
		Member member = memberMapper.get(id);
		member.setId(id);
		member.setKindId(1);
		member.setMemberName("修改 慧 20191110 AT " + System.currentTimeMillis());

		memberMapper.updateByPrimaryKey(member);
	}

	private Member getMember() {
		Long id = 643173508322426880L;
		Member member = memberMapper.get(id);
		return member;
	}

	private void deleteMember () {
		Long id = 643173508322426880L;
		Member member = new Member();
		member.setId(id);
		int delCount = memberMapper.deleteByPrimaryKey(member);
		System.out.println("delete count = " + delCount);

		member = memberMapper.get(id);
		System.out.println(" member is null " + member);

	}

	private void listMember () {
		List<Member> members = memberMapper.listMember();
		for (Member member : members) {
			System.out.println(member);
		}
	}

	private void listMembers() {
		QMember member = QMember.member;
		List<Member> members =memberMapper.listMembers(member.id, member.memberName, 1, member.id.asc(), new Page(3,1));
		printMembers(members);
		for (Member m : members) {

			System.out.println("id =" +m.getId() +" , member="  +m);
		}
	}

	private void listAllMembers() {
		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listAllMembers(1, member.id.gt(1L).and(member.memberName.like("%慧慧慧%")), member.createdTime.desc(),new Page(5,1));
		for (Member m : members) {
			System.out.println(m);
		}

		System.out.println("============= total size = " + members.getTotalSize());
	}

	private  void listPredicatesMembers() {

		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listPredicatesMembers(member.id.asc(), member.memberName.like("%慧%"), member.kindId.eq(1));//
		printMembers(members);
	}

	private void listMemberFields() {
		QMember member = QMember.member;
		List<Member> members = memberMapper.listMemberFields(member.id, member.memberName, member.kindId);
		printMembers(members);
	}
	private void listMembersWithName() {
		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listMembersWithName(member.id, member.memberName,1,member.memberName.like("%慧%"), member.id.desc(),Page.newPage(1));
		printMembers(members);
	}
	private void printMembers(List<Member> members) {
		System.out.println("====================== print members " + members.size());
		for (Member m : members) {

			System.out.println(m);
		}
		if (members instanceof  PageList) {
			PageList pageList = (PageList)members;
			System.out.println("=========== total size = " + pageList.getTotalSize());
		}
	}

	private void listMemberNames() {
		List<String> names = memberMapper.listMemberNames(1);
		for (String name: names) {
			System.out.println(name);
		}
	}

	public void listAllMemberNames () {
		List<MemberName> memberNames = memberMapper.listAllMemberNames(1);
		for (MemberName mname: memberNames) {
			System.out.println(mname);
		}
	}

	public void listMemberByPredicate() {
		QMember member = QMember.member;
		Predicate where = member.id.gt(1).and( member.id.lt(643173508322426880L + 1000000000L));
		List<Member> members = memberMapper.listMemberByPredicate(where);
		printMembers(members);
	}

	public void listMemberByPredicates () {
		QMember member = QMember.member;
		PageList<Member> members = memberMapper.listMemberByPredicates(Page.newPage(1));
		printMembers(members);
		System.out.println(members.getTotalSize());
	}

	public void listMembersWithIn() {
		QMember member = QMember.member;
		// in 的查询
		PageList<Member> members = memberMapper.listMembersWithIn(member.id.in(1,643173508322426880L, 2,3 ));
		printMembers(members);
		// between 的查询
//		PageList<Member> memberList = memberMapper.listMembersWithIn(member.createdTime.between(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
//		printMembers(memberList);
	}
}
