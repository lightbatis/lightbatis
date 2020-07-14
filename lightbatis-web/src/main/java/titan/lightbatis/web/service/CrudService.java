package titan.lightbatis.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import titan.lightbatis.mapper.CrudMapper;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;

import java.util.List;

@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudMapper<T, PK>, T,PK> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;


    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public T get(PK id) {
        return dao.get(id);
    }

    public PageList<T> list(Page page){
        return dao.list(page);
    }

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public T getByEntity(T entity) {
        return dao.getByEntity(entity);
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public PageList<T> findPage(Page page, T entity) {
        return dao.findPage(page, entity);
    }

    /**
     * 添加一条记录，会自动生成 ID
     * @param entity
     * @return
     */
    public int insert(T entity) {
        return dao.insert(entity);
    }

    /**
     * 根据主键更新一条记录
     * @param entity
     * @return
     */
    public int updateByPrimaryKey(T entity) {
        return dao.updateByPrimaryKey(entity);
    }

    /**
     * 根据主键删除一条记录
     * @param entity
     * @return
     */
    public int deleteByPrimaryKey(T entity) {
        return dao.deleteByPrimaryKey(entity);
    }
//
//    /**
//     * 保存数据（插入或更新）
//     * @param entity
//     */
//    @Transactional(readOnly = false)
//    public void save(T entity) {
//
//        if (entity == null){
//            dao.insert(entity);
//        }else{
//            dao.updateByPrimaryKey(entity);
//
//        }
//
//    }
//
//    /**
//     * 系统用户保存数据（插入或更新）
//     * @param entity
//     */
//    @Transactional(readOnly = false)
//    public void saveBySystem(T entity) {
//
//
//    }
//
//    /**
//     * 删除数据
//     * @param entity
//     */
//    @Transactional(readOnly = false)
//    public void delete(T entity) {
//        dao.deleteByPrimaryKey(entity);
//    }
}
