package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface MenuRpc {

	@POST("/menuApi/get.api")
	BaseOutput<Menu> get(@VOBody Long id);

	@POST("/menuApi/list.api")
	BaseOutput<List<Menu>> list(@VOBody Menu menu);

	@POST("/menuApi/listByExample.api")
	BaseOutput<List<Menu>> listByExample(@VOBody Menu menu);

	@POST("/menuApi/getParentMenusByUrl.api")
	BaseOutput<List<Menu>> getParentMenusByUrl(@VOBody String url);

	@POST("/menuApi/getMenuDetailByUrl.api")
	BaseOutput<Map<String, Object>> getMenuDetailByUrl(@VOBody String url);
}
