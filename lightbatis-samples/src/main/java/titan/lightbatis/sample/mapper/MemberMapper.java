package titan.lightbatis.sample.mapper;

import java.util.List;

import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;
import titan.lightbatis.sample.domain.Member;
import static titan.lightbatis.sample.domain.QMember.member;
import com.querydsl.core.types.*;
/**
 * MemberMapper 企业表 Mapper
 */
public interface MemberMapper extends LightbatisMapper<Member> {
	 public List<Member> listMember();
	 
	 Member getMember(Long id);

	/**
	 * SELECT id,member_name FROM member where kindId = ? ORDER BY id asc OFFSET ? LIMIT ?
	 * @param kindId
	 * @param id
	 * @param memberName
	 * @return
	 */
	 public PageList<Member> listMembers(Path id, Path memberName, Short kindId, OrderSpecifier id_asc, Page page);

	public PageList<Member> listAllMembers(Short kindId, Predicate where, OrderSpecifier id_asc, Page page);

	public PageList<Member> listPredicatesMembers( OrderSpecifier id_asc, Predicate... predicates);//

	public List<Member> listMemberFields (Path... paths);
}

