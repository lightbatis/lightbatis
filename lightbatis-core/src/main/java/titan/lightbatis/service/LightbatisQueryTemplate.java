package titan.lightbatis.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import titan.lightbatis.mapper.MapperManager;
import titan.lightbatis.mybatis.meta.StatementFragment;
import titan.lightbatis.result.Page;
import titan.lightbatis.result.PageList;

import java.util.*;

@Slf4j
@Service
public class LightbatisQueryTemplate {

    @Autowired
    private SqlSessionTemplate sessionTemplate = null;

    /**
     *
     * @param statementId Mybatis 的查询ID
     * @param params 查询的条件参数
     * @param page 分页
     * @param fetchMappingColumn 获取这个查询的映射信息。
     * @return
     */
    public PageList<Map<String, Object>> list(String statementId, Map<String,Object> params, Page page, boolean fetchMappingColumn) {
        List<Map<String,Object>> dataList = sessionTemplate.selectList(statementId, params, page);
        if (fetchMappingColumn) {
            //读取所有关联表
             List<StatementFragment> stmtList = MapperManager.getManager().getStatementMappingColumns(statementId);
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
                    List<Map<String,Object>> stmtDataList = sessionTemplate.selectList(fragmentStmtId, stmtParams);
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
        return (PageList<Map<String, Object>>) dataList;
    }
}
