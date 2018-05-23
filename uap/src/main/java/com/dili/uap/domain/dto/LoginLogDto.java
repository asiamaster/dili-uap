package com.dili.uap.domain.dto;


import javax.persistence.Transient;

import com.dili.uap.domain.LoginLog;
import com.dili.uap.domain.User;


public interface LoginLogDto extends LoginLog,User {


	@Transient
    String getUserName();

    void setUserName(String userName);


}
