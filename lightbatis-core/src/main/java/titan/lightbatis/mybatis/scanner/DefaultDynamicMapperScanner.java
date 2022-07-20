package titan.lightbatis.mybatis.scanner;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;
import titan.lightbatis.mybatis.IDynamicMapperScanner;
import titan.lightbatis.mybatis.configuration.LightbatisAutoConfiguration;
import titan.lightbatis.mybatis.configuration.LightbatisConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class DefaultDynamicMapperScanner implements IDynamicMapperScanner, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LightbatisAutoConfiguration.class);
    private ApplicationContext applicationContext = null;
    protected SqlSessionFactory sqlSessionFactory = null;
    protected LightbatisConfiguration configuration = null;

    @Override
    public void start(SqlSessionFactory sqlSessionFactory, LightbatisConfiguration configuration) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.configuration = configuration;
        doStart();
    }

    public void stop() {

        doStop();
    }

    protected void doStart() {

    }

    protected void doStop() {
        logger.info("#################################################### 结束了 #######################");
    }

    public void addFile(File file) {
        logger.debug("############# add file " + file.getAbsolutePath());
        String fileName = file.getName();
        if (!fileName.endsWith(".xml")) {
            return;
        }
        removeFile(file);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            String resource = file.getAbsolutePath();
            XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            builder.parse();
            // 添加 Count 语句
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件添加失败! " + file.getAbsolutePath());
        }

    }

    public void removeFile(File file) {
        try{
           removeResource(file.getAbsolutePath());
        }catch(Exception exception) {
            logger.error("文件删除失败 ！" + file.getAbsolutePath());
            exception.printStackTrace();
        }
    }
    public void removeResource(String resource) {
        configuration.removeResource(resource);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        stop();
    }
}
