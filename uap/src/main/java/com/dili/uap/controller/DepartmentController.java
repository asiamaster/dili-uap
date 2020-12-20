package com.dili.uap.controller;

import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DepartmentService;
import com.dili.uap.service.FirmService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:10:05.
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;

	@Autowired
	FirmService firmService;

	@Value("${uap.adminName:admin}")
	private String adminName;


	/**
	 * 跳转到Department页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		// 是否是集团
		boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().equalsIgnoreCase(UapConstants.GROUP_CODE);

		modelMap.addAttribute("isGroup", isGroup);
		modelMap.addAttribute("firmCode", SessionContext.getSessionContext().getUserTicket().getFirmCode());
		return "department/index";
	}

	/**
	 * 查询Department
	 * 
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object list(Department department) {
		List<Map> list = Collections.emptyList();
		// 如果有选中市场，则以选中市场为过滤条件
		// 如果没有选中市场，集团用户不显示任何数据，非集团用户显示其所属于市场数据(页面上没有市场选择框)
		if (StringUtils.isBlank(department.getFirmCode())) {
			boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().equalsIgnoreCase(UapConstants.GROUP_CODE);
			// 非集团用户
			if (!isGroup) {
				department.setFirmCode(SessionContext.getSessionContext().getUserTicket().getFirmCode());
				list = departmentService.listDepartments(department);
			}
		} else {
			list = departmentService.listDepartments(department);
		}

		return new EasyuiPageOutput((long)list.size(), list).toString();
	}

	/**
	 * 分页查询Department
	 * 
	 * @param department
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(Department department) throws Exception {
		return departmentService.listEasyuiPageByExample(department, true).toString();
	}

	/**
	 * 新增Department
	 * 
	 * @param department
	 * @return
	 */
	@BusinessLogger(businessType = "department_management", content = "新增部门", operationType = "add", systemCode = "UAP")
	@RequestMapping(value = "/insert.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(Department department) {
		Department input = this.trimIdAndParentId(department);
		BaseOutput<Department> out = departmentService.insertAfterCheck(input);
		if (out.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, department.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, department.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return this.resetIdAndParentId(out);
	}

	/**
	 * 修改Department
	 * 
	 * @param department
	 * @return
	 */
	@BusinessLogger(businessType = "department_management", content = "修改部门", operationType = "edit", systemCode = "UAP")
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(Department department) {
		Department input = this.trimIdAndParentId(department);
		BaseOutput<Department> out = departmentService.updateAfterCheck(input);
		if (out.isSuccess()) {
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, department.getCode());
			LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, department.getId());
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket != null) {
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
				LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
				LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
			}
		}
		return this.resetIdAndParentId(out);
	}

	/**
	 * 删除Department
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(String id) {
		if (id.startsWith("firm_")) {
			return BaseOutput.failure("不允许删除市场");
		}
		if (id.startsWith("department_")) {
			id = id.replace("department_", "");
		}
		Department department = DTOUtils.newInstance(Department.class);
		department.setParentId(Long.valueOf(id));
		List<Department> departments = departmentService.list(department);
		if (!CollectionUtils.isEmpty(departments)) {
			return BaseOutput.failure("当前部门有下级部门，无法删除");
		}
		department = this.departmentService.get(Long.valueOf(id));
		departmentService.delete(Long.valueOf(id));
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, department.getCode());
		LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, department.getId());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket != null) {
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
			LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
			LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
		}
		return BaseOutput.success("删除成功");
	}

	/**
	 * 根据市场code查询Department
	 * 
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "/listByCondition.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Department> listByCondition(Department department) {
		return departmentService.list(department);
	}

	/**
	 * 查询用户所在市场部门列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/listUserDepartment.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map> listUserDepartment(){
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			return null;
		}
		String userName = userTicket.getUserName();
		String firmCode = null;
		if(!adminName.equals(userName)){
			firmCode = userTicket.getFirmCode();
		}
		return departmentService.listUserDepartment(firmCode);
	}

	/**
	 * 将id以及parentid的前辍去掉
	 * 
	 * @param department
	 * @return
	 */
	private Department trimIdAndParentId(Department department) {
		if (department.aget("id") != null) {
			String id = department.aget("id").toString();
			if (id.startsWith("department_")) {
				department.setId(Long.parseLong(id.replace("department_", "")));
			}
		}
		if (department.aget("parentId") != null) {
			String parentId = department.aget("parentId").toString();
			if (parentId.startsWith("department_")) {
				department.setParentId(Long.parseLong(parentId.replace("department_", "")));
			} else {
				department.setParentId(null);
			}
		}

		return department;
	}

	/**
	 * 在返回数据前加上id以及parentId的前辍
	 * 
	 * @param out
	 * @return
	 */
	private BaseOutput<?> resetIdAndParentId(BaseOutput<Department> out) {
		if (!out.isSuccess()) {
			return out;
		}
		Department department = out.getData();
		String id = department.getId().toString();
		department.aset("id", "department_" + id);
		if (department.getParentId() != null) {
			department.aset("parentId", "department_" + department.getParentId());
		} else {
			department.aset("parentId", "firm_" + department.getFirmCode());
		}
		Object obj = DTOUtils.go(department);
		return BaseOutput.success().setData(obj);
	}
}