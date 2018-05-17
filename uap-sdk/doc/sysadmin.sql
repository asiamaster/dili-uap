/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/17 星期一 15:48:03                       */
/*==============================================================*/


drop table if exists data_auth;

drop table if exists menu;

drop table if exists resource;

drop table if exists role;

drop table if exists role_menu;

drop table if exists role_permission;

drop table if exists user;

drop table if exists user_data_auth;

drop table if exists user_role;

/*==============================================================*/
/* Table: data_auth                                             */
/*==============================================================*/
create table data_auth
(
   id                   bigint(20) not null,
   type                 varchar(20) not null comment '数据权限分类##这个由具体的业务系统确定',
   data_id              varchar(50) not null comment '数据权限ID##业务编号',
   name                 varchar(50) not null comment '名称',
   parent_data_id       bigint(20) not null default 0 comment '父级数据ID##外键，关联父级数据权限',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=34768 DEFAULT CHARSET=utf8;

alter table data_auth comment '数据权限';

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   id                   bigint(20) not null auto_increment comment '主键',
   parent_id            bigint(20) not null comment '父级菜单id',
   sort                 integer(11) not null comment '排序号',
   is_root              integer(11) not null comment '是否为根节点',
   menu_url             varchar(255) comment '菜单url',
   name                 varchar(255) not null comment '名称',
   description          varchar(255) not null comment '描述',
   target               integer(4) comment '打开链接的位置##{data:[{value:0, text:"当前窗口"},{value:1, text:"新开窗口"}]}',
   created              timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   yn                   integer(4) not null comment '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
   type                 integer(4) not null default 0 comment '类型##{data:[{value:0, text:"目录"},{value:1, text:"链接"}]}',
   icon_cls             varchar(40) comment '菜单图标',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8;

alter table menu comment '菜单';

/*==============================================================*/
/* Table: resource                                              */
/*==============================================================*/
create table resource
(
   id                   bigint(20) not null auto_increment comment '主键',
   resource_name        varchar(255) not null comment '名称',
   description          varchar(255) comment '描述',
   menu_id              bigint(20) not null comment '外键，关联menu表',
   code                 varchar(255) comment '编码##原resource URL',
   status               integer(4) not null comment '状态##{data:[{value:1, text:"启用"},{value:0, text:"停用"}]}',
   created              timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   yn                   integer(4) not null comment '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
   primary key (id),
   key FK_RESOURCE_NAVBAR (menu_id)
)
ENGINE=InnoDB AUTO_INCREMENT=11623 DEFAULT CHARSET=utf8;

alter table resource comment '资源';

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   bigint(20) not null auto_increment comment '主键',
   role_name            varchar(255) not null comment '角色名',
   description          varchar(255) not null comment '角色描述',
   created              timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   yn                   integer(4) not null comment '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

alter table role comment '角色';

/*==============================================================*/
/* Table: role_menu                                             */
/*==============================================================*/
create table role_menu
(
   id                   bigint(20) not null comment '主键',
   role_id              bigint(20) not null comment '角色ID##外键',
   menu_id              bigint(20) not null comment '菜单ID##外键',
   primary key (id),
   key FK_ROLEMENU_ROLE (role_id),
   key FK_MENUROLE_MENU (menu_id)
)
ENGINE=InnoDB AUTO_INCREMENT=39236 DEFAULT CHARSET=utf8;

alter table role_menu comment '角色菜单关系';

/*==============================================================*/
/* Table: role_permission                                       */
/*==============================================================*/
create table role_permission
(
   id                   bigint(20) not null comment '主键',
   role_id              bigint(20) not null comment '角色ID##外键',
   resource_id          bigint(20) not null comment '资源ID##外键',
   primary key (id),
   key FK_ROLEPERMISSION_ROLE (role_id),
   key FK_ROLEPERMISSION_RESOURCE (resource_id)
)
ENGINE=InnoDB AUTO_INCREMENT=1136 DEFAULT CHARSET=utf8;

alter table role_permission comment '角色资源关系';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   bigint(20) not null auto_increment comment '主键',
   user_name            varchar(50) not null comment '用户名',
   password             varchar(128) not null comment '密码',
   last_login_ip        varchar(20) comment '最后登录ip',
   last_login_time      timestamp comment '最后登录时间',
   created              timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   modified             timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   status               integer(4) not null default 1 comment '状态##状态##{data:[{value:1,text:"启用"},{value:0,text:"停用"}]}',
   yn                   integer(4) not null comment '有效性##数据有效性，用于逻辑删除##{data:[{value:0, text:"无效"},{value:1, text:"有效"}]}',
   real_name            varchar(64) not null default 'system_default' comment '真实姓名',
   serial_number        varchar(128) not null default '000' comment '用户编号',
   fixed_line_telephone varchar(24) not null comment '固定电话',
   cellphone            varchar(24) not null comment '手机号码',
   email                varchar(64) not null comment '邮箱',
   valid_time_begin     timestamp default CURRENT_TIMESTAMP comment '有效时间开始点',
   valid_time_end       timestamp comment '有效时间结束点',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=299 DEFAULT CHARSET=utf8;

alter table user comment '用户';

/*==============================================================*/
/* Table: user_data_auth                                        */
/*==============================================================*/
create table user_data_auth
(
   id                   bigint(20) not null,
   data_auth_id         bigint(20) not null comment '数据权限表id',
   user_id              bigint(20) not null comment '用户id',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=34768 DEFAULT CHARSET=utf8;

alter table user_data_auth comment '数据权限';

/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
   id                   bigint(20) not null comment '主键',
   user_id              bigint(20) not null comment '用户id##外键',
   role_id              bigint(20) not null comment '角色id##外键',
   primary key (id),
   key FK_USERROLE_USER (user_id),
   key FK_USERROLE_ROLE (role_id)
)
ENGINE=InnoDB AUTO_INCREMENT=3277 DEFAULT CHARSET=utf8;

alter table user_role comment '用户角色关系';

