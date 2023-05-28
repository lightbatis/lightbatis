package titan.lightbatis.sample.mapper;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import titan.lightbatis.annotations.LightDelete;
import titan.lightbatis.annotations.LightUpdate;
import titan.lightbatis.mapper.CrudMapper;
import titan.lightbatis.result.PageList;
import titan.lightbatis.result.Page;
import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.sample.model.entity.Member;
import titan.lightbatis.sample.model.entity.MemberName;

import java.util.List;
import java.util.Map;


/**
 * MemberMapper 企业表 Mapper
 */
public interface MemberMapper extends CrudMapper<Member,Long> {

    /**
     * 根据企业ID 获取指定的一条企业数据。
     * 相当于执行了SQL语句：select * from member where id = ?
     * @param id
     * @return
     */
    Member get( Long id);

    /**
     * 根据 kindId 获取指定类型的企业。
     * 相当于执行了SQL语句: select * from member where kind_id = ? offset=? limit = ?
     * @param kindId
     * @return
     */
    List<Member> listMemberByKindId(Integer kindId, Page page);

    /**
     * 列出所有的企业
     * @return
     */
    public List<Member> listMember();

    public List<Member> listMemberByPredicate(Predicate predicate);

    public PageList<Member> listMemberByPredicates(Page page, Predicate... predicates);
    /**
     * SELECT id,member_name FROM member where kindId = ? ORDER BY id asc OFFSET ? LIMIT ?
     *
     * @param kindId
     * @param id
     * @param memberName
     * @return
     */
    public PageList<Member> listMembers(Path id, Path memberName, Integer kindId, OrderSpecifier id_asc, Page page);

    /**
     * SELECT id,member_name FROM member where kindId = ? and {predicate} ORDER BY id asc OFFSET ? LIMIT ?
     *
     * @param kindId
     * @param id
     * @param memberName
     * @return
     */
    public PageList<Member> listMembersWithName(Path id, Path memberName, Integer kindId , Predicate predicate, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where kindId = ? and ${predicate} order by id asc OFFSET ? LIMIT ?
     * @param kindId
     * @param predicate
     * @param id_asc
     * @param page
     * @return
     */
    public PageList<Member> listAllMembers(Integer kindId, Predicate predicate, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where ${predicates} order by id asc
     * @param id_asc
     * @param predicates
     * @return
     */
    public PageList<Member> listPredicatesMembers(OrderSpecifier id_asc, Predicate... predicates);//

    public PageList<Member> listMembersWithIn(Predicate predicate);
    /**
     * select {paths} from member
     * @param paths
     * @return
     */
    public List<Member> listMemberFields(Path... paths);

    @Select("select member_name from member where kind_id=#{kindId}")
    public List<String> listMemberNames(Integer kindId);

    /**
     *
     * @param kindId
     * @return
     */
    @Select("select id, member_name as memberName from member where kind_id=#{kindId}")
    public List<MemberName> listAllMemberNames(Integer kindId);

    @LightUpdate()
    public Integer updateMemberName(String memberName, Predicate... predicates);
    @LightDelete()
    public int deleteMemberById(Long id);

}
