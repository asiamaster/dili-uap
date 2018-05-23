package com.dili.uap.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.dao.LoginLogMapper;
import com.dili.uap.dao.UserMapper;
import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.LoginLogDto;
import com.dili.uap.service.LoginLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 15:30:02.
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, Long> implements LoginLogService {
    public LoginLogMapper getActualDao() {
        return (LoginLogMapper)getDao();
    }

	public EasyuiPageOutput listEasyuiPageByExample(LoginLogDto domain, boolean useProvider) throws Exception {
		//设置分页信息
		Integer page = domain.getPage();
		page = (page == null) ? Integer.valueOf(1) : page;
		if(domain.getRows() != null && domain.getRows() >= 1) {
			//为了线程安全,请勿改动下面两行代码的顺序
			PageHelper.startPage(page, domain.getRows());
		}
		List<LoginLog>list=this.getActualDao().findByLoginLogDto(domain);
		PageInfo<LoginLog> pageInfo = new PageInfo<>(list,5);  
		
		long total = pageInfo.getTotal();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, list) : list;
		return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results);
	}
    
}