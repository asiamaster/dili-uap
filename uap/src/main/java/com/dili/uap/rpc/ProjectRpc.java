package com.dili.uap.rpc;

import java.util.Collection;
import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.domain.dto.UserDataDto;


@Restful("${alm.contextPath}")
public interface ProjectRpc {
	@POST("/projectApi/selectUserDataByIds.api")
	BaseOutput<List<UserDataDto>> selectUserDataByIds(@VOBody Collection<String> ids);
	@POST("/projectApi/selectUserDataTree.api")
	BaseOutput<List<UserDataDto>> selectUserDataTree();
}
