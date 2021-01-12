package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.dto.ClientMenuDto;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends MyMapper<Menu> {

	/**
	 * 根据用户id查询用户拥有的所有菜单权限
	 * 
	 * @param param: userId和systemId
	 * @return
	 */
	List<Menu> listByUserAndSystemId(Map param);

	/**
	 * 根据用户id查询目录和链接菜单权限
	 * 
	 * @param param key为userId、 systemId和systemCode
	 * @return
	 */
	List<Menu> listDirAndLinksByUserId(Map param);

	/**
	 * 获取上级菜单的id列表，以逗号分隔
	 * 
	 * @param id
	 * @return
	 */
	String getParentMenus(String id);

	/**
	 * 根据url获取菜单详情，包含系统信息
	 * 
	 * @param url
	 * @return
	 */
	Map<String, Object> getMenuDetailByUrl(String url);

	/**
	 * 列出所有系统和菜单
	 * 用于菜单管理
	 * @return
	 */
	List<Map> listSystemMenu();

	/**
	 * 查询客户端需要的系统和菜单
	 * 
	 * @param param
	 * @return
	 */
	List<ClientMenuDto> listSystemAndMenus(Map<String, Object> param);
}