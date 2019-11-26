package titan.lightbatis.generator;

import java.sql.Timestamp;

public class TimestampGenerator implements ILightbatisGenerator{

    @Override
    public Object generated(String mappedStatementId, Object entity, String property) {
        return new Timestamp(System.currentTimeMillis());
    }
}
