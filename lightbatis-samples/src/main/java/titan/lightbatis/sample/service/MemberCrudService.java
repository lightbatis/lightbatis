package titan.lightbatis.sample.service;

import org.springframework.stereotype.Service;
import titan.lightbatis.sample.mapper.MemberCrudMapper;
import titan.lightbatis.sample.model.entity.Member;
import titan.lightbatis.web.service.CrudService;

@Service
public class MemberCrudService extends CrudService<MemberCrudMapper, Member, Long> {


}
