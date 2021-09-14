### 1.1.5
1.  修复如果是MySQL 的自增长字段，获取增长后的值，并赋值给实体类。
```java
titan.lightbatis.mybatis.provider.impl.BaseMapperProvider.processKey
```
### 1.1.7
1. 如何处理自动增长的值
   * BaseMapperProvider.processKey
   * LightbatisMapper.insert --> BaseMapperProvider.insert
   
   * parameterObject -> Member
   * bindParameterCount = 1
   * providerMethodParameterTypes[0]=org.apache.ibatis.mapping.MappedStatement
   * titan.lightbatis.mybatis.LightbatisFactoryBean.checkDaoConfig 从这里开始检测扫描文件
   *  titan.lightbatis.mybatis.MapperBuilder.processMappedStatement
   * LightbatisMapperAnnotationBuilder 可以生成 KeyGenerator 类
   
2. 流程
   * titan.lightbatis.mybatis.LightbatisFactoryBean.checkDaoConfig
   * LightbatisMapperAnnotationBuilder.parseStatement(Method)
   * LightbatisMapperAnnotationBuilder.getSqlSourceFromAnnotations -> ProviderSqlSource
   