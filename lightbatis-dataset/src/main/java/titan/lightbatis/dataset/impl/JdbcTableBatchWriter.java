package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.*;
import titan.lightbatis.dataset.states.AbstractRowState;
import titan.lightbatis.dataset.states.SqlContext;

import java.util.*;
import java.util.stream.Collectors;

public class JdbcTableBatchWriter implements TableWriter {
    private JdbcTemplate jdbcTemplate = null;
    public JdbcTableBatchWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void write(DataTable table) throws WriteException {
        List<SqlContext> sqlList = new ArrayList<>();
        for (int i=0; i< table.getRowSize(); i++) {
            DataRow row = table.getRow(i);
            RowState state = row.getState();
            AbstractRowState rowState = (AbstractRowState)state;
            SqlContext sqlContext = rowState.getSqlContext(row);
            sqlList.add(sqlContext);
        }
        if (sqlList.size() > 0) {
            Map<String, List<SqlContext>> sqlMap = sqlList.stream().collect(Collectors.groupingBy(SqlContext::getSql));
            Set<Map.Entry<String, List<SqlContext>>> sqlSet = sqlMap.entrySet();
            sqlSet.forEach(entry -> {
                String sql = entry.getKey();
                int[] types = entry.getValue().get(0).getArgTypes();
                List<Object[]> values = new ArrayList<>();

                entry.getValue().forEach(sqlContext -> {
                    values.add(sqlContext.getArgs());
                });
                System.out.println("批处理:" + sql);
                System.out.println("类型:" + Arrays.toString(types));
                System.out.println("批处理条数：" + values.size());
                jdbcTemplate.batchUpdate(sql, values, types);
            });

        }

    }
}
