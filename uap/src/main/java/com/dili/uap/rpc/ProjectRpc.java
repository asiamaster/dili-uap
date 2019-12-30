package com.dili.uap.rpc;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.domain.Project;

@Restful("http://alm.diligrp.com")
public interface ProjectRpc {
	@POST("/projectApi/selectAll.api")
	BaseOutput<List<Project>> selectAll();
	@POST("/projectApi/get.api")
	BaseOutput<Project> get(@VOBody Long value);

}
