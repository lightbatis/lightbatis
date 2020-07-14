/**
 * 
 */
package titan.lightbatis.web.generate;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import titan.lightbatis.annotations.DalEntity;
import titan.lightbatis.web.DalConfig;
import titan.lightbatis.web.EntityRespository;

import java.util.Map;
import java.util.Set;

/**
 * @author lifei
 *
 */
@Service("generatedEntityScanner")
public class GeneratedEntityScanner{

	@Autowired
	private ApplicationContext applicationContext = null;
	
	@Autowired
	private EntityRespository entityRepository = null;

	@Autowired
	private DalConfig dalConfig = null;
	public void doScan() {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(DalEntity.class));
		Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents(dalConfig.getStartupPackage());
		for (BeanDefinition bd: beanDefinitionSet) {
			String clzName = bd.getBeanClassName();
			AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition)bd;
			AnnotationMetadata annMeta = abd.getMetadata();
			Map<String,Object> attributes = annMeta.getAnnotationAttributes(DalEntity.class.getName());
			String table = attributes.get("table").toString();
			entityRepository.addEntity(table, clzName);
		}

	}



}
