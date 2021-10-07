/**
 * 
 */
package titan.lightbatis.mybatis.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import titan.lightbatis.configuration.MapperConfig;
import titan.lightbatis.mapper.LightbatisMapper;
import titan.lightbatis.mybatis.LightbatisProperties;
import titan.lightbatis.scan.ClassPathMapperScanner;
import titan.lightbatis.table.DataSourceTableSchemaManager;
import titan.lightbatis.table.ITableSchemaManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author lifei
 *
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class  })
@EnableConfigurationProperties({ LightbatisProperties.class, MybatisProperties.class })
@AutoConfigureBefore({ MybatisAutoConfiguration.class})
public class LightbatisAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(LightbatisAutoConfiguration.class);

	private final LightbatisProperties lightProperties;

	@Autowired
	private MybatisProperties properties;

	private final Interceptor[] interceptors;

	private final ResourceLoader resourceLoader;

	private final DatabaseIdProvider databaseIdProvider;

//	@Autowired
//	private List<SqlSessionFactory> sqlSessionFactoryList;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ITableSchemaManager tableSchemaManager = null;
	public LightbatisAutoConfiguration(LightbatisProperties properties,
			ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider) {
		this.lightProperties = properties;
		this.interceptors = interceptorsProvider.getIfAvailable();
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		//this.lightProperties.setConfiguration(new LightbatisConfiguration());
		
	}

	// public MybatisLightAutoConfiguration(MybatisProperties properties) {
	// this.properties = properties;
	// this.properties.setConfiguration(new MyLightConfiguration());
	// }

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
			Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
			Assert.state(resource.exists(), "Cannot find config location: " + resource
					+ " (please add config file or check your Mybatis configuration)");
		}
		//this.properties.setConfiguration(new LightbatisConfiguration());
		//ITableSchemaManager tableSchemaManager2 = ITableSchemaManager.getInstance();
		//System.out.println(tableSchemaManager);
		// System.out.println("sql session factory list " + sqlSessionFactoryList);
		// for (SqlSessionFactory factory: sqlSessionFactoryList) {
		// System.out.println(factory);
		// System.out.println(factory.getConfiguration());
		// }
		//checkQueryMapper();
	}

	//
	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);

		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		factory.setConfiguration(properties.getConfiguration());
		if (this.properties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}

		return factory.getObject();
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = this.properties.getExecutorType();
		if (executorType != null) {
			return new SqlSessionTemplate(sqlSessionFactory, executorType);
		} else {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	}
	
	@Bean
	@ConditionalOnMissingBean(ITableSchemaManager.class)
	public ITableSchemaManager tableSchemaManager() {
		ITableSchemaManager tableSchemaManager = new DataSourceTableSchemaManager();
		
		return tableSchemaManager;
	}

	/**
	 * This will just scan the same base package as Spring Boot does. If you want
	 * more power, you can explicitly use
	 * {@link org.mybatis.spring.annotation.MapperScan} but this will get typed
	 * mappers working correctly, out-of-the-box, similar to using Spring Data JPA
	 * repositories.
	 */
	public static class AutoConfiguredMapperScannerRegistrar
			implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

		private BeanFactory beanFactory;

		private ResourceLoader resourceLoader;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {

			logger.debug("Searching for mappers annotated with @Lightbatis");

			ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

			try {
				if (this.resourceLoader != null) {
					scanner.setResourceLoader(this.resourceLoader);
				}

				List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
				//String dalPackage = titan.lightbatis.MybatisApplication.class.getPackage().getName();
				//if (!packages.contains(dalPackage)) {
					//packages.add(dalPackage);
				//	packages.add("com.aeotrade");
				//}
				if (logger.isDebugEnabled()) {
					for (String pkg : packages) {
						logger.debug("Using auto-configuration base package '{}'", pkg);
					}
				}
				//packages.add("qutm.framework.provider");
//				for (String packageName: packages) {
//					System.out.println("扫描包 ======= " + packageName);
//				}
				scanner.setAnnotationClass(Mapper.class);
				//scanner.setMarkerInterface(LightbatisMapper.class);
				scanner.registerFilters();
				//scanner.doScan(StringUtils.toStringArray(packages));
			} catch (IllegalStateException ex) {
				logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.", ex);
			}
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}

		@Override
		public void setResourceLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}
	}

	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating
	 * instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this
	 * will bring in a bean registrar and automatically register components based on
	 * the same component-scanning path as Spring Boot itself.
	 */
	@Configuration
	@Import({ AutoConfiguredMapperScannerRegistrar.class })
	@ConditionalOnMissingBean(LightbatisMapperFactoryBean.class)
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			logger.debug("No {} found.", LightbatisMapperFactoryBean.class.getName());
		}
	}
}
