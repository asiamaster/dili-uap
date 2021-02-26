package com.dili.uap.as.mapper;

import com.dili.ss.base.MyMapper;
import com.dili.uap.as.domain.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资源管理者
 */
public interface ResourceMapper extends MyMapper<Resource> {

	/**
	 * 根据用户id查询资源列表
	 * 
	 * @param userId
	 * @param systemType
	 * @return
	 */
	List<Resource> listByUserId(@Param("userId")Long userId, @Param("systemType")Integer systemType);

	/**
	 * 根据用户id和系统id查询资源列表
	 * 
	 * @param userId
	 * @param systemId
	 * @return
	 */
	List<Resource> listByUserIdAndSystemId(Long userId, Long systemId);

	List<String> findResourceCodeByUserId(Long userId);

	/**
	 * 根据菜单url，查询资源id
	 * 
	 * @param param key为url和userId
	 * @return
	 */
	List<String> listResourceCodeByMenuUrl(Map param);

	/**
	 * 根据用户ID获取用户所属权限信息名称
	 * 
	 * @param userId        用户名
	 * @param resourceCodes 权限码列表
	 * @return
	 */
	List<String> listResourceCodesByUserId(@Param("userId") Long userId, @Param("resourceCodes") List<String> resourceCodes);
}