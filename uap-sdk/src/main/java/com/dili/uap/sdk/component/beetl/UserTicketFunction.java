package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserResourceRedis;
import com.dili.uap.sdk.session.SessionContext;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取登录用户信息
 * Created by asiamaster on 2018/1/25 0026.
 */
@Component("getUserTicket")
public class UserTicketFunction implements Function {

	@Autowired
    UserResourceRedis userResourceRedis;

	@Override
	public Object call(Object[] objects, Context context) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null) {
			return false;
		}
		return userTicket;
	}
}
