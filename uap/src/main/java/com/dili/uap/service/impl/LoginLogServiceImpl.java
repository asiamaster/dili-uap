package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.dao.LoginLogMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.dto.LoginLogDto;
import com.dili.uap.service.LoginLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-28 11:33:04.
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, Long> implements LoginLogService {

    public LoginLogMapper getActualDao() {
        return (LoginLogMapper)getDao();
    }

    @Override
	public EasyuiPageOutput findByLoginLogDto(LoginLogDto loginLog, boolean useProvider) throws Exception {
		if (loginLog.getRows() != null && loginLog.getRows().intValue() >= 1) {
			PageHelper.startPage(loginLog.getPage().intValue(), loginLog.getRows().intValue());
		}
		List list = this.getActualDao().findByLoginLogDto(loginLog);
		long total = list instanceof Page ? ((Page) list).getTotal() : (long) list.size();
		List results = useProvider ? ValueProviderUtils.buildDataByProvider(loginLog, list) : list;
		return new EasyuiPageOutput(Integer.valueOf(Integer.parseInt(String.valueOf(total))), results);
	}
}