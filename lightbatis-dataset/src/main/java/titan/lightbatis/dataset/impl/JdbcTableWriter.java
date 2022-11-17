package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataTable;
import titan.lightbatis.dataset.TableWriter;

public class JdbcTableWriter implements TableWriter {
    private JdbcTemplate jdbcTemplate = null;
    @Override
    public void write(DataTable table) {
    }
}
