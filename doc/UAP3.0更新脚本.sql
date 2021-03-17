# resource_link
alter table resource_link add resource_id BIGINT AFTER id;
UPDATE resource_link rl, resource r SET rl.resource_id = r.id WHERE r.`code` = rl.resource_code;
ALTER TABLE `resource_link` DROP COLUMN `resource_code`;

# resource
ALTER TABLE `resource` add `system_type` INT AFTER `code`;

# menu
ALTER TABLE `menu` add `system_type` INT AFTER `type`;

# 菜单和资源表系统类型统一为WEB
UPDATE resource r SET r.system_type=1;
UPDATE menu m SET m.system_type=1;

# 根据资源链接，初始化添加角色菜单权限
INSERT INTO role_menu (role_id, menu_id)
SELECT DISTINCT rr.role_id, rl.menu_id
FROM role_resource rr INNER JOIN resource_link rl ON rr.resource_id = rl.resource_id;
