package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DepartmentService;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.UserDataAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-22 16:10:05.
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

	@Autowired
	FirmMapper firmMapper;

	public DepartmentMapper getActualDao() {
		return (DepartmentMapper) getDao();
	}

	@Autowired
	private UserDataAuthService userDataAuthService;
	@Autowired
	private FirmService firmService;
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional
	public BaseOutput<Department> insertAfterCheck(Department department) {
		Department record = DTOUtils.newInstance(Department.class);
		record.setName(department.getName());
		record.setFirmCode(department.getFirmCode());
		int count = this.getActualDao().selectCount(record);
		if (count > 0) {
			return BaseOutput.failure(this.buildErrorMessage(department));
		}
		Date now = new Date();
		department.setCreated(now);
		department.setModified(now);

		int result = this.getActualDao().insertSelective(department);
		if (result <= 0) {
			return BaseOutput.failure("插入部门失败");
		}
		department.setCode(department.getFirmCode() + "-" + department.getId());
		result = this.getActualDao().updateByPrimaryKey(department);
		if (result <= 0) {
			throw new RuntimeException("更新部门编码失败");
		}
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
		userDataAuth.setUserId(user.getId());
		userDataAuth.setValue(department.getId().toString());
		result = this.userDataAuthService.insert(userDataAuth);
		if (result <= 0) {
			throw new RuntimeException("更新部门编码失败");
		}
		// 更新市场管理员权限
		Firm firm = this.firmService.getByCode(department.getFirmCode());
		if (firm.getUserId() != null) {
			User adminUser = this.userMapper.selectByPrimaryKey(firm.getUserId());
			userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
			userDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
			userDataAuth.setUserId(adminUser.getId());
			userDataAuth.setValue(department.getId().toString());
			result = this.userDataAuthService.insert(userDataAuth);
			if (result <= 0) {
				throw new RuntimeException("更新市场管理员权限失败");
			}
		}
		while (firm.getParentId() != null) {
			firm = this.firmMapper.selectByPrimaryKey(firm.getParentId());
			if (firm.getUserId() != null) {
				User adminUser = this.userMapper.selectByPrimaryKey(firm.getUserId());
				userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
				userDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
				userDataAuth.setUserId(adminUser.getId());
				userDataAuth.setValue(department.getId().toString());
				result = this.userDataAuthService.insert(userDataAuth);
				if (result <= 0) {
					throw new RuntimeException("更新市场管理员权限失败");
				}
			}
		}
		return BaseOutput.success().setData(department);
	}

	@Override
	@Transactional
	public BaseOutput<Department> updateAfterCheck(Department department) {
		Department record = DTOUtils.newInstance(Department.class);
		record.setName(department.getName());
		record.setFirmCode(department.getFirmCode());
		Department oldDept = this.getActualDao().selectOne(record);
		if (oldDept != null && !oldDept.getId().equals(department.getId())) {
			return BaseOutput.failure(this.buildErrorMessage(department));
		}
		department.setModified(new Date());
		int result = this.getActualDao().updateByPrimaryKey(department);
		if (result > 0) {
			return BaseOutput.success().setData(department);
		}
		return BaseOutput.failure("更新失败");
	}

	@Override
	public List<Map> listDepartments(Department department) {
		return this.getActualDao().listDepartments(department);
	}

	/**
	 * 部门相同时，返回一个错误信息字符串
	 * 
	 * @param department
	 * @return 相同市场存在相同部门的错误信息
	 */
	private String buildErrorMessage(Department department) {
		String firmName = "";
		Firm condition = DTOUtils.newInstance(Firm.class);
		condition.setCode(department.getFirmCode());

		Firm firm = firmMapper.selectOne(condition);
		if (firm != null) {
			firmName = firm.getName();
		}

		StringBuilder sb = new StringBuilder().append("[").append(firmName).append("]").append("市场下存在相同的部门").append("[").append(department.getName()).append("]");
		return sb.toString();

	}

	@Override
	public Department getDepartment(Department department) {
		return this.getActualDao().selectOne(department);
	}

	@Override
	public List<Department> findByUserId(Long userId) {
		return this.getActualDao().findByUserId(userId);
	}

	@Override
	public Department getFirstDepartment(Long id) {
		Department department = this.getActualDao().selectByPrimaryKey(id);

		while (department.getParentId() != null) {
			department = this.getActualDao().selectByPrimaryKey(department.getParentId());
		}
		return department;
	}

	@Override
	public List<Department> getChildDepartments(Long parentId) {
		return this.getActualDao().getChildDepartments(parentId);
	}

	@Override
	public List<Department> listUserAuthDepartmentByFirmId(Long userId, Long firmId) {
		if (Objects.isNull(userId) || Objects.isNull(firmId)) {
			return Collections.emptyList();
		}
		UserDataAuth condition = DTOUtils.newInstance(UserDataAuth.class);
		condition.setUserId(userId);
		condition.setRefCode(DataAuthType.DEPARTMENT.getCode());
		List<UserDataAuth> userDataAuthList = userDataAuthService.list(condition);
		if (CollectionUtils.isEmpty(userDataAuthList)) {
			return Collections.emptyList();
		}
		Firm firm = firmMapper.selectByPrimaryKey(firmId);
		if (Objects.isNull(firm)) {
			return Collections.emptyList();
		}
		Department department = DTOUtils.newInstance(Department.class);
		department.setFirmCode(firm.getCode());
		List<Department> departmentList = this.listByExample(department);
		if (CollectionUtils.isEmpty(departmentList)) {
			return Collections.emptyList();
		}
		return departmentList.stream().filter(d -> userDataAuthList.stream().anyMatch(ud -> Objects.equals(String.valueOf(d.getId()), ud.getValue()))).collect(Collectors.toList());
	}

	@Override
	public List<Map> listUserDepartment(String firmCode) {
		List<Map> list = getActualDao().listUserDepartment(firmCode);
		list.forEach(map ->{
			Map<String, String> attributes = new HashMap<>(2);
			attributes.put("firmCode", (String) map.get("firmCode"));
			map.put("attributes",attributes);
		});
		return list;
	}

	@Override
	public List<Long> getSeniorDepartmentIds() {
		return this.getActualDao().getSeniorDepartmentIds();
	}
}