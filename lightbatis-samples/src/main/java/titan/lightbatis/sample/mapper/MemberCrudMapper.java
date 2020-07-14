package titan.lightbatis.sample.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import titan.lightbatis.mapper.CrudMapper;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;
import titan.lightbatis.sample.model.entity.Member;

public interface MemberCrudMapper extends CrudMapper<Member, Long> {


}
