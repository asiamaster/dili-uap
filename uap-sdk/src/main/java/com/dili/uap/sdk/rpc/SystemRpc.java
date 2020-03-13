package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Systems;
import com.dili.uap.sdk.domain.dto.SystemDto;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/13 16:00
 */
@Restful("${uap.contextPath}")
public interface SystemRpc {

    @POST("/systemApi/listByExample.api")
    BaseOutput<List<Systems>> listByExample(@VOBody SystemDto systemDto);

    @POST("/systemApi/getByCode.api")
    BaseOutput<Systems> getByCode(@VOBody String code);
}
