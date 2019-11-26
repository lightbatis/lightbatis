package titan.lightbatis.sample.mapper;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;
import titan.lightbatis.sample.domain.Member;

import java.util.List;

/**
 * MemberMapper 企业表 Mapper
 */
public interface MemberMapper extends LightbatisMapper<Member> {

    /**
     * 根据企业ID 获取指定的一条企业数据。
     * 相当于执行了SQL语句：select * from member where id = ?
     * @param id
     * @return
     */
    Member getMember(Long id);

    /**
     * 根据 kindId 获取指定类型的企业。
     * 相当于执行了SQL语句: select * from member where kind_id = ?
     * @param kindId
     * @return
     */
    List<Member> listMemberByKindId(Short kindId);

    /**
     * 列出所有的企业
     * @return
     */
    public List<Member> listMember();


    /**
     * SELECT id,member_name FROM member where kindId = ? ORDER BY id asc OFFSET ? LIMIT ?
     *
     * @param kindId
     * @param id
     * @param memberName
     * @return
     */
    public PageList<Member> listMembers(Path id, Path memberName, Short kindId, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where kindId = ? and ${predicate} order by id asc OFFSET ? LIMIT ?
     * @param kindId
     * @param predicate
     * @param id_asc
     * @param page
     * @return
     */
    public PageList<Member> listAllMembers(Short kindId, Predicate predicate, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where ${predicates} order by id asc
     * @param id_asc
     * @param predicates
     * @return
     */
    public PageList<Member> listPredicatesMembers(OrderSpecifier id_asc, Predicate... predicates);//

    /**
     * select {paths} from member
     * @param paths
     * @return
     */
    public List<Member> listMemberFields(Path... paths);
}

