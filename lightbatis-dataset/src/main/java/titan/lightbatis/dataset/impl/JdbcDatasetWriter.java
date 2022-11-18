package titan.lightbatis.dataset.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataSet;
import titan.lightbatis.dataset.DataWriter;

public class JdbcDatasetWriter implements DataWriter {
    private JdbcTemplate jdbcTemplate = null;

    @Override
    public void write(DataSet dataSet) {
        JdbcTableWriter tableWriter = new JdbcTableWriter(jdbcTemplate);
        for (int i=0;i<dataSet.getTableSize();i++){
            tableWriter.write(dataSet.getTable(i));
        }
    }
}
