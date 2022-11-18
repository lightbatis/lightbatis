package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.RowState;
import titan.lightbatis.dataset.TableWriter;

public class JdbcTableWriter implements TableWriter {
    private JdbcTemplate jdbcTemplate = null;
    public JdbcTableWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void write(DataTable table) {
        for (int i=0; i< table.getRowSize(); i++) {
            DataRow row = table.getRow(i);
            RowState state = row.getState();
            state.update(jdbcTemplate, row);
        }
    }
}
