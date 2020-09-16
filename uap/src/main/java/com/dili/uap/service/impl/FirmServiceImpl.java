package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.dao.UserRoleMapper;
import com.dili.uap.domain.UserRole;
import com.dili.uap.domain.dto.EditFirmAdminUserDto;
import com.dili.uap.domain.dto.FirmAddDto;
import com.dili.uap.domain.dto.FirmUpdateDto;
import com.dili.uap.domain.dto.PaymentFirmDto;
import com.dili.uap.rpc.PayRpc;
import com.dili.uap.rpc.UidRpc;
import com.dili.uap.sdk.domain.*;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.RoleService;
import com.dili.uap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 14:31:07.
 */
@Service
public class FirmServiceImpl extends BaseServiceImpl<Firm, Long> implements FirmService {

	/**
	 * 调用支付系统商户注册接口默认密码
	 */
	private static final String PASSWORD = "123456";

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

	public FirmMapper getActualDao() {
		return (FirmMapper) getDao();
	}

	@Transactional
	@Override
	public BaseOutput<Object> insertAndBindUserDataAuth(FirmAddDto firmDto) {
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if(user == null){
			return BaseOutput.failure("登录超时");
		}
		//统一验证
		String msg = validateAddFirm(firmDto);
		if(null != msg){
			return BaseOutput.failure(msg);
		}
		BaseOutput<String> output = this.uidRpc.getFirmSerialNumber();
		if (!output.isSuccess()) {
			return BaseOutput.failure("获取市场编号失败");
		}
		firmDto.setSerialNumber(Long.parseLong(output.getData()));
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCode(UapConstants.GROUP_CODE);
		Firm groupFirm = this.getActualDao().selectOne(query);
		//将当前市场的parentId设置为集团
		firmDto.setParentId(groupFirm.getId());
		int rows = this.getActualDao().insertSelective(firmDto);
		if (rows <= 0) {
			return BaseOutput.failure("插入市场信息失败");
		}
		//调用支付系统商户注册接口
		PaymentFirmDto paymentFirmDto = new PaymentFirmDto();
		paymentFirmDto.setMchId(firmDto.getId());
		paymentFirmDto.setCode(firmDto.getCode());
		paymentFirmDto.setName(firmDto.getName());
		paymentFirmDto.setAddress(firmDto.getActualDetailAddress());
		paymentFirmDto.setContact(firmDto.getLegalPersonName());
		paymentFirmDto.setMobile(firmDto.getTelephone());
		paymentFirmDto.setPassword(PASSWORD);
		BaseOutput<PaymentFirmDto> registerMerchant = payRpc.registerMerchant(paymentFirmDto);
		if (!registerMerchant.isSuccess()) {
			BaseOutput.failure(registerMerchant.getMessage());
			LOGGER.error(registerMerchant.getMessage());
			throw new AppException(registerMerchant.getMessage());
		}
		//为当前用户设置数据权限，当前用户得看到新增的市场
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
		userDataAuth.setUserId(user.getId());
		userDataAuth.setValue(firmDto.getCode());
		rows = this.userDataAuthMapper.insertSelective(userDataAuth);
		if (rows <= 0) {
			throw new RuntimeException("绑定用户市场数据权限失败");
		}
		return BaseOutput.success().setData(firmDto);
	}

	@Transactional
	@Override
	public BaseOutput<Object> updateSelectiveAfterCheck(FirmUpdateDto dto) {
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCertificateNumber(dto.getCertificateNumber());
		Firm old = this.getActualDao().selectOne(query);
		if (old != null && !dto.getId().equals(old.getId())) {
			return BaseOutput.failure("企业证件号不能重复");
		}
		Firm firm = DTOUtils.as(dto, Firm.class);
		if (firm.getLongTermEffictive()) {
			firm.setCertificateValidityPeriod(null);
		}
		int rows = 0;
		try {
			rows = this.updateExactSimple(dto);

			//调用支付系统修改商户接口
			PaymentFirmDto paymentFirmDto = new PaymentFirmDto();
			paymentFirmDto.setMchId(dto.getId());
			paymentFirmDto.setCode(dto.getCode());
			paymentFirmDto.setName(dto.getName());
			paymentFirmDto.setAddress(dto.getActualDetailAddress());
			paymentFirmDto.setContact(dto.getLegalPersonName());
			paymentFirmDto.setMobile(dto.getTelephone());
			BaseOutput<PaymentFirmDto> modifyMerchant = payRpc.modifyMerchant(paymentFirmDto);
			if (!modifyMerchant.isSuccess()) {
				BaseOutput.failure(modifyMerchant.getMessage());
				LOGGER.error(modifyMerchant.getMessage());
				throw new AppException(modifyMerchant.getMessage());
			}
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
		return rows > 0 ? BaseOutput.success("修改成功").setData(this.getActualDao().selectByPrimaryKey(dto.getId())) : BaseOutput.failure("修改失败");
	}

	@Override
	public Firm getIdByCode(String firmCode) {
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
			adminUser.setPassword(dto.getPassword());
			adminUser.setFirmCode(firm.getCode());
			adminUser.setRealName(firm.getSimpleName());
			BaseOutput output = this.userService.save(adminUser);
			if (!output.isSuccess()) {
				return output;
			}
			firm.setUserId(adminUser.getId());
			firmUpdate = true;
		} else {
			adminUser.setUserName(dto.getUserName());
			adminUser.setCellphone(dto.getCellphone());
			adminUser.setEmail(dto.getEmail());
			adminUser.setPassword(dto.getPassword());
			adminUser.setFirmCode(firm.getCode());
			BaseOutput output = this.userService.save(adminUser);
			if (!output.isSuccess()) {
				return output;
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

		//为当前用户设置数据权限，当前用户得看到新增的市场
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
		userDataAuth.setUserId(adminUser.getId());
		userDataAuth.setValue(firm.getCode());
		int row = this.userDataAuthMapper.insertSelective(userDataAuth);
		if (row <= 0) {
			throw new RuntimeException("绑定用户市场数据权限失败");
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
		if (firm.getState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("该商户状态为已开通状态，请勿重复操作");
		}
		firm.setState(FirmState.ENABLED.getValue());
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
		if (firm.getState().equals(FirmState.DISABLED.getValue())) {
			return BaseOutput.failure("该商户状态为已关闭状态，请勿重复操作");
		}
		firm.setState(FirmState.DISABLED.getValue());
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

	@Override
	public BaseOutput<Object> logicalDelete(Long id) {
		Firm firm = this.getActualDao().selectByPrimaryKey(id);
		if (firm.getDeleted()) {
			return BaseOutput.failure("商户为已删除状态，请勿重复操作");
		}
		if (firm.getState().equals(FirmState.ENABLED.getValue())) {
			return BaseOutput.failure("商户状态为已开通状态，无法关闭");
		}
		LocalDateTime closeTime = firm.getCloseTime();
		LocalDateTime beforeCloseTimeOneYear = closeTime.plusYears(1L);
		if(beforeCloseTimeOneYear.isAfter(LocalDateTime.now())) {
			return BaseOutput.failure("关闭了时间超过1年以上可以删除");
		}
		firm.setDeleted(true);
		int rows = this.getActualDao().updateByPrimaryKeySelective(firm);
		return rows > 0 ? BaseOutput.success() : BaseOutput.failure("删除失败");
	}


	/**
	 * 新增企业统一验证
	 * @param firmDto
	 * @return
	 */
	private String validateAddFirm(FirmAddDto firmDto){
		if (!firmDto.getLongTermEffictive() && firmDto.getCertificateNumber() == null) {
			return "法人身份证有效期不能为空";
		}
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCertificateNumber(firmDto.getCertificateNumber());
		int count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return "企业证件号不能重复";
		}
		query = DTOUtils.newInstance(Firm.class);
		query.setCode(firmDto.getCode());
		//企业简码不能重复
		count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return "已存在相同的企业简码";
		}
		return null;
	}

}