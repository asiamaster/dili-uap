package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;

import java.util.List;

/**
 * Created by asiam on 2018/7/12 0012.
 */
@Restful("${uap.contextPath}")
public interface DataAuthRefRpc {

    @POST("/dataAuthRefApi/listSourcesByCode.api")
    BaseOutput<List> listSourcesByCode(@VOBody String refCode);

}
