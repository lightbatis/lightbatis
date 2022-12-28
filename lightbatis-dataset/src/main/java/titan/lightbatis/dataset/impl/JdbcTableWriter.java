package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.*;

public class JdbcTableWriter implements TableWriter {
    private JdbcTemplate jdbcTemplate = null;
    public JdbcTableWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void write(DataTable table) throws WriteException {
        for (int i=0; i< table.getRowSize(); i++) {
            DataRow row = table.getRow(i);
            RowState state = row.getState();
            int count = state.update(jdbcTemplate, row);
            System.out.println("======= 成功执行 =" + count + " 条");
        }
    }
}
