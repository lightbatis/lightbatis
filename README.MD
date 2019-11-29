```
█████████████████████████████████████  
█████████████████████████████████████  
████ ▄▄▄▄▄ █▀█ █▄▄▀▀ ▀▄█ █ ▄▄▄▄▄ ████  
████ █   █ █▀▀▀█ ▀▀ ████▄█ █   █ ████  
████ █▄▄▄█ █▀ █▀▀▄▀▀▄ ▀█ █ █▄▄▄█ ████  
████▄▄▄▄▄▄▄█▄▀ ▀▄█▄▀▄█ ▀ █▄▄▄▄▄▄▄████  
████ ▄ ▄ ▀▄  ▄▀▄▀▄ █ █▀ █ ▀ ▀▄█▄▀████  
████▄ ▄   ▄▄██▄█▀▄  ▄▄▀█ ▄▀  ▀█▀█████  
████ ▀▄▄█ ▄▄▄ ▄█▄▄▀▄▄█▀ ▀▀▀▀▀▄▄█▀████  
█████ ▀ ▄ ▄▄█▀  ▄██ █▄▄▀  ▄ ▀▄▄▀█████  
████▀▄  ▄▀▄▄█▄▀▄▀█▄▀▀ ▄ ▀▀▀ ▀▄ █▀████  
████ ██▄▄▄▄█▀▄▀█▀█▀▄▀█ ▀▄▄█▀██▄▀█████  
████▄███▄█▄█▀▄ █▄▀▄▄▀▄██ ▄▄▄ ▀   ████  
████ ▄▄▄▄▄ █▄█▄ ▄▄  ██▄  █▄█ ▄▄▀█████  
████ █   █ █ ▀█▄ ▀ ▄▄▀▀█ ▄▄▄▄▀ ▀ ████  
████ █▄▄▄█ █ ▄▀███▀▄▄▄▄▄ █▄▀  ▄ █████  
████▄▄▄▄▄▄▄█▄███▄█▄▄▄▄▄██▄█▄▄▄▄██████  
█████████████████████████████████████  
█████████████████████████████████████ 
``` 

# lightbatis 的设计理念
    lightbatis 是基于约定的命名规范和类型规范在 Mybatis 的基础上开发的，快速的、简洁的数据层访问工具包。
## 关于命名规范
    在日常的数据库访问的操作中，我们经常遇到三个对象，分别是： 数据库表、实体类、 数据访问的 Mapper 类，以下图：  
![关系图](./doc/lightbatis_members.png)  

    图中1处的表结构中的列名 kind_id , 第2处的实体类Member 的属性 kindId 是通过驼峰命名转化过来的，同时@Column 的注释也指定了列名
    为 kind_id, 第3处的 listMemberByKindId 方法中的参数 kindId 与 Member 类的属性 kindId 同名且同类型。由此分析他们三者之间存
    在着一系列的关联关系。我们就是把这种关联关系通过规范规定下来，形成默认的开发规则，基于这种常见的规则，便形成了统一数据访问的应用工具。
    
    通过分析我们得到以下结论：  
    **基于驼峰命名的规范通过属性、参数的名称建立表、实体类、Mapper 类的关联关系。** 以上图为例
    3处的 listMemberByKindId 方法 kindId 参数 = Member 属性 kindId = member 表 kind_id 列。
代码：
``` List<Member> listMemberByKindId(Short kindId)``` 所表达的意思就是
``` select * from member where kind_id = ? ``` 其中 kind_id = kindId, kindId 的值就是方法中 kindId 的值。基于此我们可以做  
以下的约定：  

一、SQL 查询条件约定  
    a) 简单类型  
        1. 作用在 Mapper 方法的参数上.     
        2. 类型为Java 的普通类型。（int,long,Integer,Long,String,Date等）   
        3. 参数的名称与实体类的属性名称相同。  
    b) 指定类型  
        1. 作用在 Mapper 方法的参数上  
        2. 类型为 com.querydsl.core.types.Predicate 如： ``` member.id.gt(1L).and(member.memberName.like("%慧慧慧%")) ```  
    c) 混合类型  
        1. 作用在 Mapper 方法的参数上  
        2. 可以是以上 a,b 两处的参数。  
         
二、 SQL 查询字段约定  
    类型为 com.querydsl.core.types.Path 的方法参数

三、 SQL 排序字段约定  
     类型为 com.querydsl.core.types.OrderSpecifier 的方法参数

Mapper 的定义示例：
```java
/**
 * MemberMapper 企业表 Mapper
 */
public interface MemberMapper extends LightbatisMapper<Member> {

    /**
     * 根据企业ID 获取指定的一条企业数据。
     * 相当于执行了SQL语句：select * from member where id = ?
     * @param id
     * @return
     */
    Member getMember(Long id);

    /**
     * 根据 kindId 获取指定类型的企业。
     * 相当于执行了SQL语句: select * from member where kind_id = ?
     * @param kindId
     * @return
     */
    List<Member> listMemberByKindId(Short kindId);

    /**
     * 列出所有的企业
     * @return
     */
    public List<Member> listMember();


    /**
     * SELECT id,member_name FROM member where kindId = ? ORDER BY id asc OFFSET ? LIMIT ?
     *
     * @param kindId
     * @param id
     * @param memberName
     * @return
     */
    public PageList<Member> listMembers(Path id, Path memberName, Short kindId, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where kindId = ? and ${predicate} order by id asc OFFSET ? LIMIT ?
     * @param kindId
     * @param predicate
     * @param id_asc
     * @param page
     * @return
     */
    public PageList<Member> listAllMembers(Short kindId, Predicate predicate, OrderSpecifier id_asc, Page page);

    /**
     * select * from member where ${predicates} order by id asc
     * @param id_asc
     * @param predicates
     * @return
     */
    public PageList<Member> listPredicatesMembers(OrderSpecifier id_asc, Predicate... predicates);//

    /**
     * select {paths} from member
     * @param paths
     * @return
     */
    public List<Member> listMemberFields(Path... paths);
}

```


## 关于类型规范


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
@Lightbatis()
`
## 使用上的一些约定
## SpringBoot 环境上使用 lightbatis 
    在 SpringBoot 的启动类上添加 @Lightbatis() 注释即可，如下：
```java
@Lightbatis()
@EnableLightbatisWeb()
@SpringBootApplication
public class SampleMapperApplication
```
### insert 操作

### update 操作

### select 操作

## 基于代码的约定

## 简便易用的可视化代码生成控制台

## 相关工程结构


## 相关技术点
### 使用 handlebars 用来动态生成 MyBatis SQL  

![Handlebars](https://handlebarsjs.com/guide/) https://handlebarsjs.com/guide/