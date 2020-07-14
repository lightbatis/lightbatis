package titan.lightbatis.mapper;

import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;

import java.util.List;

public interface CrudMapper<T,PK> extends LightbatisMapper<T>{
    /**
     * 根据主键 [id] 获取一条记录()
     * @param id 主键 主键编号
     * @return 返回主键对应的对象
     */

    public T get(PK id);

    /**
     * 分页查询所有的数据
     * @param page
     * @param <T>
     * @return
     */
    public <T> PageList<T> list(Page page);

    /**
     * 按条件查找列表的数据
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> findList(T entity);

    /**
     * 根据输入的实体对象，查询属性不为 null 的指定的一个对象
     * @param entity
     * @return
     */
    public T getByEntity(T entity);

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public PageList<T> findPage(Page page, T entity);
}
