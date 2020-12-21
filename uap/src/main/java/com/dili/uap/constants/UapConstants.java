package com.dili.uap.constants;

/**
 * 系统常量配置
 */
public class UapConstants {

	/**
	 * 统一权限平台系统编码
	 */
	public static final String UAP_SYSTEM_CODE = "UAP";
	/**
	 * 园区管理系统编码
	 */
	public static final String PARK_SYSTEM_CODE = "PARK";
	// 密码错误多次后锁定用户
	public static final String LOGIN_FAILED_TIMES = "loginFailedTimes";
	// 锁定用户恢复时长(毫秒)，默认12小时
	public static final String RESUME_DURATION = "resumeDuration";

	/**
	 * 树状结构，系统ID前缀
	 */
	public static final String SYSTEM_PREFIX = "system_";
	/**
	 * 树状结构，菜单ID前缀
	 */
	public static final String MENU_PREFIX = "menu_";
	/**
	 * 树状结构，资源ID前缀
	 */
	public static final String RESOURCE_PREFIX = "resource_";

	// 集团编码
	public static final String GROUP_CODE = "group";

	/**
	 * 默认的邮箱后缀
	 */
	public static final String EMAIL_POSTFIX = "@diligrp.com";

	/**
	 * 用户的默认密码
	 */
	public static final String DEFAULT_PASS = "123456";

	/**
	 * 树结构，市场code前缀
	 */
	public static final String FIRM_PREFIX = "firm_";
	/**
	 * 树状结构，菜单ID前缀
	 */
	public static final String ALM_PROJECT_PREFIX = "alm_";

	/**
	 * 数据字典编码：企业所属行业
	 */
	public static final String DD_CODE_INDUSTRY = "industry";

	/**
	 * 数据字典编码：证件类型
	 */
	public static final String DD_CODE_CERTIFICATE_TYPE = "enterpriseCertificateType";

	/**
	 * 法人证件类型
	 */
	public static final String DD_CODE_LEGAL_PERSON_CERTIFICATE_TYPE = "legalPersonCertificateType";

	/**
	 * 交易厅数据权限前缀
	 */
	public static final String TRADING_HALL_PREFIX = "trading_hall_";

	/**
	 * 市场新增审批流程定义KEY
	 */
	public static final String FIRM_ADD_APPROVE_PROCESS_KEY = "firmAddProcess";

}
