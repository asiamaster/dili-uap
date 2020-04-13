package com.dili.uap.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;

@Restful("${uap.contextPath}")
public interface BizNumberRpc {
    @POST("/bizNumberApi/get.api")
    BaseOutput<String> get();
}
