package titan.lightbatis.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.sample.domain.Member;

/**
 * MemberMapper 企业表 Mapper
 */
@Mapper
public interface MemberMapper extends LightbatisMapper<Member> {
	 public List<Member> listMember();
	 
	 Member getMember(Long id);
}

