package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;

import java.util.List;

/**
 * <B>Description</B> <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/2 18:00
 */
@Restful("${uap.contextPath}")
public interface FirmRpc {

	@POST("/firmApi/listByExample.api")
	BaseOutput<List<Firm>> listByExample(@VOBody FirmDto firm);

	@POST("/firmApi/getByCode.api")
	BaseOutput<Firm> getByCode(@VOBody String code);

	@POST("/firmApi/getById.api")
	BaseOutput<Firm> getById(@VOBody Long id);

	@POST("/firmApi/getAllChildrenByParentId.api")
	BaseOutput<List<Firm>> getAllChildrenByParentId(@VOBody(required = false) Long id);
}
