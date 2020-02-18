package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.DataDictionaryValue;

import java.util.List;

/**
 * 数据字典(值)接口
 * Created by asiam on 2018/7/10 0010.
 */
@Restful("${uap.contextPath}")
public interface DataDictionaryRpc {

    @POST("/dataDictionaryApi/listDataDictionaryValue.api")
    BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue(@VOBody DataDictionaryValue dataDictionaryValue);

    @POST("/dataDictionaryApi/listDataDictionary.api")
    BaseOutput<List<DataDictionary>> listDataDictionary(@VOBody DataDictionary dataDictionary);

    @POST("/dataDictionaryApi/listDataDictionaryValueByDdCode.api")
    BaseOutput<List<DataDictionaryValue>> listDataDictionaryValueByDdCode(@ReqParam("ddCode") String ddCode);
}
