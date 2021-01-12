package com.dili.uap.sdk.component.beetl;

import com.dili.uap.sdk.rpc.MenuRpc;
import com.dili.uap.sdk.session.PermissionContext;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.util.WebContent;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取当前页面上级菜单
 * Created by asiamaster on 2020/1/12
 */
@Component("parentMenus")
public class ParentMenusFunction implements Function {
	@SuppressWarnings("all")
	@Autowired
	MenuRpc menuRpc;

	@Override
	public Object call(Object[] objects, Context context) {
		PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
		return menuRpc.getParentMenusByUrl(pc.getUrl()).getData();
	}
}
