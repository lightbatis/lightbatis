DROP TABLE member;/*SQL@Run*//*SkipError*/
CREATE TABLE member(
    ID BIGINT NOT NULL   COMMENT '序号' ,
    member_name VARCHAR(64)    COMMENT '企业名称' ,
    kind_id INT    COMMENT '企业性质' ,
    verify_status VARCHAR(32)    COMMENT '审核状态' ,
    REVISION INT    COMMENT '乐观锁' ,
    CREATED_BY VARCHAR(32)    COMMENT '创建人' ,
    CREATED_TIME DATETIME    COMMENT '创建时间' ,
    UPDATED_BY VARCHAR(32)    COMMENT '更新人' ,
    UPDATED_TIME DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (ID)
) COMMENT = '企业表 ';/*SQL@Run*/
ALTER TABLE member COMMENT '企业表';/*SQL@Run*/
DROP TABLE 企业员工表;/*SQL@Run*//*SkipError*/
CREATE TABLE 企业员工表(
    ID BIGINT NOT NULL   COMMENT '序列号' ,
    member_id BIGINT    COMMENT '企业ID' ,
    staff_name VARCHAR(32)    COMMENT '员工名称' ,
    status VARCHAR(32)    COMMENT '状态' ,
    REVISION INT    COMMENT '乐观锁' ,
    CREATED_BY VARCHAR(32)    COMMENT '创建人' ,
    CREATED_TIME DATETIME    COMMENT '创建时间' ,
    UPDATED_BY VARCHAR(32)    COMMENT '更新人' ,
    UPDATED_TIME DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (ID)
) COMMENT = '企业员工表 ';/*SQL@Run*/
ALTER TABLE 企业员工表 COMMENT '企业员工表';/*SQL@Run*/
