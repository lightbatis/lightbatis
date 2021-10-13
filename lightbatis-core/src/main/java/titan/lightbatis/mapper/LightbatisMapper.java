package titan.lightbatis.mapper;

import com.querydsl.core.types.Predicate;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.transaction.annotation.Transactional;
import titan.lightbatis.annotations.LightSave;
import titan.lightbatis.mybatis.provider.impl.BaseMapperProvider;

/**
 *
 * @author lifei114@126.com
 * @param <T>
 */
public interface LightbatisMapper<T> extends QueryMapper<T> {

    /**
     * 根据实体对象， 往数据库里插入一条记录。
     * @param record 属性添加了 @ID 注释，且值为 NULL, 将使用 SnowflakeId 算法自动生成一个，且值赋给该属性
     * @return 保存影响的条数
     */
    @InsertProvider(type = BaseMapperProvider.class, method = "insert")
    @Transactional(readOnly = false)
    int insert(T record);

    /**
     * 如果存在就更新，不存在就插入
     * @param record
     * @return
     */
    @Transactional(readOnly = false)
    @LightSave
    //@InsertProvider(type = BaseMapperProvider.class, method = "save")
    int save(T record);

    @UpdateProvider(type = BaseMapperProvider.class, method="updateByPrimaryKey")
    @Transactional(readOnly = false)
    int updateByPrimaryKey(T record);
    
    @DeleteProvider(type = BaseMapperProvider.class, method="deleteByPrimaryKey")
    @Transactional(readOnly = false)
    int deleteByPrimaryKey(T record);
}
