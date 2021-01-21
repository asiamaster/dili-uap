package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;

/**
 * 平台公告接口
 * Created by asiamaster on 2021/1/21
 */
@Restful("${uap.contextPath}")
public interface AnnunciateMessageRpc {

	@POST("/api/ws/sendAnnunciate")
	BaseOutput sendAnnunciate(@VOBody AnnunciateMessage annunciateMessage);

}
