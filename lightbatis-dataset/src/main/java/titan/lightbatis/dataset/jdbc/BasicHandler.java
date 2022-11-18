package titan.lightbatis.dataset.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

public class BasicHandler {
    private String sql;
    private JdbcTemplate jdbcTemplate;

    public BasicHandler(JdbcTemplate jdbcTemplate, String sql) {
        this.jdbcTemplate = jdbcTemplate;
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
