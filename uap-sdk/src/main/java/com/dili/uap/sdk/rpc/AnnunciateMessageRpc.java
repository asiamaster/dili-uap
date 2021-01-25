package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.dto.AnnunciateMessage;
import com.dili.uap.sdk.domain.dto.AnnunciateMessages;

/**
 * 平台公告接口
 * Created by asiamaster on 2021/1/21
 */
@Restful("${uap.contextPath}")
public interface AnnunciateMessageRpc {

	/**
	 * 发送平台公告给一个目标
	 * 目标对象不存在，无法发送, 返回BaseOutput.failure
	 * @param annunciateMessage
	 * @return
	 */
	@POST("/api/ws/sendAnnunciate")
	BaseOutput sendAnnunciate(@VOBody AnnunciateMessage annunciateMessage);

	/**
	 * 发送平台公告给多个目标
	 * 目标对象不存在，无法发送, 返回BaseOutput.failure
	 * @param annunciateMessages
	 * @return
	 */
	@POST("/api/ws/sendAnnunciates")
	BaseOutput sendAnnunciates(@VOBody AnnunciateMessages annunciateMessages);

	/**
	 * 对一个目标撤销平台公告
	 * 目标对象不存在，无法发送, 返回BaseOutput.failure
	 * @param annunciateMessage targetId和id(annunciate表的id)必填
	 * @return
	 */
	@POST("/api/ws/withdrawAnnunciate")
	BaseOutput withdrawAnnunciate(@VOBody AnnunciateMessage annunciateMessage);

	/**
	 * 对多个目标撤销平台公告
	 * 目标对象不存在，无法发送, 返回BaseOutput.failure
	 * @param annunciateMessages targetIds和id(annunciate表的id)必填
	 * @return
	 */
	@POST("/api/ws/withdrawAnnunciates")
	BaseOutput withdrawAnnunciates(@VOBody AnnunciateMessages annunciateMessages);
}
