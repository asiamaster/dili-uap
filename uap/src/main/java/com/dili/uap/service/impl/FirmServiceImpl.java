package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.dao.UserDataAuthMapper;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-23 14:31:07.
 */
@Service
public class FirmServiceImpl extends BaseServiceImpl<Firm, Long> implements FirmService {

	@Autowired
	private UserDataAuthMapper userDataAuthMapper;

	public FirmMapper getActualDao() {
		return (FirmMapper) getDao();
	}

	@Override
	public BaseOutput<Object> insertAndBindUserDataAuth(Firm firm) {
		Firm query = DTOUtils.newInstance(Firm.class);
		query.setCode(firm.getCode());
		int count = this.getActualDao().selectCount(query);
		if (count > 0) {
			return BaseOutput.failure("已存在相同的市场编码");
		}
		int rows = this.getActualDao().insertSelective(firm);
		if (rows <= 0) {
			return BaseOutput.failure("插入市场信息失败");
		}
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setUserId(user.getId());
		userDataAuth.setValue(firm.getCode());
		rows = this.userDataAuthMapper.insertSelective(userDataAuth);
		if (rows <= 0) {
			throw new RuntimeException("绑定用户市场数据权限失败");
		}
		return BaseOutput.success();
	}
}