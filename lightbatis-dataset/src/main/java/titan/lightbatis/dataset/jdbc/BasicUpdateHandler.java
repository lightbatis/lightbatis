package titan.lightbatis.dataset.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.exception.SQLRuntimeException;

public class BasicUpdateHandler extends  BasicHandler implements UpdateHandler{
    public BasicUpdateHandler(JdbcTemplate jdbcTemplate,String sql) {
        super(jdbcTemplate, sql);
    }

    @Override
    public int execute(Object[] args) throws SQLRuntimeException {
        return 0;
    }

    @Override
    public int execute(Object[] args, Class[] argTypes) throws SQLRuntimeException {
        return 0;
    }
}
