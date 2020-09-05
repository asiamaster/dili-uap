package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.BeanConver;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.domain.dto.FirmAddDto;
import com.dili.uap.domain.dto.FirmUpdateDto;
import com.dili.uap.rpc.UidRpc;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 14:31:07.
 */
@Service
public class FirmServiceImpl extends BaseServiceImpl<Firm, Long> implements FirmService {

	@Autowired
	private UserDataAuthMapper userDataAuthMapper;
	@Autowired
	private UidRpc uidRpc;

	public FirmMapper getActualDao() {
		return (FirmMapper) getDao();
	}

	@Transactional
	@Override
	public BaseOutput<Object> insertAndBindUserDataAuth(FirmAddDto firmDto) {
		Firm firm = DTOUtils.as(firmDto, Firm.class);
		if (!firm.getLongTermEffictive() && firm.getCertificateNumber() == null) {
			return BaseOutput.failure("法人身份证有效期不能为空");
		}
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCertificateNumber(firmDto.getCertificateNumber());
		int count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return BaseOutput.failure("企业证件号不能重复");
		}
		BaseOutput<String> output = this.uidRpc.getFirmCode();
		if (!output.isSuccess()) {
			BaseOutput.failure("获取市场编码失败");
		}
		query = DTOUtils.newInstance(Firm.class);
		query.setCode(output.getCode());
		count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return BaseOutput.failure("已存在相同的市场编码");
		}
		firm.setCode(output.getData());
		query.setCode(UapConstants.GROUP_CODE);
		Firm groupFirm = this.getActualDao().selectOne(query);
		firm.setParentId(groupFirm.getId());
		int rows = this.getActualDao().insertSelective(firm);
		if (rows <= 0) {
			return BaseOutput.failure("插入市场信息失败");
		}
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
		userDataAuth.setUserId(user.getId());
		userDataAuth.setValue(firm.getCode());
		rows = this.userDataAuthMapper.insertSelective(userDataAuth);
		if (rows <= 0) {
			throw new RuntimeException("绑定用户市场数据权限失败");
		}
		return BaseOutput.success().setData(firm);
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
		int rows = this.getActualDao().updateByPrimaryKeyExact(firm);
		return rows > 0 ? BaseOutput.success("修改成功").setData(this.getActualDao().selectByPrimaryKey(dto.getId())) : BaseOutput.failure("修改失败");
	}

	@Override
	public Firm getIdByCode(String firmCode) {
		Firm record = DTOUtils.newInstance(Firm.class);
		record.setCode(firmCode);
		return this.getActualDao().selectOne(record);
	}
}