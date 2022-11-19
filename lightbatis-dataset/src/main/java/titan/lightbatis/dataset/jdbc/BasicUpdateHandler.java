package titan.lightbatis.dataset.jdbc;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import titan.lightbatis.dataset.exception.SQLRuntimeException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasicUpdateHandler extends  BasicHandler implements UpdateHandler{
    public BasicUpdateHandler(JdbcTemplate jdbcTemplate,String sql) {
        super(jdbcTemplate, sql);
    }

//    @Override
//    public int execute(Object[] args) throws SQLRuntimeException {
//        return 0;
//    }

    @Override
    public int execute(Object[] args, int[] argTypes) throws SQLRuntimeException {
        try{
            int count = jdbcTemplate.update(getSql(), args, argTypes);
            return count;
        }catch (Throwable ex) {
            ex.printStackTrace(System.out);
            throw new SQLRuntimeException("\r\n" + getSql() + "\r\n" + ArrayUtils.toString(args), ex);
        }

    }
}
