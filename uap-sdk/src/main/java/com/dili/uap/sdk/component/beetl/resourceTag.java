package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserResourceRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 用户资源权限检查标签
 * Created by asiamaster on 2017/7/21 0021.
 */
@Component("resource")
public class resourceTag extends Tag {

	@Autowired
	UserResourceRedis userResourceRedis;

	@Autowired
	UserUrlRedis userUrlRedis;
	//标签自定义属性
	private final String CODE_FIELD = "code";
	private final String URL_FIELD = "url";

	@Override
	public void render() {
		Map<String, Object> argsMap = (Map)this.args[1];
		String code = (String) argsMap.get(CODE_FIELD);
		String url = (String) argsMap.get(URL_FIELD);
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null) {
			try {
				ctx.byteWriter.writeString("用户未登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if(StringUtils.isNotBlank(code) && userResourceRedis.checkUserResourceRight(userTicket.getId(), code)){
			try {
				ctx.byteWriter.write(getBodyContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(StringUtils.isNotBlank(url) && userUrlRedis.checkUserMenuUrlRight(userTicket.getId(), url)){
			try {
				ctx.byteWriter.write(getBodyContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
