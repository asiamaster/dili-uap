package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserResourceRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.dili.uap.sdk.session.PermissionContext;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户资源权限检查标签
 * Created by asiamaster on 2017/7/21 0021.
 */
@Component("resource")
public class ResourceTag extends Tag {

	@Autowired
	private UserResourceRedis userResourceRedis;

	@Autowired
	private UserUrlRedis userUrlRedis;

	/**
	 * 用户id: 有权限的menuUrl
	 */
	private static ThreadLocal<Set<String>> userMenuUrlMap = new ThreadLocal<>();

	//标签自定义属性
	private static final String CODE_FIELD = "code";
	private static final String URL_FIELD = "url";
	private static final String CHECK_MENU_FIELD = "checkMenu";

	@Override
	public void render() {
		Map<String, Object> argsMap = (Map)this.args[1];
		//判断用户是否拥有资源权限
		String code = (String) argsMap.get(CODE_FIELD);
		//判断用户是否拥有菜单url权限
		String url = (String) argsMap.get(URL_FIELD);
		//结合资源权限和菜单url，判断用户是否能访问代码块
		String checkMenu = (String) argsMap.get(CHECK_MENU_FIELD);
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null) {
			try {
				ctx.byteWriter.writeString("用户未登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if(StringUtils.isNotBlank(code)) {
			String[] codes = code.split(",");
			for (int i = 0; i < codes.length; i++) {
				if(writeByCode(codes[i], checkMenu, userTicket)){
					break;
				}
			}
		}else if (StringUtils.isNotBlank(url)){
			writeByUrl(url, userTicket);
		}
	}

	/**
	 * 根据资源code 写出
	 * @param code
	 * @param checkMenu
	 * @param userTicket
	 * @return 是否成功写出
	 */
	private boolean writeByCode(String code, String checkMenu, UserTicket userTicket){
		if (userResourceRedis.checkUserResourceRight(userTicket.getId(), code)) {
			if(StringUtils.isBlank(checkMenu)) {
				write();
			}else{

				//线程缓存中没有该用户的菜单权限，则从redis读取并缓存
				if(get().isEmpty()){
					set((Set)userUrlRedis.getUserMenus(userTicket.getId()));
				}
				PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
				//判断当前访问的url是否是资源所属菜单的url
				if(get().contains(pc.getUrl().trim().replace("http://", "").replace("https://", ""))){
					write();
				}
			}
			return true;
		}
		return false;
	}

	private static void clean() {
		userMenuUrlMap.remove();
	}

	private static Set<String> get() {
		if(userMenuUrlMap.get() == null){
			set(new LinkedHashSet());
		}
		return userMenuUrlMap.get();
	}

	private static void set(Set<String> map){
		userMenuUrlMap.set(map);
	}

	/**
	 * 根据菜单url 写出
	 * @param url
	 * @param userTicket
	 */
	private void writeByUrl(String url, UserTicket userTicket) {
		if (userUrlRedis.checkUserMenuUrlRight(userTicket.getId(), url)) {
			write();
		}
	}

	private void write(){
		try {
			ctx.byteWriter.write(getBodyContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
