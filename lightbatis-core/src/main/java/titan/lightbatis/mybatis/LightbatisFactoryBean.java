/**
 * 
 */
package titan.lightbatis.mybatis;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import titan.lightbatis.mybatis.handler.JacksonTypeHandler;

import static org.springframework.util.Assert.notNull;

/**
 * @author lifei114@126.com
 *
 */
public class LightbatisFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    private Class<T> mapperInterface;

    private boolean addToConfig = true;

    private MapperBuilder mapperBuilder;

    public LightbatisFactoryBean() {
        //intentionally empty
    }

    public LightbatisFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();

        notNull(this.mapperInterface, "Property 'mapperInterface' is required");

        Configuration configuration = getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            //处理 MyBatis 默认的处理规则
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
            //如果 MyBatis 不能处理的，尝试使用 Lightbatis 来处理
            logger.debug("尝试使用 lightbatis 来处理 = " + this.mapperInterface);
            LightbatisMapperAnnotationBuilder lightbatisBuilder = new LightbatisMapperAnnotationBuilder(configuration, this.mapperInterface);
            lightbatisBuilder.parse();
        } else if (this.addToConfig && configuration.hasMapper(this.mapperInterface)) {
            //如果 MyBatis 不能处理的，尝试使用 Lightbatis 来处理
            logger.debug("尝试使用 lightbatis 来处理 = " + this.mapperInterface);
            LightbatisMapperAnnotationBuilder lightbatisBuilder = new LightbatisMapperAnnotationBuilder(configuration, this.mapperInterface);
            lightbatisBuilder.parse();
        }
        configuration.getTypeHandlerRegistry().register(JacksonTypeHandler.class);
        //直接针对接口处理通用接口方法对应的 MappedStatement 是安全的，通用方法不会出现 IncompleteElementException 的情况
        if (configuration.hasMapper(this.mapperInterface) && mapperBuilder != null && mapperBuilder.isExtendCommonMapper(this.mapperInterface)) {
            mapperBuilder.processConfiguration(getSqlSession().getConfiguration(), this.mapperInterface);
        }
    }

    /**
     * Return the mapper interface of the MyBatis mapper
     *
     * @return class of the interface
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * Sets the mapper interface of the MyBatis mapper
     *
     * @param mapperInterface class of the interface
     */
    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    //------------- mutators --------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * Return the flag for addition into MyBatis config.
     *
     * @return true if the mapper will be added to MyBatis in the case it is not already
     * registered.
     */
    public boolean isAddToConfig() {
        return addToConfig;
    }

    /**
     * If addToConfig is false the mapper will not be added to MyBatis. This means
     * it must have been included in mybatis-config.xml.
     * <p/>
     * If it is true, the mapper will be added to MyBatis in the case it is not already
     * registered.
     * <p/>
     * By default addToCofig is true.
     *
     * @param addToConfig
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * 设置通用 Mapper 配置
     *
     * @param mapperBuilder
     */
    public void setMapperBuilder(MapperBuilder mapperBuilder) {
        this.mapperBuilder = mapperBuilder;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
