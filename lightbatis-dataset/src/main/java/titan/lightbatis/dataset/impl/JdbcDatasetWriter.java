package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataSet;
import titan.lightbatis.dataset.DataWriter;
import titan.lightbatis.dataset.WriteException;

public class JdbcDatasetWriter implements DataWriter {
    private JdbcTemplate jdbcTemplate = null;

    public JdbcDatasetWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(DataSet dataSet) throws WriteException {
        JdbcTableWriter tableWriter = new JdbcTableWriter(jdbcTemplate);
        for (int i=0;i<dataSet.getTableSize();i++){
            tableWriter.write(dataSet.getTable(i));
        }
    }
}
