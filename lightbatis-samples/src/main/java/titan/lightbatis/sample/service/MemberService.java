package titan.lightbatis.sample.service;

import titan.lightbatis.result.PageList;
import javax.annotation.Generated;
import titan.lightbatis.result.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import titan.lightbatis.sample.model.entity.Member;
import titan.lightbatis.sample.mapper.MemberMapper;

/**
 * MemberService 企业表 Service
 */
@Generated("titan.lightbatis.web.generate.ServiceBeanSerializer")
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;



    /**
     * 根据主键 [id] 获取一条记录(企业表)
     * @param id 主键 ID
     * @return 返回主键对应的对象
     */
    public Member get(Long id) {
        return memberMapper.get(id);
    }

}

