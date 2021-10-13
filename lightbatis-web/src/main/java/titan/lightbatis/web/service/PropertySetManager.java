package titan.lightbatis.web.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import titan.lightbatis.web.entity.LightPropertySet;
import titan.lightbatis.web.entity.propertyset.IPropertySet;
import titan.lightbatis.web.mapper.PropertyEntryMapper;

@Service
public class PropertySetManager implements InitializingBean,ApplicationContextAware {
    private static PropertySetManager instance = null;
    private ConfigurableApplicationContext applicationContext = null;
    @Autowired
    private PropertyEntryMapper propertyEntryMapper = null;

    public static PropertySetManager getInstance() {
        return instance;
    }

    public IPropertySet getPropertySetByGlobal(Long memberId) {
        String beanName = "PropertySet_"+memberId;
        if (applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName, LightPropertySet.class);
        }else {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LightPropertySet.class);
            beanDefinitionBuilder.addConstructorArgValue(propertyEntryMapper);
            beanDefinitionBuilder.addConstructorArgValue(String.valueOf(memberId));
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
            return applicationContext.getBean(beanName, LightPropertySet.class);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        PropertySetManager.instance = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = (ConfigurableApplicationContext)applicationContext;
    }
}
