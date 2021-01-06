package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.dao.TerminalBindingMapper;
import com.dili.uap.domain.TerminalBinding;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.service.AuthService;
import com.dili.uap.service.TerminalBindingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2020-12-08 16:03:34.
 */
@Service
public class TerminalBindingServiceImpl extends BaseServiceImpl<TerminalBinding, Long> implements TerminalBindingService {

	@Autowired
	private AuthService authService;

	public TerminalBindingMapper getActualDao() {
		return (TerminalBindingMapper) getDao();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> bindByToken(String accessToken, String refreshToken, String terminalId) {
		UserTicket user = this.authService.getUserTicket(accessToken, refreshToken);
		if (user == null) {
			return BaseOutput.failure("获取用户信息失败");
		}
		TerminalBinding tb = DTOUtils.newInstance(TerminalBinding.class);
		tb.setUserId(user.getId());
		tb.setTerminalId(terminalId);
		int count = this.getActualDao().selectCount(tb);
		if (count > 0) {
			return BaseOutput.failure("终端号已绑定，请勿重复操作");
		}
		int rows = this.getActualDao().insertSelective(tb);
		return rows > 0 ? BaseOutput.success() : BaseOutput.failure("绑定失败");
	}
}