package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.beetl.core.Tag;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 用户标签，参数为field，可根据字段名取用户属性
 * Created by asiamaster on 2018/1/22 0021.
 */
@Component("user")
public class userTag extends Tag {

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
			Field userField = userTicket.getClass().getDeclaredField(field);
			userField.setAccessible(true);
			ctx.byteWriter.writeString(userField.get(userTicket).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
