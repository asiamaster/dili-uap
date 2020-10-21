package com.dili.uap.sdk.rpc;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.UserPushInfo;
import com.dili.uap.sdk.domain.dto.UserPushInfoQuery;

/**
 * <B>Description</B> <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/24 17:46
 */
@Restful("${uap.contextPath}")
public interface UserPushInfoRpc {

	/**
	 * 查询他推送信息列表
	 * 
	 * @param userQuery
	 * @return
	 */
	@POST("/userPushInfoApi/listByExample.api")
	BaseOutput<List<UserPushInfo>> listByExample(@VOBody(required = false) UserPushInfoQuery query);
}
