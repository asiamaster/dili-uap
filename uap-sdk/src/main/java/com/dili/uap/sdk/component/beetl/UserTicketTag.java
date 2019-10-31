package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.beetl.core.Tag;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 用户标签，参数为field，可根据字段名取用户属性
 * Created by asiamaster on 2018/1/22 0021.
 */
@Component("userTicket")
public class UserTicketTag extends Tag {

	//标签自定义属性
	private final String FIELD_KEY = "field";

	@Override
	public void render() {
		Map<String, Object> argsMap = (Map)this.args[1];
		String field = (String) argsMap.get(FIELD_KEY);
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null) {
			try {
				ctx.byteWriter.writeString("用户未登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			ctx.byteWriter.writeString(userTicket.aget(field).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
