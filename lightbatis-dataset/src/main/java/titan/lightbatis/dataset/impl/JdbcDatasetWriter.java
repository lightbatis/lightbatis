package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataSet;
import titan.lightbatis.dataset.DataWriter;
import titan.lightbatis.dataset.WriteException;

public class JdbcDatasetWriter implements DataWriter {
    private JdbcTemplate jdbcTemplate = null;

    private Boolean batch = false;
    public JdbcDatasetWriter(JdbcTemplate jdbcTemplate, Boolean batch) {
        this.jdbcTemplate = jdbcTemplate;
        this.batch = batch;
    }

    @Override
    public void write(DataSet dataSet) throws WriteException {
        if (batch) {
            JdbcTableBatchWriter tableWriter = new JdbcTableBatchWriter(jdbcTemplate);
            for (int i=0;i<dataSet.getTableSize();i++){
                tableWriter.write(dataSet.getTable(i));
            }
        }else {
            JdbcTableWriter tableWriter = new JdbcTableWriter(jdbcTemplate);
            for (int i=0;i<dataSet.getTableSize();i++){
                tableWriter.write(dataSet.getTable(i));
            }
        }

    }
}
