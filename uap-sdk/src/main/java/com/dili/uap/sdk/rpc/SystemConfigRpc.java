package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.SystemConfig;

import java.util.List;

/**
 * 系统配置接口
 * Created by asiam on 2018/7/10 0010.
 */
@Restful("${uap.contextPath}")
public interface SystemConfigRpc {

    @POST("/systemConfigApi/list.api")
    BaseOutput<List<SystemConfig>> list(@VOBody SystemConfig systemConfig);

    @POST("/systemConfigApi/saveOrUpdate.api")
    BaseOutput saveOrUpdate(@VOBody SystemConfig systemConfig);

    @POST("/systemConfigApi/getByCode.api")
    BaseOutput<SystemConfig> getByCode(@VOBody String code) ;
}
