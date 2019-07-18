--schedule_job表新增字段:
`retry_count` INT(11) NULL DEFAULT NULL COMMENT '重试次数',
	`retry_interval` BIGINT(20) NULL DEFAULT NULL COMMENT '重试间隔/毫秒',
	`recovery_callback` VARCHAR(40) NULL DEFAULT NULL COMMENT '兜底回调',

--menu表新增字段
`shortcut` TINYINT(4) NULL DEFAULT '0' COMMENT '快捷菜单',

--system表新增字段
`is_client` TINYINT(4) NULL DEFAULT '0' COMMENT '是否C端',

--新增resource_link表
CREATE TABLE `resource_link` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`resource_code` VARCHAR(50) NULL DEFAULT NULL,
	`menu_id` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='资源链接关系表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=44
;
