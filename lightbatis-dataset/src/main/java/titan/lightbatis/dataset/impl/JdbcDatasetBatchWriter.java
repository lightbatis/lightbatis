package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataSet;
import titan.lightbatis.dataset.DataWriter;
import titan.lightbatis.dataset.WriteException;

public class JdbcDatasetBatchWriter implements DataWriter {
    private JdbcTemplate jdbcTemplate = null;

    public JdbcDatasetBatchWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(DataSet dataSet) throws WriteException {
        JdbcTableBatchWriter tableWriter = new JdbcTableBatchWriter(jdbcTemplate);
        for (int i=0;i<dataSet.getTableSize();i++){
            tableWriter.write(dataSet.getTable(i));
        }
    }
}
