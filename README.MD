# lightbatis 统一数据访问层
lightbatis 统一数据层访问工具包，是在 Mybatis 的基础上，基于约定规范设计的增强功能集合。  
基于规范的约定，不需要添加任务的注释。

## 执行过程及原理
    1. 自定义 Lightbatis  注释，同时默认 Mapper 的接口是 LightbatisMapper
    2. Lightbatis 注释会 @import  LightbatisScannerRegistrar 扫描注册类。
    3. LightbatisScannerRegistrar 类 指定 factoryBean 为 LightbatisFactoryBean
    4. LightbatisScannerRegistrar 类 扫描到所有接口为 LightbatisMapper 的接口，该接口默认实现将由 LightbatisFactoryBean 代理
    5. LightbatisFactoryBean 默认继承了 SqlSessionDaoSupport, 在 checkDaoConfig 方法，先有默认实现检查原来 Mybatis 的默认实现
    6. LightbatisFactoryBean 的 checkDaoConfig 方法中，如果有 Mybatis 没有实现的方法，由 Lightbatis 来实现。
    7. Lightbatis 实现的方法是，将没有系统默认注释(@Select, @Insert, @Update, @Delete, @SelectProvider, @InsertProvider, @UpdateProvider, @DeleteProvider )的方法，转化成 MappedStatement 并注册到 MyBatis 的 Configuration 中去。
    

## 如何读懂代码

![lightbatis 导图](./lightbatis.png)  


`
@LightbatisScan()
`
## 使用上的一些约定

### insert 操作

### update 操作

### select 操作

## 基于代码的约定

## 简便易用的可视化代码生成控制台

## 相关工程结构