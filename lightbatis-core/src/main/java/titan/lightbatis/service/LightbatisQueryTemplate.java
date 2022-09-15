package titan.lightbatis.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import titan.lightbatis.mapper.MapperManager;
import titan.lightbatis.mybatis.meta.StatementFragment;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;

import java.util.*;

@Slf4j
@Service
public class LightbatisQueryTemplate implements InitializingBean, ApplicationContextAware {

    @Autowired
    private SqlSessionTemplate primarySessionTemplate = null;
    @Autowired
    private MapperManager mapperManager = null;
    /**
     *
     * @param statementId Mybatis 的查询ID
     * @param params 查询的条件参数
     * @param page 分页
     * @param fetchMappingColumn 获取这个查询的映射信息。
     * @return
     */
    public List<Map<String, Object>> list(String statementId, Map<String,Object> params, Page page, boolean fetchMappingColumn) {
        List<Map<String,Object>> tmpList = new ArrayList<>();
        if (page == null) {
            tmpList = primarySessionTemplate.selectList(statementId, params);
        } else {
            tmpList = primarySessionTemplate.selectList(statementId, params, page);
        }
        final List<Map<String,Object>> dataList = new ArrayList<>(tmpList);
        if (fetchMappingColumn) {
            //读取所有关联表
             List<StatementFragment> stmtList = mapperManager.getStatementMappingColumns(statementId);
            stmtList.forEach( stmt ->{
                String stmtId = stmt.getStatementId();
                Set<Object> values = new HashSet<>();

                dataList.forEach(item -> {
                    if (item.containsKey(stmt.getSourceColumn())) {
                        values.add(item.get(stmt.getSourceColumn()));
                    }
                });

                Map<String,Object> stmtParams = new HashMap<>();
                stmtParams.put("values", values);

                String fragmentStmtId = stmt.getStatementId();
                try{
                    //查询关联表的数据
                    List<Map<String,Object>> stmtDataList = primarySessionTemplate.selectList(fragmentStmtId, stmtParams);
                    stmtDataList.forEach(fragment ->{
                        // 将数据关联到主表上去
                        dataList.stream().filter(data -> data.get(stmt.getSourceColumn()).toString().equals(fragment.get(stmt.getPkColumn()).toString())).forEach( item ->{
                            stmt.getMappingColumns().forEach(colMeta ->{
                               Object colValue = fragment.get(colMeta.getTargetColumn());
                               item.put(colMeta.getColumn(), colValue);
                            });
                        });
                    });
                }catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            });
        }
        if (page == null) {
            return  dataList;
        } else {
            PageList plist = new PageList();
            plist.addAll(dataList);
            if (tmpList instanceof PageList) {
                PageList tmpPage = (PageList) tmpList;
                plist.setTotalSize(tmpPage.getTotalSize());
            }
            return plist;
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       String[] names = applicationContext.getBeanNamesForType(SqlSessionTemplate.class);
        for (String name: names) {
            SqlSessionTemplate sessionTemplate =  applicationContext.getBean(name, SqlSessionTemplate.class);
            System.out.println(sessionTemplate);
        }
    }
}
