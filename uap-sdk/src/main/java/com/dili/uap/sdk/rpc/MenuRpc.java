package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;
import com.dili.uap.sdk.domain.Menu;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface MenuRpc {

	@POST("/menuApi/get")
	BaseOutput<Menu> get(@VOSingleParameter Long id);

	@POST("/menuApi/list")
	BaseOutput<List<Menu>> list(@VOBody Menu user);

	@POST("/menuApi/listByExample")
	BaseOutput<List<Menu>> listByExample(@VOBody Menu user);

	@POST("/menuApi/getParentMenusByUrl")
	BaseOutput<List<Menu>> getParentMenusByUrl(@VOSingleParameter String url);
}
