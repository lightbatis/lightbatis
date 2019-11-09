/**
 * 
 */
package titan.lightbatis.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import titan.lightbatis.web.generate.GeneratedEntityScanner;

/**
 * @author lifei
 *
 */
public class DalApplicationListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationStartedEvent) {
			ConfigurableApplicationContext context = ((ApplicationStartedEvent) event).getApplicationContext();
			StackTraceElement[] elements = new RuntimeException().getStackTrace();
			for (StackTraceElement element : elements) {
				if ("main".equals(element.getMethodName())) {
					String clzName = element.getClassName();
					String basePackage = StringUtils.substring(clzName, 0, StringUtils.lastIndexOf(clzName, "."));
					DalConfig.startupClass = clzName;
					DalConfig.applicationPackage = basePackage;
//					try {
//						DalConfig dalConfig = context.getBean(DalConfig.class);
//						dalConfig.startupClass = clzName;
//						
//						dalConfig.applicationPackage = basePackage;
//						if (StringUtils.isEmpty(dalConfig.getBasePackage())) {
//							dalConfig.setBasePackage(basePackage);
//						}
//					} catch (Exception ex) {
//						DalConfig.startupClass = clzName;
//						DalConfig.applicationPackage = basePackage;
//						
//						ex.printStackTrace(System.err);
//					}
				}
			}
//			ApplicationStartedEvent startedEvent = (ApplicationStartedEvent)event;

//			String[] names =context.getBeanNamesForAnnotation(Generated.class);
//			for (String name:names) {
//				System.out.println("name= " + name);
//			}
			//GeneratedEntityScanner scanner = context.getBean(GeneratedEntityScanner.class);
			//scanner.doScan();

		} else if (event instanceof ApplicationFailedEvent) {
			ApplicationFailedEvent failedEvent = (ApplicationFailedEvent) event;
			failedEvent.getException().printStackTrace(System.err);
		}
	}

}
