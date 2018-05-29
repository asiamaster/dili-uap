package com.dili.uap.domain.dto;

import java.util.Date;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.SystemExceptionLog;

public interface SystemExceptionLogDto extends SystemExceptionLog{
	 @Column(name = "`exception_time`")
	    @Operator(Operator.GREAT_EQUAL_THAN)
	    Date getStartExceptionTime();
	    void setStartExceptionTime(Date startExceptionTime);
	    
	    @Column(name = "`exception_time`")
	    @Operator(Operator.LITTLE_EQUAL_THAN)
	    Date getEndExceptionTime();

	    void setEndExceptionTime(Date endExceptionTime);
}
