package titan.lightbatis.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import titan.lightbatis.mybatis.configuration.LightbatisConfiguration;

public interface IDynamicMapperScanner {

    public void start(SqlSessionFactory sqlSessionFactory, LightbatisConfiguration configuration);

}
