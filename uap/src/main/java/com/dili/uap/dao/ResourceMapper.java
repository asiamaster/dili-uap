package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.Resource;
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
	List<Resource> listByUserIdAndSystemId(@Param("userId")Long userId, @Param("systemId")Long systemId);

	/**
	 * 根据用户id和系统code查询资源列表
	 * @param userId
	 * @param systemCode
	 * @return
	 */
	List<String> listByUserIdAndSystemCode(@Param("userId")Long userId, @Param("systemCode")String systemCode);

	/**
	 * 根据用户id查询有权限的资源列表
	 * @param userId
	 * @param systemCode
	 * @return
	 */
	List<String> findResourceCodeByUserId(Long userId);

	/**
	 * 根据菜单url，查询资源id
	 * 
	 * @param param key为url和userId
	 * @return
	 */
	List<String> listResourceCodeByMenuUrl(Map param);

	/**
	 * 根据用户id和资源code判断是否存在
	 * @param userId
	 * @param code
	 * @return
	 */
	boolean existsByUserIdAndCode(@Param("userId") Long userId, @Param("code") String code);

	/**
	 * 根据用户ID获取用户所属权限信息名称
	 * 
	 * @param userId        用户名
	 * @param resourceCodes 权限码列表
	 * @return
	 */
	List<String> listResourceCodesByUserId(@Param("userId") Long userId, @Param("resourceCodes") List<String> resourceCodes);
}