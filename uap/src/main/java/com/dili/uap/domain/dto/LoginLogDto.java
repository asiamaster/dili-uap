package com.dili.uap.domain.dto;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.LoginLog;


public interface LoginLogDto extends LoginLog {


	@Transient
    String getUserName();

    void setUserName(String userName);

    @Column(name = "`login_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getStartLoginTime();
    void setStartLoginTime(Date startLoginTime);
    
    @Column(name = "`login_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getEndLoginTime();

    void setEndLoginTime(Date endLoginTime);

    
    
    
    
}
