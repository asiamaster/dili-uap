package com.dili.uap.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.bpmc.sdk.domain.ProcessInstanceMapping;
import com.dili.bpmc.sdk.domain.TaskMapping;
import com.dili.bpmc.sdk.dto.StartProcessInstanceDto;
import com.dili.bpmc.sdk.dto.TaskCompleteDto;
import com.dili.bpmc.sdk.rpc.restful.RuntimeRpc;
import com.dili.bpmc.sdk.rpc.restful.TaskRpc;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.DepartmentMapper;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.FirmApproveResult;
import com.dili.uap.domain.UserRole;
import com.dili.uap.domain.dto.EditFirmAdminUserDto;
import com.dili.uap.domain.dto.FirmAddDto;
import com.dili.uap.domain.dto.FirmListDto;
import com.dili.uap.domain.dto.FirmUpdateDto;
import com.dili.uap.domain.dto.PaymentFirmDto;
import com.dili.uap.glossary.Yn;
import com.dili.uap.rpc.PayRpc;
import com.dili.uap.rpc.UidRpc;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.FirmApproveRecord;
import com.dili.uap.sdk.domain.FirmState;
import com.dili.uap.sdk.domain.Role;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.BpmcUtil;
import com.github.pagehelper.Page;

import tk.mybatis.mapper.entity.Example;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 14:31:07.
 */
@Service
public class FirmServiceImpl extends BaseServiceImpl<Firm, Long> implements FirmService {

	/**
	 * 调用支付系统商户注册接口默认密码
	 */
	private static final String PASSWORD = "123456";

	/**
	 * 市场信息同步支付系统的标识
	 */
	@Value("${uap.firm.to.payment.flag:false}")
	private String paymentFlag;

	@Autowired
	private UserDataAuthMapper userDataAuthMapper;
	@Autowired
	private UidRpc uidRpc;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleSerice;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private PayRpc payRpc;
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private RuntimeRpc runtimeRpc;
	@Autowired
	private TaskRpc taskRpc;
	@Autowired
	private BpmcUtil bpmcUtil;

	public FirmMapper getActualDao() {
		return (FirmMapper) getDao();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> saveAndSubmit(FirmUpdateDto dto) {
		if (dto.getId() == null) {
			FirmAddDto addDto = DTOUtils.as(dto, FirmAddDto.class);
			BaseOutput<Object> output = this.addFirm(addDto);
			if (!output.isSuccess()) {
				return output;
			}
			return this.submit(addDto.getId(), dto.getTaskId());
		} else {
			UserTicket user = SessionContext.getSessionContext().getUserTicket();
			if (user == null) {
				return BaseOutput.failure("用户未登录");
			}
			BaseOutput<Object> output = this.updateSelectiveAfterCheck(dto);
			if (!output.isSuccess()) {
				return output;
			}
			return this.submit(dto.getId(), dto.getTaskId());
		}
	}

	@Transactional
	@Override
	public BaseOutput<Object> addFirm(FirmAddDto firmDto) {
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("登录超时");
		}
		firmDto.setCreatorId(user.getId());
		// 统一验证
		String msg = validateAddFirm(firmDto);
		if (null != msg) {
			return BaseOutput.failure(msg);
		}
		BaseOutput<String> output = this.uidRpc.getFirmSerialNumber();
		if (!output.isSuccess()) {
			return BaseOutput.failure("获取市场编号失败");
		}
		firmDto.setSerialNumber(Long.parseLong(output.getData()));
		firmDto.setFirmState(FirmState.UNREVIEWED.getValue());
		int rows = this.getActualDao().insertSelective(firmDto);
		if (rows <= 0) {
			return BaseOutput.failure("插入市场信息失败");
		}
		return BaseOutput.success().setData(firmDto);
	}

	@Transactional
	@Override
	public BaseOutput<Object> updateSelectiveAfterCheck(FirmUpdateDto dto) {
		Firm firmQuery = DTOUtils.newInstance(Firm.class);
		firmQuery.setCode(dto.getCode());
		Firm old = this.getActualDao().selectOne(firmQuery);
		if (old != null && !old.getId().equals(dto.getId())) {
			return BaseOutput.failure("已存在相同编码的商户");
		}
		Firm firm = this.getActualDao().selectByPrimaryKey(dto.getId());
		if (!firm.getFirmState().equals(FirmState.UNREVIEWED.getValue()) && !firm.getFirmState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("当前状态不能修改商户信息");
		}
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCertificateNumber(dto.getCertificateNumber());
		old = this.getActualDao().selectOne(query);
		if (old != null && !dto.getId().equals(old.getId())) {
			return BaseOutput.failure("企业证件号不能重复");
		}
		firm = DTOUtils.as(dto, Firm.class);
		if (firm.getLongTermEffictive()) {
			firm.setCertificateValidityPeriod(null);
		}
		int rows = 0;
		rows = this.updateExactSimple(dto);
		return rows > 0 ? BaseOutput.success("修改成功").setData(this.getActualDao().selectByPrimaryKey(dto.getId())) : BaseOutput.failure("修改失败");
	}

	@Override
	public Firm getByCode(String firmCode) {
		Firm record = DTOUtils.newInstance(Firm.class);
		record.setCode(firmCode);
		return this.getActualDao().selectOne(record);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> updateAdminUser(EditFirmAdminUserDto dto) {
		Firm firm = this.getActualDao().selectByPrimaryKey(dto.getId());
		if (firm == null) {
			return BaseOutput.failure("商户不存在");
		}
		if (!firm.getFirmState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("当前商户状态不能设置超级管理员");
		}
		boolean firmUpdate = false;
		User adminUser = null;
		if (dto.getUserId() != null) {
			adminUser = this.userService.get(dto.getUserId());
		}
		if (adminUser == null) {
			adminUser = DTOUtils.newInstance(User.class);
			adminUser.setUserName(dto.getUserName());
			adminUser.setCellphone(dto.getCellphone());
			adminUser.setEmail(dto.getEmail());
			adminUser.setFirmCode(firm.getCode());
			adminUser.setRealName(firm.getSimpleName());
			// 先判断市场的简称存不存在，不存在则用市场名称，老市场的简称可能不存在
			if (firm.getSimpleName() == null || "".equals(firm.getSimpleName())) {
				adminUser.setRealName(firm.getName());
			}
			BaseOutput output = this.userService.save(adminUser);
			if (!output.isSuccess()) {
				return output;
			}
			firm.setUserId(adminUser.getId());
			firmUpdate = true;
		} else {
			// 校验用户账号是否存在
			List<User> userList = new ArrayList<User>();
			User query = DTOUtils.newInstance(User.class);
			query.setUserName(dto.getUserName());
			userList = userService.listByExample(query);
			if (CollectionUtils.isNotEmpty(userList)) {
				// 匹配是否有用户ID不等当前修改记录的用户
				boolean result = userList.stream().anyMatch(u -> !u.getId().equals(dto.getUserId()));
				if (result) {
					return BaseOutput.failure("用户账号已存在");
				}
			}
			adminUser.setUserName(dto.getUserName());
			adminUser.setCellphone(dto.getCellphone());
			adminUser.setEmail(dto.getEmail());
			adminUser.setFirmCode(firm.getCode());
			BaseOutput output = this.userService.save(adminUser);
			if (!output.isSuccess()) {
				return output;
			}
		}

		// 默认设置用户的权限
		if (firmUpdate) {
			List<Firm> firms = this.getActualDao().selectAllChildrenFirms(firm.getId());
			// 为当前用户设置数据权限，当前用户得看到新增的市场
			List<UserDataAuth> dataAuths = new ArrayList<UserDataAuth>(firms.size());
			for (Firm f : firms) {
				UserDataAuth depDataAuth = DTOUtils.newInstance(UserDataAuth.class);
				depDataAuth.setRefCode(DataAuthType.MARKET.getCode());
				depDataAuth.setUserId(adminUser.getId());
				depDataAuth.setValue(f.getCode());
				dataAuths.add(depDataAuth);
			}
			this.userDataAuthMapper.insertList(dataAuths);
			// 新增部门权限
			List<String> firmCodes = new ArrayList<String>(firms.size());
			firms.forEach(f -> firmCodes.add(f.getCode()));
			Example example = new Example(Department.class);
			example.createCriteria().andIn("firmCode", firmCodes);
			List<Department> departments = this.departmentMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(departments)) {
				List<UserDataAuth> dataAuthList = new ArrayList<UserDataAuth>(departments.size());
				for (Department d : departments) {
					UserDataAuth depDataAuth = DTOUtils.newInstance(UserDataAuth.class);
					depDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
					depDataAuth.setUserId(adminUser.getId());
					depDataAuth.setValue(d.getId().toString());
					dataAuthList.add(depDataAuth);
				}
				this.userDataAuthMapper.insertList(dataAuthList);
			}
		} else {
			// 更新市场数据权限
			List<Firm> firms = this.getActualDao().selectAllChildrenFirms(firm.getId());
			List<String> firmCodes = new ArrayList<String>(firms.size());
			firms.forEach(f -> firmCodes.add(f.getCode()));
			Example example = new Example(UserDataAuth.class);
			example.createCriteria().andEqualTo("refCode", DataAuthType.MARKET.getCode()).andEqualTo("userId", adminUser.getId()).andIn("value", firmCodes);
			this.userDataAuthMapper.deleteByExample(example);
			// 为当前用户设置数据权限，当前用户得看到新增的市场
			List<UserDataAuth> dataAuths = new ArrayList<UserDataAuth>(firms.size());
			for (Firm f : firms) {
				UserDataAuth depDataAuth = DTOUtils.newInstance(UserDataAuth.class);
				depDataAuth.setRefCode(DataAuthType.MARKET.getCode());
				depDataAuth.setUserId(adminUser.getId());
				depDataAuth.setValue(f.getCode());
				dataAuths.add(depDataAuth);
			}
			this.userDataAuthMapper.insertList(dataAuths);
			// 更新部门权限
			example = new Example(Department.class);
			example.createCriteria().andIn("firmCode", firmCodes);
			List<Department> departments = this.departmentMapper.selectByExample(example);
			if (CollectionUtils.isNotEmpty(departments)) {
				example = new Example(UserDataAuth.class);
				List<Long> departmentIds = new ArrayList<Long>(firms.size());
				departments.forEach(d -> departmentIds.add(d.getId()));
				example.createCriteria().andEqualTo("refCode", DataAuthType.DEPARTMENT.getCode()).andEqualTo("userId", adminUser.getId()).andIn("value", departmentIds);
				this.userDataAuthMapper.deleteByExample(example);
				List<UserDataAuth> dataAuthList = new ArrayList<UserDataAuth>(departments.size());
				for (Department d : departments) {
					UserDataAuth depDataAuth = DTOUtils.newInstance(UserDataAuth.class);
					depDataAuth.setRefCode(DataAuthType.DEPARTMENT.getCode());
					depDataAuth.setUserId(adminUser.getId());
					depDataAuth.setValue(d.getId().toString());
					dataAuthList.add(depDataAuth);
				}
				this.userDataAuthMapper.insertList(dataAuthList);
			}
		}

		Role role = null;
		if (dto.getRoleId() != null) {
			role = this.roleSerice.get(dto.getRoleId());
		}
		if (role == null) {
			role = DTOUtils.newInstance(Role.class);
			role.setRoleName(firm.getName() + "管理员");
			role.setFirmCode(firm.getCode());
			int rows = this.roleSerice.insertSelective(role);
			if (rows <= 0) {
				throw new AppException("更新角色失败");
			}
			firm.setRoleId(role.getId());
			firmUpdate = true;
		}
		BaseOutput output = this.roleSerice.saveRoleMenuAndResource(role.getId(), dto.getResourceIds().toArray(new String[] {}));
		if (!output.isSuccess()) {
			throw new AppException(output.getMessage());
		}
		UserRole urQuery = DTOUtils.newInstance(UserRole.class);
		urQuery.setUserId(adminUser.getId());
		urQuery.setRoleId(role.getId());
		int count = this.userRoleMapper.selectCount(urQuery);
		if (count <= 0) {
			int rows = this.userRoleMapper.insertSelective(urQuery);
			if (rows <= 0) {
				throw new AppException("绑定用户角色失败");
			}
		}
		if (firmUpdate) {
			int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
			if (rows <= 0) {
				throw new AppException("更新商户失败");
			}
		}
		return BaseOutput.success();
	}

	@Override
	public BaseOutput<Object> enable(Long id) {
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm.getFirmState().equals(FirmState.DISABLED.getValue())) {
			return BaseOutput.failure("当前商户状态不能进行此操作");
		}
		if (firm.getFirmState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("该商户状态为已开通状态，请勿重复操作");
		}
		firm.setFirmState(FirmState.ENABLED.getValue());
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		if (firm.getUserId() != null) {
			BaseOutput output = this.userService.upateEnable(firm.getUserId(), true);
			if (!output.isSuccess()) {
				return output;
			}
		}
		return rows > 0 ? BaseOutput.success() : BaseOutput.failure("更新状态失败");
	}

	@Override
	public BaseOutput<Object> disable(Long id) {
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm.getFirmState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("当前商户状态不能进行此操作");
		}
		if (firm.getFirmState().equals(FirmState.DISABLED.getValue())) {
			return BaseOutput.failure("该商户状态为已关闭状态，请勿重复操作");
		}
		firm.setFirmState(FirmState.DISABLED.getValue());
		firm.setCloseTime(LocalDateTime.now());
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		if (firm.getUserId() != null) {
			BaseOutput output = this.userService.upateEnable(firm.getUserId(), false);
			if (!output.isSuccess()) {
				return output;
			}
		}
		return rows > 0 ? BaseOutput.success() : BaseOutput.failure("更新状态失败");
	}

	/**
	 * 新增企业统一验证
	 * 
	 * @param firmDto
	 * @return
	 */
	private String validateAddFirm(FirmAddDto firmDto) {
		Firm firmQuery = DTOUtils.newInstance(Firm.class);
		firmQuery.setCode(firmDto.getCode());
		int count = this.getActualDao().selectCount(firmQuery);
		if (count > 0) {
			return "已存在相同编码的商户";
		}
		if (!firmDto.getLongTermEffictive() && firmDto.getCertificateNumber() == null) {
			return "法人身份证有效期不能为空";
		}
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCertificateNumber(firmDto.getCertificateNumber());
		count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return "企业证件号不能重复";
		}
		query = DTOUtils.newInstance(Firm.class);
		query.setCode(firmDto.getCode());
		// 企业简码不能重复
		count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return "已存在相同的企业简码";
		}
		return null;
	}

	@Override
	public List<Firm> getAllChildrenByParentId(Long parentId) {
		if (parentId == null) {
			Example example = new Example(Firm.class);
			example.createCriteria().andIsNull("parentId").andEqualTo("firmState", FirmState.ENABLED.getValue()).andEqualTo("deleted", Yn.NO.getCode());
			return this.getActualDao().selectByExample(example);
		}
		return this.getActualDao().selectAllChildrenFirms(parentId);
	}

	@Override
	public BaseOutput<Object> submit(Long id, String taskId) {
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("用户未登录");
		}
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm == null) {
			return BaseOutput.failure("商户不存在");
		}
		if (!firm.getFirmState().equals(FirmState.UNREVIEWED.getValue()) && !firm.getFirmState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("当前状态不能提交审批");
		}
		firm.setFirmState(FirmState.APPROVING.getValue());
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		if (rows <= 0) {
			return BaseOutput.failure("更新商户状态失败");
		}

		if (StringUtils.isNotEmpty(taskId)) {
			if (this.isNeedClaim(taskId)) {
				BaseOutput<String> output = this.taskRpc.claim(taskId, user.getId().toString());
				if (!output.isSuccess()) {
					LOGGER.error(output.getMessage());
					throw new AppException("签收流程任务失败");
				}
			}
			BaseOutput<String> output = this.taskRpc.complete(taskId);
			if (!output.isSuccess()) {
				LOGGER.error(output.getMessage());
				throw new AppException("执行流程任务失败");
			}
		} else {
			StartProcessInstanceDto startProcessInstanceDto = DTOUtils.newInstance(StartProcessInstanceDto.class);
			startProcessInstanceDto.setProcessDefinitionKey(UapConstants.FIRM_ADD_APPROVE_PROCESS_KEY);
			startProcessInstanceDto.setBusinessKey(id.toString());
			BaseOutput<ProcessInstanceMapping> output = this.runtimeRpc.startProcessInstanceByKey(startProcessInstanceDto);
			if (!output.isSuccess()) {
				LOGGER.error(output.getMessage());
				throw new AppException("执行流程任务失败");
			}
			firm.setProcessDefinitionId(output.getData().getProcessDefinitionId());
			firm.setProcessInstanceId(output.getData().getProcessInstanceId());
			rows = this.getActualDao().updateByPrimaryKeySelective(firm);
			if (rows <= 0) {
				LOGGER.error(output.getMessage());
				throw new AppException("更新流程信息失败");
			}
		}
		return BaseOutput.success();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> reject(Long id, String taskId, Long approverId, String notes) {
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm == null) {
			return BaseOutput.failure("商户不存在");
		}
		if (!firm.getFirmState().equals(FirmState.APPROVING.getValue())) {
			return BaseOutput.failure("当前状态进行审批操作");
		}
		firm.setFirmState(FirmState.UNREVIEWED.getValue());
		List<FirmApproveRecord> results = null;
		if (StringUtils.isNotBlank(firm.getApproveResult())) {
			results = JSON.parseObject(firm.getApproveResult(), new TypeReference<List<FirmApproveRecord>>() {
			});
		} else {
			results = new ArrayList<FirmApproveRecord>();
		}
		FirmApproveRecord result = DTOUtils.newInstance(FirmApproveRecord.class);
		result.setApproveTime(LocalDateTime.now());
		result.setApproverId(approverId);
		result.setNotes(notes);
		result.setResult(FirmApproveResult.REJECTED.getValue());
		results.add(result);
		firm.setApproveResult(JSON.toJSONString(results));
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		if (rows <= 0) {
			return BaseOutput.failure("更新商户信息失败");
		}
		if (this.isNeedClaim(taskId)) {
			BaseOutput<String> claimOutput = this.taskRpc.claim(taskId, approverId.toString());
			if (!claimOutput.isSuccess()) {
				throw new AppException(claimOutput.getMessage());
			}
		}
		TaskCompleteDto completeDto = DTOUtils.newInstance(TaskCompleteDto.class);
		completeDto.setTaskId(taskId);
		completeDto.setVariables(new HashMap<String, Object>() {
			{
				put("approveResult", FirmApproveResult.REJECTED.getValue());
				put("creatorId", firm.getCreatorId());
			}
		});

		BaseOutput<String> taskOutput = this.taskRpc.complete(completeDto);
		if (!taskOutput.isSuccess()) {
			throw new AppException("执行流程任务失败");
		}
		return BaseOutput.success();
	}

	private boolean isNeedClaim(String taskId) {
		BaseOutput<TaskMapping> output = this.taskRpc.getById(taskId);
		if (!output.isSuccess()) {
			LOGGER.error(output.getMessage());
			throw new AppException("查询流程任务失败");
		}
		return StringUtils.isBlank(output.getData().getAssignee());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> accept(Long id, String taskId, Long approverId, String notes) {
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("登录超时");
		}
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm == null) {
			return BaseOutput.failure("商户不存在");
		}
		if (!firm.getFirmState().equals(FirmState.APPROVING.getValue())) {
			return BaseOutput.failure("当前状态进行审批操作");
		}
		firm.setFirmState(FirmState.ENABLED.getValue());
		List<FirmApproveRecord> results = null;
		if (StringUtils.isNotBlank(firm.getApproveResult())) {
			results = JSON.parseObject(firm.getApproveResult(), new TypeReference<List<FirmApproveRecord>>() {
			});
		} else {
			results = new ArrayList<FirmApproveRecord>();
		}
		FirmApproveRecord result = DTOUtils.newInstance(FirmApproveRecord.class);
		result.setApproveTime(LocalDateTime.now());
		result.setApproverId(approverId);
		result.setNotes(notes);
		result.setResult(FirmApproveResult.ACCEPTED.getValue());
		results.add(result);
		firm.setApproveResult(JSON.toJSONString(results));
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		if (rows <= 0) {
			return BaseOutput.failure("更新商户信息失败");
		}

		// 如果nacos配置项uap.firm.to.payment.flag的值不为false,则市场信息同步到支付系统；调用支付系统商户注册接口
		if (!"false".equalsIgnoreCase(paymentFlag)) {
			PaymentFirmDto paymentFirmDto = new PaymentFirmDto();
			paymentFirmDto.setMchId(firm.getId());
			paymentFirmDto.setParentId(firm.getParentId());
			paymentFirmDto.setCode(firm.getCode());
			paymentFirmDto.setName(firm.getName());
			paymentFirmDto.setAddress(firm.getActualDetailAddress());
			paymentFirmDto.setContact(firm.getLegalPersonName());
			paymentFirmDto.setMobile(firm.getTelephone());
			paymentFirmDto.setPassword(PASSWORD);
			BaseOutput<PaymentFirmDto> registerMerchant = payRpc.registerMerchant(paymentFirmDto);
			if (!registerMerchant.isSuccess()) {
				BaseOutput.failure(registerMerchant.getMessage());
				LOGGER.error(registerMerchant.getMessage());
				throw new AppException(registerMerchant.getMessage());
			}
		}
		// 为当前用户设置数据权限，当前用户得看到新增的市场
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
		userDataAuth.setUserId(user.getId());
		userDataAuth.setValue(firm.getCode());
		rows = this.userDataAuthMapper.insertSelective(userDataAuth);
		if (rows <= 0) {
			throw new RuntimeException("绑定用户市场数据权限失败");
		}

		BaseOutput<TaskMapping> output = this.taskRpc.getById(taskId);
		if (!output.isSuccess()) {
			LOGGER.error(output.getMessage());
			throw new AppException("查询流程任务失败");
		}
		if (StringUtils.isBlank(output.getData().getAssignee())) {
			BaseOutput<String> claimOutput = this.taskRpc.claim(taskId, approverId.toString());
			if (!claimOutput.isSuccess()) {
				throw new AppException(claimOutput.getMessage());
			}
		}
		TaskCompleteDto completeDto = DTOUtils.newInstance(TaskCompleteDto.class);
		completeDto.setTaskId(taskId);
		completeDto.setVariables(new HashMap<String, Object>() {
			{
				put("approveResult", FirmApproveResult.ACCEPTED.getValue());
			}
		});

		BaseOutput<String> taskOutput = this.taskRpc.complete(completeDto);
		if (!taskOutput.isSuccess()) {
			throw new AppException("执行流程任务失败");
		}
		return BaseOutput.success();
	}

	@Override
	public BaseOutput<Object> deleteAndStopProcess(Long id, String taskId) {
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm == null) {
			return BaseOutput.failure("商户不存在");
		}
		if (!firm.getFirmState().equals(FirmState.UNREVIEWED.getValue())) {
			return BaseOutput.failure("当前状态进行删除操作");
		}
		int rows = this.getActualDao().deleteByPrimaryKey(id);
		if (rows <= 0) {
			return BaseOutput.failure("删除市场失败");
		}
		if (StringUtils.isNotBlank(taskId)) {
			BaseOutput<TaskMapping> output = this.taskRpc.getById(taskId);
			if (!output.isSuccess()) {
				LOGGER.error(output.getMessage());
				throw new AppException("查询流程任务失败");
			}
			if (StringUtils.isBlank(output.getData().getAssignee())) {
				BaseOutput<String> claimOutput = this.taskRpc.claim(taskId, firm.getCreatorId().toString());
				if (!claimOutput.isSuccess()) {
					throw new AppException(claimOutput.getMessage());
				}
			}
			TaskCompleteDto completeDto = DTOUtils.newInstance(TaskCompleteDto.class);
			completeDto.setTaskId(taskId);
			completeDto.setVariables(new HashMap<String, Object>() {
				{
					put("deleteFlag", 1);
				}
			});
			BaseOutput<String> taskOutput = this.taskRpc.complete(completeDto);
			if (!taskOutput.isSuccess()) {
				throw new AppException("执行流程任务失败");
			}
		}

		return BaseOutput.success();
	}

	@Override
	public EasyuiPageOutput listEasyuiPageByExample(Firm domain, boolean useProvider) throws Exception {
		List<Firm> list = listByExample(domain);
		long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
		List<FirmListDto> dtos = DTOUtils.as(list, FirmListDto.class);
		this.bpmcUtil.fitLoggedUserIsCanHandledProcess(dtos);
		List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, dtos) : dtos;
		return new EasyuiPageOutput(total, results);
	}

}