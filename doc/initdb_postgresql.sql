drop table member ;
/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member (
                        id                   int8                 not null,
                        member_name          varchar(128)         null,
                        kind_id              int4                 null,
                        staff_id             int8                 null,
                        staff_name           varchar(32)          null,
                        verify_status        varchar(32)          null,
                        status               int4         null,
                        revision             int4                 null,
                        created_by           int8                 null,
                        created_time         timestamp            null,
                        updated_by           int8                 null,
                        updated_time         timestamp            null,
                        constraint pk_member primary key (id)
);

comment on table member is
    '企业表';

comment on column member.id is
    'ID';

comment on column member.member_name is
    '企业名称';

comment on column member.kind_id is
    '企业类型';

comment on column member.verify_status is
    '企业审核状态';

comment on column member.status is
    '数据状态，逻辑删除字段 1 = 删除， 0 = 没有删除';

comment on column member.staff_id is
    '企业联系人';

comment on column member.staff_name is
    '企业联系人名称';

comment on column member.revision is
    '乐观锁';

comment on column member.created_by is
    '创建人';

comment on column member.created_time is
    '创建时间';

comment on column member.updated_by is
    '更新人';

comment on column member.updated_time is
    '更新时间';


drop table  staff;

CREATE TABLE staff(
                      ID int8 NOT NULL,
                      member_id int8,
                      staff_name varchar(32),
                      status int4,
                      REVISION int4,
                      CREATED_BY varchar(32),
                      CREATED_TIME timestamp,
                      UPDATED_BY varchar(32),
                      UPDATED_TIME timestamp,
                      PRIMARY KEY (ID)
);

COMMENT ON TABLE staff IS '企业员工表';;
COMMENT ON COLUMN staff.ID IS '序列号';;
COMMENT ON COLUMN staff.member_id IS '企业ID';;
COMMENT ON COLUMN staff.staff_name IS '员工名称';;
COMMENT ON COLUMN staff.status IS '数据状态，逻辑删除字段 1 = 删除， 0 = 没有删除';;
COMMENT ON COLUMN staff.REVISION IS '乐观锁';;
COMMENT ON COLUMN staff.CREATED_BY IS '创建人';;
COMMENT ON COLUMN staff.CREATED_TIME IS '创建时间';;
COMMENT ON COLUMN staff.UPDATED_BY IS '更新人';;
COMMENT ON COLUMN staff.UPDATED_TIME IS '更新时间';;